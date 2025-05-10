package modele.pieces;

import modele.deco.DecoPion;
import modele.plateau.Plateau;

public class Pion extends Piece {
    private boolean priseEnPassantPossible = false;

    public Pion(int x, int y, PieceColor color, Plateau plateau) {
        super(x, y, color, plateau, PieceType.PION);
    }

    @Override
    protected void initDecorateur() {
        this.decorateur = new DecoPion(this);
    }


    public boolean isPriseEnPassantPossible() {
        return priseEnPassantPossible;
    }

    public void setPriseEnPassantPossible(boolean value) {
        this.priseEnPassantPossible = value;
    }

}
