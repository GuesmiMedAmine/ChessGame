package modele.jeu;

public class Roi extends Piece {
    public Roi(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean mouvementValide(int newX, int newY) {
        return Math.abs(newX - x) <= 1 && Math.abs(newY - y) <= 1;
    }
}
