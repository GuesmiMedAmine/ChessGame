package modele.deco;

import modele.pieces.Cavalier;
import modele.plateau.Case;

import java.util.ArrayList;
import java.util.List;

public class DecoCavalier extends Deco {
    public DecoCavalier(Cavalier wrapped) {
        super(wrapped);
    }

    @Override
    public List<Case> getCasesAccessibles() {
        List<Case> cases = new ArrayList<>();
        int x = getX(), y = getY();
        int[][] moves = {
                { 2,  1}, { 1,  2},
                {-1,  2}, {-2,  1},
                {-2, -1}, {-1, -2},
                { 1, -2}, { 2, -1}
        };

        for (int[] m : moves) {
            Case c = getPlateau().getCase(x + m[0], y + m[1]);
            if (c != null && (c.getPiece() == null || c.getPiece().getColor() != getColor())) {
                cases.add(c);
            }
        }
        return cases;
    }
}

