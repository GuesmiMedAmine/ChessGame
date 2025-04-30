package modele.deco;

import modele.pieces.PieceColor;
import modele.pieces.Roi;
import modele.pieces.Tour;
import modele.plateau.Case;
import modele.plateau.Plateau;
import java.util.ArrayList;
import java.util.List;

public class DecoRoi extends Deco {
    private final Roi roi;
    private final Plateau plateau;

    public DecoRoi(Roi roi, Plateau plateau) {
        this.roi = roi;
        this.plateau = plateau;
    }

    @Override
    public List<Case> getCasesAccessibles() {
        List<Case> cases = new ArrayList<>();
        int[][] directions = {{1,0}, {-1,0}, {0,1}, {0,-1}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};

        return getCases(cases, directions, plateau, roi.getX(), roi.getY(), roi.getColor());
    }

    List<Case> getCases(List<Case> cases, int[][] directions, Plateau plateau, int x, int y, PieceColor color) {
        for (int[] dir : directions) {
            Case c = plateau.getCaseRelative(
                    plateau.getCase(x, y),
                    dir[0],
                    dir[1]
            );
            if (c != null && (c.getPiece() == null || c.getPiece().getColor() != color)) {
                cases.add(c);
            }
        }

        // Ajout du roque si possible
        if (!roi.hasMoved()) {
            // Roque côté roi (droit)
            Case tourDroite = plateau.getCase(7, roi.getY());
            if (tourDroite.getPiece() instanceof Tour && !((Tour)tourDroite.getPiece()).hasMoved()) {
                if (plateau.getCase(5, roi.getY()).getPiece() == null
                        && plateau.getCase(6, roi.getY()).getPiece() == null) {
                    cases.add(plateau.getCase(6, roi.getY()));
                }
            }

            // Roque côté reine (gauche)
            Case tourGauche = plateau.getCase(0, roi.getY());
            if (tourGauche.getPiece() instanceof Tour && !((Tour)tourGauche.getPiece()).hasMoved()) {
                if (plateau.getCase(1, roi.getY()).getPiece() == null
                        && plateau.getCase(2, roi.getY()).getPiece() == null
                        && plateau.getCase(3, roi.getY()).getPiece() == null) {
                    cases.add(plateau.getCase(2, roi.getY()));
                }
            }
        }


        return cases;
    }
}