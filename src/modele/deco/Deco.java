package modele.deco;

import modele.plateau.Case;
import modele.plateau.Direction;
import modele.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

/**
 * Décorateur générique pour Piece :
 * - étend AbstractDecorator,
 * - implémente slideInDirections()
 */
public abstract class Deco extends AbstractDecorator {

    public Deco(Piece wrapped) {
        super(wrapped);
    }

    /** Glissement générique */
    protected List<Case> slideInDirections(int x, int y, List<Direction> dirs) {
        List<Case> result = new ArrayList<>();
        for (Direction dir : dirs) {
            int step = 1;
            while (true) {
                Case c = getPlateau().getCaseRelative(
                        getPlateau().getCase(x, y),
                        dir.dx * step, dir.dy * step
                );
                if (c == null) break;
                if (c.getPiece() == null || c.getPiece().getColor() != getColor()) {
                    result.add(c);
                }
                if (c.getPiece() != null) break;
                step++;
            }
        }
        return result;
    }

    /** Chaque décorateur spécifique implémente ses accès */
    @Override public abstract List<Case> getCasesAccessibles();
}
