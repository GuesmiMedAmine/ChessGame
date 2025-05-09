package modele.deco;

import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.pieces.Fou;
import java.util.ArrayList;
import java.util.List;

public class DecoFou extends Deco {
    private final Fou fou;
    private final Plateau plateau;

    public DecoFou(Fou fou, Plateau plateau) {
        this.fou = fou;
        this.plateau = plateau;
    }

    @Override
    public List<Case> getCasesAccessibles() {
        List<Case> result = new ArrayList<>();
        Case origine = plateau.getCase(fou.getX(), fou.getY());
        int[][] dirs = {{1,1},{1,-1},{-1,1},{-1,-1}};

        for (int[] d : dirs) {
            int step = 1;
            Case c;
            while ((c = plateau.getCaseRelative(origine, d[0]*step, d[1]*step)) != null) {
                if (c.getPiece() == null) {
                    result.add(c);
                } else {
                    if (c.getPiece().getColor() != fou.getColor()) {
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
