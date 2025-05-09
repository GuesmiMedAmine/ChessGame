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
        int[][] moves = {
                {2, 1}, {1, 2},
                {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2},
                {1, -2}, {2, -1}
        };

        // Ajout des cases valides
        for (int[] move : moves) {
            int newX = cavalier.getX() + move[0];
            int newY = cavalier.getY() + move[1];

            if (newX >= 0 && newX < 8 && newY >= 0 && newY < 8) {
                Case c = plateau.getCase(newX, newY);
                if (c != null && (c.getPiece() == null || c.getPiece().getColor() != cavalier.getColor())) {
                    cases.add(c);
                }
            }
        }

        return cases;
    }
}