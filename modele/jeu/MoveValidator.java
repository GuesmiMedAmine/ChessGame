package modele.jeu;

import modele.deco.*;
import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.pieces.*;

public class MoveValidator {
    public static boolean isValid(Piece p, Case tgt, Plateau plat) {
        // Vérification de base
        if (!p.getCasesAccessibles().contains(tgt)) return false;

        // Vérification spéciale pour pion
        if (p instanceof Pion) {
            return validerMouvementPion((Pion)p, tgt, plat);
        }

        // Vérification spéciale pour roque
        if (p instanceof Roi && Math.abs(tgt.getX() - p.getX()) == 2) {
            return validerRoque((Roi)p, tgt, plat);
        }

        return true;
    }

    private static boolean validerMouvementPion(Pion pion, Case tgt, Plateau plat) {
        // Logique de prise en passant
        if (Math.abs(tgt.getX() - pion.getX()) == 1 && tgt.getPiece() == null) {
            return checkEnPassant(pion, tgt, plat);
        }
        return true;
    }

    private static boolean checkEnPassant(Pion pion, Case tgt, Plateau plat) {
        int direction = pion.getColor() == PieceColor.WHITE ? 1 : -1;
        Case caseAdjacente = plat.getCase(tgt.getX(), pion.getY());
        Piece pieceAdjacente = caseAdjacente.getPiece();

        return pieceAdjacente instanceof Pion &&
                ((Pion)pieceAdjacente).isPriseEnPassantPossible();
    }

    private static boolean validerRoque(Roi roi, Case tgt, Plateau plat) {
        // Implémentation simplifiée
        int direction = tgt.getX() > roi.getX() ? 1 : -1;
        Case tourCase = plat.getCase(direction == 1 ? 7 : 0, roi.getY());

        return tourCase.getPiece() instanceof Tour &&
                !((Tour)tourCase.getPiece()).hasMoved() &&
                !roi.hasMoved();
    }
}