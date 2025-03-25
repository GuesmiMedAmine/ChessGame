package modele.jeu;

public abstract class Piece {
    protected int x, y;

    public Piece(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public abstract boolean mouvementValide(int newX, int newY);
}
