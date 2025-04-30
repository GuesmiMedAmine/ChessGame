package modele.deco;

import modele.pieces.Dame;
import modele.plateau.Case;
import modele.plateau.Plateau;
import java.util.ArrayList;
import java.util.List;

public class DecoDame extends Deco {
    private final Dame dame;
    private final Plateau plateau;

    public DecoDame(Dame dame, Plateau plateau) {
        this.dame = dame;
        this.plateau = plateau;
    }

    @Override
    public List<Case> getCasesAccessibles() {
        List<Case> cases = new ArrayList<>();
        int[][] directions = {
                {1,0}, {-1,0}, {0,1}, {0,-1}, // Directions de la tour
                {1,1}, {1,-1}, {-1,1}, {-1,-1} // Directions du fou
        };

        for (int[] dir : directions) {
            int step = 1;
            while (true) {
                Case c = plateau.getCaseRelative(
                        plateau.getCase(dame.getX(), dame.getY()),
                        dir[0] * step,
                        dir[1] * step
                );

                if (c == null) break;

                if (c.getPiece() == null) {
                    cases.add(c);
                } else {
                    if (c.getPiece().getColor() != dame.getColor()) {
                        cases.add(c);
                    }
                    break;
                }
                step++;
            }
        }
        return cases;
    }
}