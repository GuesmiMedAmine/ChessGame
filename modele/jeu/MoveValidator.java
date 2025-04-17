package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;

public class MoveValidator {

    public static boolean isValidMove(Plateau plateau, Case depart, Case arrivee, PieceColor joueur) {
        // Clone du plateau pour simuler le coup
        Piece piece = depart.getPiece();
        Piece ancienneCible = arrivee.getPiece();

        arrivee.setPiece(piece);
        depart.setPiece(null);

        boolean roiEnEchec = plateau.roiEnEchec(joueur);

        // On restaure le plateau
        depart.setPiece(piece);
        arrivee.setPiece(ancienneCible);

        return !roiEnEchec;
    }

    public static boolean isEnPassant(Case depart, Case arrivee, Plateau plateau) {
        Piece piece = depart.getPiece();
        if (piece.getType() != PieceType.PION) return false;

        int dx = arrivee.getX() - depart.getX();
        int dy = arrivee.getY() - depart.getY();

        // Doit être une diagonale
        if (Math.abs(dx) != 1 || Math.abs(dy) != 1) return false;

        Case caseCible = plateau.getCase(arrivee.getX(), depart.getY());
        Piece pieceCible = caseCible.getPiece();

        if (pieceCible != null && pieceCible.getType() == PieceType.PION && pieceCible.getColor() != piece.getColor()) {
            // TODO : Vérifier que ce pion a bougé de 2 cases au tour précédent
            return true; // Hypothèse simple
        }

        return false;
    }

    public static Case getEnPassantCaptureCase(Case depart, Case arrivee) {
        return new Case(arrivee.getX(), depart.getY());
    }

    public static boolean isCastling(Case depart, Case arrivee) {
        Piece piece = depart.getPiece();
        if (piece == null || piece.getType() != PieceType.ROI) return false;

        int dx = arrivee.getX() - depart.getX();
        return depart.getY() == arrivee.getY() && Math.abs(dx) == 2;
    }

    public static void executeCastling(Plateau plateau, Case depart, Case arrivee) {
        int y = depart.getY();
        if (arrivee.getX() == 6) { // petit roque
            Piece tour = plateau.getCase(7, y).getPiece();
            plateau.getCase(5, y).setPiece(tour);
            plateau.getCase(7, y).setPiece(null);
            tour.setPosition(5, y);
        } else if (arrivee.getX() == 2) { // grand roque
            Piece tour = plateau.getCase(0, y).getPiece();
            plateau.getCase(3, y).setPiece(tour);
            plateau.getCase(0, y).setPiece(null);
            tour.setPosition(3, y);
        }
    }

    public static boolean isPromotion(Piece piece, Case arrivee) {
        return piece.getType() == PieceType.PION && (arrivee.getY() == 0 || arrivee.getY() == 7);
    }

    public static Piece executePromotion(Piece piece, Case arrivee) {
        return new Piece(arrivee.getX(), arrivee.getY(), PieceType.DAME, piece.getColor());
    }
}