package modele.deco;

import modele.pieces.Roi;
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

        for (int[] dir : directions) {
            Case c = plateau.getCaseRelative(
                    plateau.getCase(roi.getX(), roi.getY()),
                    dir[0],
                    dir[1]
            );
            if (c != null && (c.getPiece() == null || c.getPiece().getColor() != roi.getColor())) {
                cases.add(c);
            }
        }
        return cases;
    }
}