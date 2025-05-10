package modele.pieces;

import modele.deco.Deco;
import modele.plateau.Plateau;
import modele.plateau.Case;
import java.util.List;

public abstract class Piece {
    protected int x;
    protected int y;
    protected final PieceColor color;
    protected final PieceType type;
    protected Deco decorateur;
    protected String imagePath;
    protected Plateau plateau;

    public Piece(int x, int y, PieceColor color, Plateau plateau, PieceType type) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.plateau = plateau;
        this.type = type;
        this.initDecorateur();
        this.setImagePath();
    }

    protected abstract void initDecorateur();

    /**
     * Initialise le chemin vers l'image correspondant au type et à la couleur de la pièce.
     */
    protected void setImagePath() {
        this.imagePath = "/images/" +
                (color == PieceColor.WHITE ? "w" : "b") +
                type.getLetter() +
                ".png";
    }

    public List<Case> getCasesAccessibles() {
        return decorateur.getCasesAccessibles();
    }

    // Accesseurs
    public int getX() { return x; }
    public int getY() { return y; }
    public PieceColor getColor() { return color; }
    public PieceType getType() { return type; }
    public String getImagePath() { return imagePath; }
    public Plateau getPlateau() { return plateau; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
