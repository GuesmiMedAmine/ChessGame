package modele.pieces;

import modele.deco.DecoFou;
import modele.plateau.Plateau;

public class Fou extends Piece {
    public Fou(int x, int y, PieceColor color, Plateau plateau) {
        super(x, y, color, plateau, PieceType.FOU);
    }

    @Override
    protected void initDecorateur(Plateau plateau) {
        this.decorateur = new DecoFou(this, plateau);
    }

    @Override
    protected void setImagePath() {
        this.imagePath = "/images/" + (color == PieceColor.WHITE ? "w" : "b") + "B.png";
    }
    @Override
    protected void initDecorateur() {
        this.decorateur = new DecoFou(this, plateau);
    }

    @Override
    public PieceType getType() { return PieceType.FOU; }
}