package modele.deco;

import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.pieces.Tour;
import modele.pieces.Piece;
import modele.pieces.PieceColor;
import modele.pieces.PieceType;
import java.util.ArrayList;
import java.util.List;
public class DecoTour extends Deco {
    private final Tour tour;
    private final Plateau plateau;

    public DecoTour(Tour tour, Plateau plateau) {
        this.tour = tour;
        this.plateau = plateau;
    }

    @Override
    public List<Case> getCasesAccessibles() {
        List<Case> result = new ArrayList<>();
        Case origine = plateau.getCase(tour.getX(), tour.getY());
        int[][] dirs = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        for (int[] d : dirs) {
            int step = 1;
            Case c;
            while ((c = plateau.getCaseRelative(origine, d[0]*step, d[1]*step)) != null) {
                if (c.getPiece() == null) {
                    result.add(c);
                } else {
                    if (c.getPiece().getColor() != tour.getColor()) {
                        result.add(c);
                    }
                    break;
                }
                step++;
            }
        }
        return result;
    }
}
