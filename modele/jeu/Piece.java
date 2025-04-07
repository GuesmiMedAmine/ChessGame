package modele.jeu;

public class Piece {
    private PieceType type;
    private PieceColor couleur;

    public Piece(PieceType type, PieceColor couleur) {
        this.type = type;
        this.couleur = couleur;
    }

    public PieceType getType() {
        return type;
    }

    public PieceColor getCouleur() {
        return couleur;
    }
}
