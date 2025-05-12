// controlleur/Controlleur.java
package controlleur;

import modele.jeu.Jeu;
import modele.pieces.PieceColor;
import modele.plateau.Case;
import utils.ThreadManager;
import vue.VueControleur;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 * Contrôleur central du jeu.
 * Initialise modèle et vue, et orchestre les clics de la souris.
 */
public class Controlleur {
    private final Jeu jeu;
    private final VueControleur vue;

    /**
     * Constructeur qui crée un nouveau Jeu, instancie la Vue, et branche les gestionnaires de clics.
     */
    public Controlleur() {
        // Modèle
        this.jeu = new Jeu();
        // Vue, injectée avec le modèle
        this.vue = new VueControleur(jeu);
        // Lier les clics
        vue.addCaseClickListener(new CaseClickHandler());
    }

    public VueControleur getVue() {
        return vue;
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

                // Jouer le coup en arrière-plan
                ThreadManager.getInstance().executeInBackgroundThenOnEDT(
                    // Tâche en arrière-plan
                    () -> {
                        // Jouer le coup (pas besoin de retourner la valeur)
                        jeu.jouerCoup(depart, arrivee);
                    },
                    // Tâche sur l'EDT une fois terminée
                    () -> {
                        // Vérifier si la partie est terminée après le coup
                        if (jeu.estPartieTerminee()) {
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
