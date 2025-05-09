package modele.pieces;

import modele.deco.DecoRoi;
import modele.plateau.Plateau;

public class Roi extends Piece {
    private boolean hasMoved = false;

    public Roi(int x, int y, PieceColor color, Plateau plateau) {
        super(x, y, color, plateau, PieceType.ROI);
    }

    @Override
    protected void initDecorateur() {
        this.decorateur = new DecoRoi(this);
    }

    @Override
    protected void setImagePath() {
        this.imagePath = "/images/" + (color == PieceColor.WHITE ? "w" : "b") + "K.png";
    }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        this.hasMoved = true;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public PieceType getType() {
        return PieceType.ROI;
    }
}
