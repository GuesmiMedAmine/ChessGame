// controlleur/Controlleur.java
package controlleur;

import events.GameEndEvent;
import events.GameEndListener;
import events.NewGameEvent;
import events.NewGameListener;
import factory.GameFactory;
import modele.jeu.Jeu;
import modele.pieces.PieceColor;
import modele.plateau.Case;
import utils.ThreadManager;
import vue.VueControleur;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Contrôleur central du jeu.
 * Initialise modèle et vue, et orchestre les clics de la souris.
 * Implémente GameEndListener pour gérer les événements de fin de partie.
 * Implémente NewGameListener pour gérer les événements de nouvelle partie.
 */
public class Controlleur implements GameEndListener, NewGameListener {
    private Jeu jeu;
    private VueControleur vue;
    private JFrame frame;

    /**
     * Constructeur qui crée un nouveau Jeu, instancie la Vue, et branche les gestionnaires de clics.
     */
    public Controlleur() {
        // Modèle
        this.jeu = GameFactory.createStandardGame();

        // Vue, injectée avec le modèle
        this.vue = new VueControleur(jeu);

        // Lier les clics
        vue.addCaseClickListener(new CaseClickHandler());

        // S'enregistrer comme listener pour les événements
        vue.addGameEndListener(this);
        vue.addNewGameListener(this);

        // Démarrer le timer
        vue.startTimer();
    }

    /**
     * Définit la fenêtre principale de l'application.
     * 
     * @param frame La fenêtre principale
     */
    public void setFrame(JFrame frame) {
        this.frame = frame;

        // Ajouter la barre de menu à la fenêtre
        frame.setJMenuBar(vue.getMenuBar());
    }

    public VueControleur getVue() {
        return vue;
    }

    /**
     * Gère les événements de fin de partie.
     * 
     * @param event L'événement de fin de partie
     */
    @Override
    public void gameEndRequested(GameEndEvent event) {
        // Demander confirmation
        int response = JOptionPane.showConfirmDialog(
                vue, 
                "Voulez-vous vraiment terminer la partie ?", 
                "Confirmation", 
                JOptionPane.YES_NO_OPTION);

        if (response == JOptionPane.YES_OPTION) {
            // Arrêter le timer
            vue.stopTimer();

            // Terminer la partie (match nul)
            jeu.terminerPartie(null);

            // Afficher un message
            JOptionPane.showMessageDialog(
                    vue, 
                    "La partie a été terminée manuellement.", 
                    "Partie terminée", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Gère les événements de nouvelle partie.
     * 
     * @param event L'événement de nouvelle partie
     */
    @Override
    public void newGameRequested(NewGameEvent event) {
        if (event.isProfessional()) {
            // Demander confirmation
            int response = JOptionPane.showConfirmDialog(
                    vue, 
                    "Voulez-vous vraiment démarrer une nouvelle partie professionnelle ?", 
                    "Confirmation", 
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                // Démarrer une nouvelle partie professionnelle
                startNewProGame();
            }
        } else {
            // Demander confirmation
            int response = JOptionPane.showConfirmDialog(
                    vue, 
                    "Voulez-vous vraiment démarrer une nouvelle partie standard ?", 
                    "Confirmation", 
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                // Démarrer une nouvelle partie standard
                startNewStandardGame();
            }
        }
    }

    /**
     * Démarre une nouvelle partie standard.
     */
    public void startNewStandardGame() {
        // Arrêter le timer actuel
        vue.stopTimer();

        // Créer une nouvelle partie standard
        this.jeu = GameFactory.createStandardGame();

        // Créer une nouvelle vue
        this.vue = new VueControleur(jeu);

        // Lier les clics
        vue.addCaseClickListener(new CaseClickHandler());

        // S'enregistrer comme listener pour les événements
        vue.addGameEndListener(this);
        vue.addNewGameListener(this);

        // Démarrer le timer
        vue.startTimer();

        // Mettre à jour la fenêtre
        if (frame != null) {
            frame.setContentPane(vue);
            frame.setJMenuBar(vue.getMenuBar());
            frame.revalidate();
            frame.repaint();
        }
    }

    /**
     * Démarre une nouvelle partie professionnelle.
     */
    public void startNewProGame() {
        // Arrêter le timer actuel
        vue.stopTimer();

        // Créer une nouvelle partie professionnelle
        this.jeu = GameFactory.createProGame();

        // Créer une nouvelle vue
        this.vue = new VueControleur(jeu);

        // Lier les clics
        vue.addCaseClickListener(new CaseClickHandler());

        // S'enregistrer comme listener pour les événements
        vue.addGameEndListener(this);
        vue.addNewGameListener(this);

        // Démarrer le timer
        vue.startTimer();

        // Mettre à jour la fenêtre
        if (frame != null) {
            frame.setContentPane(vue);
            frame.setJMenuBar(vue.getMenuBar());
            frame.revalidate();
            frame.repaint();
        }
    }

    /**
     * Handler des clics sur les cases : sélection, tentative de coup.
     */
    private class CaseClickHandler extends MouseAdapter {
        private Case selectedCase;

        @Override
        public void mouseClicked(MouseEvent e) {
            // Si la partie est terminée, ne rien faire
            if (jeu.estPartieTerminee()) {
                return;
            }

            // Récupérer la position (x,y) depuis la propriété client
            Object prop = e.getSource();
            var lbl = (javax.swing.JLabel) prop;
            Point pt = (Point) lbl.getClientProperty("pos");
            Case clicked = jeu.getPlateau().getCase(pt.x, pt.y);

            if (selectedCase == null) {
                // 1ère sélection : si une pièce du joueur courant
                if (clicked.getPiece() != null
                        && clicked.getPiece().getColor() == jeu.getJoueurActuel()) {
                    selectedCase = clicked;

                    // Calculer les coups valides en arrière-plan
                    ThreadManager.getInstance().executeInBackgroundThenOnEDT(
                        // Tâche en arrière-plan
                        () -> {
                            // Récupérer les coups valides (pas besoin de retourner, on les recalcule dans l'EDT)
                            clicked.getPiece().getCasesAccessibles();
                        },
                        // Tâche sur l'EDT une fois terminée
                        () -> {
                            // Indiquer à la vue la sélection et les déplacements possibles
                            vue.selectCase(selectedCase, clicked.getPiece().getCasesAccessibles());
                        }
                    );
                }
            } else {
                // 2ème clic : tenter le coup
                final Case depart = selectedCase;
                final Case arrivee = clicked;

                // Effacer la sélection immédiatement pour feedback utilisateur
                selectedCase = null;
                vue.clearSelection();

                // Créer une référence finale à la case de départ et d'arrivée
                final Case finalDepart = depart;
                final Case finalArrivee = arrivee;

                // Jouer le coup en arrière-plan
                ThreadManager.getInstance().executeInBackgroundThenOnEDT(
                    // Tâche en arrière-plan
                    () -> {
                        // Jouer le coup
                        jeu.jouerCoup(finalDepart, finalArrivee);
                    },
                    // Tâche sur l'EDT une fois terminée
                    () -> {
                        // Changer le joueur actif du timer
                        vue.switchTimerPlayer();

                        // Vérifier si la partie est terminée après le coup
                        if (jeu.estPartieTerminee()) {
                            // Arrêter le timer
                            vue.stopTimer();

                            // Afficher le message de fin de partie
                            afficherFinPartie();
                        }
                    }
                );
            }
        }

        /**
         * Affiche un message de fin de partie.
         */
        private void afficherFinPartie() {
            PieceColor vainqueur = jeu.getVainqueur();
            String message;

            if (vainqueur != null) {
                String couleurGagnante = (vainqueur == PieceColor.WHITE ? "Blanc" : "Noir");
                message = "ÉCHEC ET MAT ! Les " + couleurGagnante + "s ont gagné la partie !";
            } else {
                message = "PAT ! La partie est nulle.";
            }

            // Afficher le message dans une boîte de dialogue
            javax.swing.JOptionPane.showMessageDialog(vue, message, "Fin de partie", 
                                                     javax.swing.JOptionPane.INFORMATION_MESSAGE);

            // Afficher également dans la console
            System.out.println("\n" + message);
        }
    }
}
