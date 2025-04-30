package modele.pieces;

import modele.deco.DecoRoi;
import modele.plateau.Plateau;

public class Roi extends Piece {
    private boolean hasMoved = false;
    public Roi(int x, int y, PieceColor color, Plateau plateau) {
        super(x, y, color, plateau, PieceType.ROI); // Passage du type au constructeur parent
    }
    @Override
    protected void initDecorateur(Plateau plateau) {
        this.decorateur = new DecoRoi(this, plateau);
    }

    @Override
    protected void setImagePath() {
        this.imagePath = "/images/" + (color == PieceColor.WHITE ? "w" : "b") + "K.png";
    }

    @Override
    public PieceType getType() { return PieceType.ROI; }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        this.hasMoved = true;
    }
    @Override
    protected void initDecorateur() {
        this.decorateur = new DecoRoi(this, plateau); // <-- Initialisation du décorateur
    }

    public boolean hasMoved() {
        return hasMoved;
    }
}
