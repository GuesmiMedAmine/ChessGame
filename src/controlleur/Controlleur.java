
package controlleur;

import events.GameEndEvent;
import events.GameEndListener;
import events.NewGameEvent;
import events.NewGameListener;
import events.UndoEvent;
import events.UndoListener;
import events.RedoEvent;
import events.RedoListener;
import factory.GameFactory;
import modele.jeu.Jeu;
import modele.pieces.PieceColor;
import modele.plateau.Case;
import utils.ThreadManager;
import vue.IView;
import vue.console.ConsoleView;
import vue.graphique.GraphicalView;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Contrôleur central du jeu.
 * Initialise modèle et vue, et orchestre les clics de la souris.
 * Implémente GameEndListener pour gérer les événements de fin de partie.
 * Implémente NewGameListener pour gérer les événements de nouvelle partie.
 */
public class Controlleur implements GameEndListener, NewGameListener, UndoListener, RedoListener {
    private Jeu jeu;
    private IView vue;
    private JFrame frame;
    private boolean isGraphicalView;

    /**
     * Constructeur qui crée un nouveau Jeu, instancie la Vue, et branche les gestionnaires de clics.
     * Par défaut, utilise la vue graphique.
     */
    public Controlleur() {
        this(true);
    }

    /**
     * Constructeur qui crée un nouveau Jeu, instancie la Vue, et branche les gestionnaires de clics.
     * 
     * @param useGraphicalView true pour utiliser la vue graphique, false pour utiliser la vue console
     */
    public Controlleur(boolean useGraphicalView) {
        // Modèle
        this.jeu = GameFactory.createStandardGame();
        this.isGraphicalView = useGraphicalView;

        // Vue, injectée avec le modèle
        if (useGraphicalView) {
            this.vue = new GraphicalView(jeu);
        } else {
            this.vue = new ConsoleView(jeu);
        }

        // Lier les clics (seulement pour la vue graphique)
        vue.addCaseClickListener(new CaseClickHandler());

        // S'enregistrer comme listener pour les événements
        vue.addGameEndListener(this);
        vue.addNewGameListener(this);
        vue.addUndoListener(this);
        vue.addRedoListener(this);

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

        // Ajouter la barre de menu à la fenêtre (seulement pour la vue graphique)
        if (isGraphicalView) {
            frame.setJMenuBar(((GraphicalView) vue).getMenuBar());
        }
    }

    public IView getVue() {
        return vue;
    }

    /**
     * Gère les événements de fin de partie.
     * 
     * @param event L'événement de fin de partie
     */
    @Override
    public void gameEndRequested(GameEndEvent event) {
        // Demander confirmation (différent selon le type de vue)
        boolean confirmed = false;

        if (isGraphicalView) {
            int response = JOptionPane.showConfirmDialog(
                    (JPanel) vue, 
                    "Voulez-vous vraiment terminer la partie ?", 
                    "Confirmation", 
                    JOptionPane.YES_NO_OPTION);
            confirmed = (response == JOptionPane.YES_OPTION);
        } else {
            System.out.println("Voulez-vous vraiment terminer la partie ? (o/n)");
            // Dans une vraie application, on lirait l'entrée utilisateur ici
            // Pour simplifier, on suppose que l'utilisateur répond oui
            confirmed = true;
        }

        if (confirmed) {
            // Arrêter le timer
            vue.stopTimer();

            // Exécuter la fin de partie en arrière-plan
            ThreadManager.getInstance().executeInBackgroundThenOnEDT(
                // Tâche en arrière-plan
                () -> {
                    // Terminer la partie (match nul)
                    jeu.terminerPartie(null);
                },
                // Tâche sur l'EDT une fois terminée
                () -> {
                    // Afficher un message
                    if (isGraphicalView) {
                        JOptionPane.showMessageDialog(
                                (JPanel) vue, 
                                "La partie a été terminée manuellement.", 
                                "Partie terminée", 
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        System.out.println("La partie a été terminée manuellement.");
                    }
                }
            );
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
            boolean confirmed = false;

            if (isGraphicalView) {
                int response = JOptionPane.showConfirmDialog(
                        (JPanel) vue, 
                        "Voulez-vous vraiment démarrer une nouvelle partie professionnelle ?", 
                        "Confirmation", 
                        JOptionPane.YES_NO_OPTION);
                confirmed = (response == JOptionPane.YES_OPTION);
            } else {
                System.out.println("Voulez-vous vraiment démarrer une nouvelle partie professionnelle ? (o/n)");
                // Dans une vraie application, on lirait l'entrée utilisateur ici
                // Pour simplifier, on suppose que l'utilisateur répond oui
                confirmed = true;
            }

            if (confirmed) {
                // Démarrer une nouvelle partie professionnelle
                startNewProGame();
            }
        } else {
            // Demander confirmation
            boolean confirmed = false;

            if (isGraphicalView) {
                int response = JOptionPane.showConfirmDialog(
                        (JPanel) vue, 
                        "Voulez-vous vraiment démarrer une nouvelle partie standard ?", 
                        "Confirmation", 
                        JOptionPane.YES_NO_OPTION);
                confirmed = (response == JOptionPane.YES_OPTION);
            } else {
                System.out.println("Voulez-vous vraiment démarrer une nouvelle partie standard ? (o/n)");
                // Dans une vraie application, on lirait l'entrée utilisateur ici
                // Pour simplifier, on suppose que l'utilisateur répond oui
                confirmed = true;
            }

            if (confirmed) {
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

        // Exécuter la création de la partie en arrière-plan
        ThreadManager.getInstance().executeInBackgroundThenOnEDT(
            // Tâche en arrière-plan
            () -> {
                // Créer une nouvelle partie standard
                this.jeu = GameFactory.createStandardGame();
            },
            // Tâche sur l'EDT une fois terminée
            () -> {
                // Créer une nouvelle vue du même type que l'actuelle
                if (isGraphicalView) {
                    this.vue = new GraphicalView(jeu);
                } else {
                    this.vue = new ConsoleView(jeu);
                }

                // Lier les clics
                vue.addCaseClickListener(new CaseClickHandler());

                // S'enregistrer comme listener pour les événements
                vue.addGameEndListener(this);
                vue.addNewGameListener(this);
                vue.addUndoListener(this);
                vue.addRedoListener(this);

                // Démarrer le timer
                vue.startTimer();

                // Mettre à jour la fenêtre (seulement pour la vue graphique)
                if (frame != null && isGraphicalView) {
                    frame.setContentPane((JPanel) vue);
                    frame.setJMenuBar(((GraphicalView) vue).getMenuBar());
                    frame.revalidate();
                    frame.repaint();
                }
            }
        );
    }

    /**
     * Démarre une nouvelle partie professionnelle.
     */
    public void startNewProGame() {
        // Arrêter le timer actuel
        vue.stopTimer();

        // Exécuter la création de la partie en arrière-plan
        ThreadManager.getInstance().executeInBackgroundThenOnEDT(
            // Tâche en arrière-plan
            () -> {
                // Créer une nouvelle partie professionnelle
                this.jeu = GameFactory.createProGame();
            },
            // Tâche sur l'EDT une fois terminée
            () -> {
                // Créer une nouvelle vue du même type que l'actuelle
                if (isGraphicalView) {
                    this.vue = new GraphicalView(jeu);
                } else {
                    this.vue = new ConsoleView(jeu);
                }

                // Lier les clics
                vue.addCaseClickListener(new CaseClickHandler());

                // S'enregistrer comme listener pour les événements
                vue.addGameEndListener(this);
                vue.addNewGameListener(this);
                vue.addUndoListener(this);
                vue.addRedoListener(this);

                // Démarrer le timer
                vue.startTimer();

                // Mettre à jour la fenêtre (seulement pour la vue graphique)
                if (frame != null && isGraphicalView) {
                    frame.setContentPane((JPanel) vue);
                    frame.setJMenuBar(((GraphicalView) vue).getMenuBar());
                    frame.revalidate();
                    frame.repaint();
                }
            }
        );
    }

    /**
     * Gère les événements d'annulation de coup.
     * 
     * @param event L'événement d'annulation
     */
    @Override
    public void undoRequested(UndoEvent event) {
        // Utiliser un tableau pour stocker le résultat (pour pouvoir le modifier dans la lambda)
        final boolean[] resultat = new boolean[1];

        // Exécuter l'annulation en arrière-plan
        ThreadManager.getInstance().executeInBackgroundThenOnEDT(
            // Tâche en arrière-plan
            () -> {
                // Annuler le dernier coup et stocker le résultat
                resultat[0] = jeu.annulerDernierCoup();
            },
            // Tâche sur l'EDT une fois terminée
            () -> {
                if (resultat[0]) {
                    // Changer le joueur actif du timer
                    vue.switchTimerPlayer();

                    // Afficher un message
                    System.out.println("Coup annulé");
                } else {
                    // Afficher un message d'erreur
                    if (isGraphicalView) {
                        JOptionPane.showMessageDialog(
                                (JPanel) vue, 
                                "Impossible d'annuler : aucun coup à annuler.", 
                                "Annulation impossible", 
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        System.out.println("Impossible d'annuler : aucun coup à annuler.");
                    }
                }
            }
        );
    }

    /**
     * Gère les événements de rétablissement de coup.
     * 
     * @param event L'événement de rétablissement
     */
    @Override
    public void redoRequested(RedoEvent event) {
        // Utiliser un tableau pour stocker le résultat (pour pouvoir le modifier dans la lambda)
        final boolean[] resultat = new boolean[1];

        // Exécuter le rétablissement en arrière-plan
        ThreadManager.getInstance().executeInBackgroundThenOnEDT(
            // Tâche en arrière-plan
            () -> {
                // Refaire le dernier coup annulé et stocker le résultat
                resultat[0] = jeu.refaireDernierCoup();
            },
            // Tâche sur l'EDT une fois terminée
            () -> {
                if (resultat[0]) {
                    // Changer le joueur actif du timer
                    vue.switchTimerPlayer();

                    // Afficher un message
                    System.out.println("Coup rétabli");
                } else {
                    // Afficher un message d'erreur
                    if (isGraphicalView) {
                        JOptionPane.showMessageDialog(
                                (JPanel) vue, 
                                "Impossible de rétablir : aucun coup à rétablir.", 
                                "Rétablissement impossible", 
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        System.out.println("Impossible de rétablir : aucun coup à rétablir.");
                    }
                }
            }
        );
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

            // Afficher le message (différent selon le type de vue)
            if (isGraphicalView) {
                JOptionPane.showMessageDialog((JPanel) vue, message, "Fin de partie", 
                                             JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.out.println("\n" + message);
            }
        }
    }
}
