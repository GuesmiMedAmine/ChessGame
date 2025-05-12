package modele.deco;

import modele.pieces.Pion;
import modele.pieces.Piece;
import modele.pieces.PieceColor;
import modele.plateau.Case;

import java.util.ArrayList;
import java.util.List;

public class DecoPion extends Deco {
    public DecoPion(Pion wrapped) {
        super(wrapped);
    }

    /**
     * Vérifie si un mouvement est une prise en passant valide
     * @param pion Le pion qui tente de prendre en passant
     * @param tgt La case cible
     * @return true si le mouvement est une prise en passant valide, false sinon
     */
    public boolean validerPriseEnPassant(Pion pion, Case tgt) {
        // Vérifier que la case cible est vide
        if (tgt.getPiece() != null) {
            return false;
        }

        // Vérifier le mouvement diagonal
        int deltaX = Math.abs(tgt.getX() - pion.getX());
        int deltaY = tgt.getY() - pion.getY();
        int direction = (pion.getColor() == PieceColor.WHITE) ? 1 : -1;

        if (deltaX != 1 || deltaY != direction) {
            return false;
        }

        // Vérifier le pion adverse
        Case caseAdjacente = getPlateau().getCase(tgt.getX(), pion.getY());
        Piece pieceAdjacente = caseAdjacente.getPiece();

        return pieceAdjacente != null
                && pieceAdjacente instanceof Pion
                && pieceAdjacente.getColor() != pion.getColor()
                && ((Pion) pieceAdjacente).isPriseEnPassantPossible();
    }

    /**
     * Vérifie si une case est une case de prise en passant possible
     * @param pion Le pion qui tente de prendre en passant
     * @param tgt La case cible
     * @return true si la case est une case de prise en passant possible, false sinon
     */
    public boolean isPriseEnPassantCase(Pion pion, Case tgt) {
        return tgt.getPiece() == null
                && Math.abs(tgt.getX() - pion.getX()) == 1
                && tgt.getY() == pion.getY() + (pion.getColor() == PieceColor.WHITE ? 1 : -1);
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
