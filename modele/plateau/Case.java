package modele.plateau;

import modele.pieces.Piece;

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
        Case aCase = (Case) o;
        return getX() == aCase.getX() && getY() == aCase.getY();
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public Piece getPiece() { return piece; }
    public void setPiece(Piece piece) { this.piece = piece; }
}