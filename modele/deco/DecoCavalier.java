package modele.deco;

import modele.pieces.Cavalier;
import modele.plateau.Case;
import modele.plateau.Plateau;
import java.util.ArrayList;
import java.util.List;

public class DecoCavalier extends Deco {
    private final Cavalier cavalier;
    private final Plateau plateau;

    public DecoCavalier(Cavalier cavalier, Plateau plateau) {
        this.cavalier = cavalier;
        this.plateau = plateau;
    }

    @Override
    public List<Case> getCasesAccessibles() {
        List<Case> cases = new ArrayList<>();
        int[][] moves = {{2,1}, {1,2}, {-1,2}, {-2,1}, {-2,-1}, {-1,-2}, {1,-2}, {2,-1}};

        for (int[] m : moves) {
            Case c = plateau.getCaseRelative(
                    plateau.getCase(cavalier.getX(), cavalier.getY()),
                    m[0],
                    m[1]
            );
            if (c != null && (c.getPiece() == null || c.getPiece().getColor() != cavalier.getColor())) {
                cases.add(c);
            }
        }
        return cases;
    }
}