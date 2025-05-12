package modele.jeu;

import modele.plateau.Case;
import modele.pieces.Piece;


public class Coup {
    private final Piece piece;
    private final Case depart;
    private final Case arrivee;
    private final Piece pieceCapturee;
    private final boolean roque;
    private final boolean priseEnPassant;

    public Coup(Piece piece, Case arrivee, boolean roque, boolean priseEnPassant) {
        this.piece = piece;
        this.depart = piece.getCurrentCase();
        this.arrivee = arrivee;
        this.pieceCapturee = arrivee.getPiece();
        this.roque = roque;
        this.priseEnPassant = priseEnPassant;
    }

    // Getters
    public Piece getPiece() { return piece; }
    public Case getDepart() { return depart; }
    public Case getArrivee() { return arrivee; }
    public Piece getPieceCapturee() { return pieceCapturee; }
    @Override
    public String toString() {
        String capture = (pieceCapturee != null) ? "x" : "";
        String special = "";
        if (roque) special = " (Roque)";
        if (priseEnPassant) special = " (Prise en passant)";

        return String.format("%s %c%d → %c%d%s%s",
                piece.getType(),
                (char)('a' + depart.getX()), depart.getY() + 1,
                (char)('a' + arrivee.getX()), arrivee.getY() + 1,
                capture, special);
    }
}
