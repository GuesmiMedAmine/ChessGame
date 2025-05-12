package vue;

import modele.jeu.Jeu;
import modele.plateau.Case;
import modele.plateau.Plateau;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Observable;

/**
 * Vue du plateau d'échecs, observe le modèle et gère l'affichage.
 * Étend la classe de base VueBase.
 */
public class VueControleur extends VueBase {
    private final Jeu jeu;

    // Sélection et coups possibles pour le highlight
    private Case selectedCase;
    private List<Case> validMoves;

    public VueControleur(Jeu jeu) {
        this.jeu = jeu;
        Plateau plateau = jeu.getPlateau();
        plateau.addObserver(this);
        setLayout(new GridLayout(8, 8));
        initUI();
        update(plateau, null);
    }

    /**
     * Met à jour l'affichage : pièces, sélection et coups possibles.
     */
    @Override
    public void update(Observable o, Object arg) {
        Plateau p = (Plateau) o;

        // Mettre à jour les pièces en utilisant la méthode de la classe de base
        updatePieces(p);

        // Ajouter la surbrillance pour la sélection et les coups valides
        highlightSelection(p);

        revalidate();
        repaint();
    }

    /**
     * Ajoute la surbrillance pour la case sélectionnée, les coups valides,
     * et les rois en échec.
     */
    private void highlightSelection(Plateau p) {
        // Récupérer les cases des rois
        Case roiBlanc = p.getRoi(modele.pieces.PieceColor.WHITE);
        Case roiNoir = p.getRoi(modele.pieces.PieceColor.BLACK);

        // Vérifier si les rois sont en échec
        boolean roiBlancEnEchec = jeu.estEnEchec(modele.pieces.PieceColor.WHITE);
        boolean roiNoirEnEchec = jeu.estEnEchec(modele.pieces.PieceColor.BLACK);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                JLabel lbl = grid[x][y];
                Case c = p.getCase(x, y);

                // Réinitialiser le fond avec la couleur de base
                lbl.setBackground(getCaseColor(x, y));

                // Surbrillance sélection et coups valides
                if (selectedCase != null && selectedCase.equals(c)) {
                    lbl.setBackground(new Color(255, 255, 0, 150));
                } else if (validMoves != null && validMoves.contains(c)) {
                    lbl.setBackground(new Color(144, 238, 144, 150));
                }

                // Surbrillance rouge pour les rois en échec
                if ((roiBlancEnEchec && c.equals(roiBlanc)) || 
                    (roiNoirEnEchec && c.equals(roiNoir))) {
                    lbl.setBackground(new Color(255, 0, 0, 150));
                }

                lbl.setBorder(null);
            }
        }
    }

    /**
     * Définir la case sélectionnée et les déplacements valides à surligner.
     */
    public void selectCase(Case selected, List<Case> moves) {
        this.selectedCase = selected;
        this.validMoves   = moves;
        highlightSelection(jeu.getPlateau());
        revalidate();
        repaint();
    }


    /**
     * Effacer la sélection et les surbrillances.
     */
    public void clearSelection() {
        this.selectedCase = null;
        if (validMoves != null) validMoves.clear();
        repaint();
    }
}
