package modele.pieces;

import modele.deco.DecoDame;
import modele.plateau.Plateau;

public class Dame extends Piece {
    public Dame(int x, int y, PieceColor color, Plateau plateau) {
        super(x, y, color, plateau, PieceType.DAME);
    }

    @Override
    protected void initDecorateur(Plateau plateau) {
        this.decorateur = new DecoDame(this, plateau);
    }

    @Override
    protected void setImagePath() {
        this.imagePath = "/images/" + (color == PieceColor.WHITE ? "w" : "b") + "Q.png";
    }
    @Override
    protected void initDecorateur() {
        this.decorateur = new DecoDame(this, plateau);
    }


    @Override
    public PieceType getType() { return PieceType.DAME; }
}