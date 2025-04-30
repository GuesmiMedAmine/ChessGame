package modele.deco;

import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.pieces.Pion;
import modele.pieces.Piece;
import modele.pieces.PieceColor;
import modele.pieces.PieceType;
import java.util.ArrayList;
import java.util.List;

public class DecoPion extends Deco {
    private final Pion pion;
    private final Plateau plateau;

    public DecoPion(Pion pion, Plateau plateau) {
        this.pion = pion;
        this.plateau = plateau;
    }

    @Override
    public List<Case> getCasesAccessibles() {
        List<Case> result = new ArrayList<>();
        Case origine = plateau.getCase(pion.getX(), pion.getY());
        int dir = (pion.getColor() == PieceColor.WHITE) ? 1 : -1;

        // Avancée d'une case
        Case fwd = plateau.getCaseRelative(origine, 0, dir);
        if (fwd != null && fwd.getPiece() == null) {
            result.add(fwd);

            // Avancée de deux cases si premier coup
            boolean start = (pion.getColor() == PieceColor.WHITE && origine.getY() == 1)
                    || (pion.getColor() == PieceColor.BLACK && origine.getY() == 6);
            Case fwd2 = plateau.getCaseRelative(origine, 0, dir * 2);
            if (start && fwd2 != null && fwd2.getPiece() == null) {
                result.add(fwd2);
            }
        }

        // Prises diagonales
        for (int dx : new int[]{-1, 1}) {
            Case diag = plateau.getCaseRelative(origine, dx, dir);
            if (diag != null && diag.getPiece() != null
                    && diag.getPiece().getColor() != pion.getColor()) {
                result.add(diag);
            }
        }

        return result;
    }
}
