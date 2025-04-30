package modele.jeu;

import modele.plateau.Case;
import modele.pieces.Piece;

public class Coup {
    private final Piece piece;
    private final Case depart;
    private final Case arrivee;
    private final Piece pieceCapturee;

    public Coup(Piece piece, Case arrivee) {
        this.piece = piece;
        this.depart = piece.getPlateau().getCase(piece.getX(), piece.getY());
        this.arrivee = arrivee;
        this.pieceCapturee = arrivee.getPiece();
    }

    // Getters
    public Piece getPiece() { return piece; }
    public Case getDepart() { return depart; }
    public Case getArrivee() { return arrivee; }
    public Piece getPieceCapturee() { return pieceCapturee; }
}