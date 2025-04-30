package modele.pieces;

import modele.deco.DecoTour;
import modele.plateau.Plateau;

public class Tour extends Piece {
    private boolean hasMoved = false;

    public Tour(int x, int y, PieceColor color, Plateau plateau) {
        super(x, y, color, plateau, PieceType.TOUR); // Initialisation du type
    }
    @Override
    protected void initDecorateur(Plateau plateau) {
        this.decorateur = new DecoTour(this, plateau);
    }

    @Override
    protected void setImagePath() {
        this.imagePath = "/images/" + (color == PieceColor.WHITE ? "w" : "b") + "R.png";
    }



    @Override
    public PieceType getType() { return PieceType.TOUR; }

    @Override
    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        this.hasMoved = true;
    }

    public boolean hasMoved() {
        return hasMoved;
    }
    @Override
    protected void initDecorateur() {
        this.decorateur = new DecoTour(this, plateau);
    }
}

