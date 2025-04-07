package modele.jeu;

public class Piece {
    private int x, y;
    private PieceType type;
    private PieceColor color;

    public Piece(int x, int y, PieceType type, PieceColor color) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.color = color;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public PieceType getType() { return type; }
    public PieceColor getColor() { return color; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean mouvementValide(int newX, int newY) {
        int dx = Math.abs(newX - x);
        int dy = Math.abs(newY - y);
        return switch (type) {
            case ROI -> dx <= 1 && dy <= 1;
            case TOUR -> dx == 0 || dy == 0;
            case FOU -> dx == dy;
            case DAME -> dx == dy || dx == 0 || dy == 0;
            case CAVALIER -> dx * dx + dy * dy == 5;
            case PION -> (color == PieceColor.BLANC ? newY - y == 1 : y - newY == 1) && dx == 0;
        };
    }
}
