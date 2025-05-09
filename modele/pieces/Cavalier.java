package modele.pieces;

import modele.deco.DecoCavalier;
import modele.plateau.Plateau;

public class Cavalier extends Piece {
    public Cavalier(int x, int y, PieceColor color, Plateau plateau) {
        super(x, y, color, plateau, PieceType.CAVALIER);
    }

    @Override
    protected void initDecorateur() {
        this.decorateur = new DecoCavalier(this);
    }

    @Override
    protected void setImagePath() {
        this.imagePath = "/images/" + (color == PieceColor.WHITE ? "w" : "b") + "N.png";
    }

    @Override
    public PieceType getType() {
        return PieceType.CAVALIER;
    }
}
