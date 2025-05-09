package modele.deco;

import modele.pieces.Pion;
import modele.pieces.PieceColor;
import modele.plateau.Case;

import java.util.ArrayList;
import java.util.List;

public class DecoPion extends Deco {
    public DecoPion(Pion wrapped) {
        super(wrapped);
    }

    @Override
    public List<Case> getCasesAccessibles() {
        List<Case> result = new ArrayList<>();
        int x = getX(), y = getY();
        int dir = (getColor() == PieceColor.WHITE) ? +1 : -1;

        // avancer
        Case one = getPlateau().getCase(x, y + dir);
        if (one != null && one.getPiece() == null) {
            result.add(one);
            boolean home = (getColor() == PieceColor.WHITE && y == 1)
                    || (getColor() == PieceColor.BLACK && y == 6);
            if (home) {
                Case two = getPlateau().getCase(x, y + 2 * dir);
                if (two != null && two.getPiece() == null) result.add(two);
            }
        }

        // captures
        for (int dx : new int[]{ -1, 1 }) {
            Case diag = getPlateau().getCase(x + dx, y + dir);
            if (diag != null
                    && diag.getPiece() != null
                    && diag.getPiece().getColor() != getColor()) {
                result.add(diag);
            }
        }

        // en passant
        for (int dx : new int[]{ -1, 1 }) {
            Case adj = getPlateau().getCase(x + dx, y);
            if (adj != null
                    && adj.getPiece() instanceof Pion
                    && ((Pion) adj.getPiece()).isPriseEnPassantPossible()) {
                Case ep = getPlateau().getCase(x + dx, y + dir);
                if (ep != null) result.add(ep);
            }
        }

        return result;
    }
}
