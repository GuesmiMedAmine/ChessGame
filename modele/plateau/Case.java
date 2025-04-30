package modele.plateau;

import modele.pieces.Piece;

import java.util.Objects;

public class Case {
    private final int x, y;
    private Piece piece;

    public Case(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Case other = (Case) o;
        return getX() == other.getX() && getY() == other.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }


    public int getX() { return x; }
    public int getY() { return y; }
    public Piece getPiece() { return piece; }
    public void setPiece(Piece piece) { this.piece = piece; }
}