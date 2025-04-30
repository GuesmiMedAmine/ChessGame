package modele.jeu;


import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.pieces.*;

public class MoveValidator {
    public static boolean isValid(Piece p, Case tgt, Plateau plat) {
        // Vérifications spéciales avant la validation standard
        if (p instanceof Roi && Math.abs(tgt.getX() - p.getX()) == 2) {
            return validerRoque((Roi)p, tgt, plat);
        }

        if (p instanceof Pion && isPriseEnPassantCase((Pion) p, tgt, plat)) {
            return validerPriseEnPassant((Pion)p, tgt, plat);
        }

        // Validation standard
        return p.getCasesAccessibles().contains(tgt)
                && !simulerEchec(p, tgt, plat);
    }

    static boolean validerRoque(Roi roi, Case tgt, Plateau plat) {
        // 1. Vérifier que le roi n'a pas bougé
        if (roi.hasMoved()) return false;

        // 2. Déterminer la direction
        int direction = tgt.getX() > roi.getX() ? 1 : -1;
        int rookX = direction == 1 ? 7 : 0;

        // 3. Vérifier la tour
        Case rookCase = plat.getCase(rookX, roi.getY());
        if (!(rookCase.getPiece() instanceof Tour) || ((Tour)rookCase.getPiece()).hasMoved()) {
            return false;
        }

        // 4. Vérifier les cases intermédiaires
        int start = Math.min(roi.getX(), rookX) + 1;
        int end = Math.max(roi.getX(), rookX);
        for (int x = start; x < end; x++) {
            if (plat.getCase(x, roi.getY()).getPiece() != null) return false;
        }

        // 5. Vérifier l'absence d'échec
        return !plat.estEnEchec(roi.getColor());
    }

    static boolean validerPriseEnPassant(Pion pion, Case tgt, Plateau plat) {
        // 1. Vérifier le mouvement diagonal
        int deltaX = Math.abs(tgt.getX() - pion.getX());
        int deltaY = tgt.getY() - pion.getY();
        int direction = (pion.getColor() == PieceColor.WHITE) ? 1 : -1;

        if (deltaX != 1 || deltaY != direction) return false;

        // 2. Vérifier le pion adverse
        Case caseAdjacente = plat.getCase(tgt.getX(), pion.getY());
        Piece pionAdverse = caseAdjacente.getPiece();

        return pionAdverse instanceof Pion
                && ((Pion)pionAdverse).isPriseEnPassantPossible();
    }

    private static boolean isPriseEnPassantCase(Pion pion, Case tgt, Plateau plat) {
        return tgt.getPiece() == null
                && Math.abs(tgt.getX() - pion.getX()) == 1
                && tgt.getY() == pion.getY() + (pion.getColor() == PieceColor.WHITE ? 1 : -1);
    }

    private static boolean simulerEchec(Piece p, Case tgt, Plateau plat) {
        // Simulation du mouvement pour vérifier l'échec
        Piece pieceCapturee = tgt.getPiece();
        p.setPosition(tgt.getX(), tgt.getY());
        tgt.setPiece(p);

        boolean enEchec = plat.estEnEchec(p.getColor());

        // Annulation de la simulation
        p.setPosition(tgt.getX(), tgt.getY());
        tgt.setPiece(pieceCapturee);

        return enEchec;
    }
}