package modele.plateau;
/**
 * Les 8 directions possibles, avec leur vecteur (dx, dy).
 */
public enum Direction {
    UP(-1, 0), DOWN(1, 0),
    LEFT(0, -1), RIGHT(0, 1),
    UP_LEFT(-1, -1), UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1), DOWN_RIGHT(1, 1);

    public final int dx;
    public final int dy;
    Direction(int dx, int dy) { this.dx = dx; this.dy = dy; }
    public int dx() { return dx; }
    public int dy() { return dy; }
}