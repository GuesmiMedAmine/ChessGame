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

    public Plateau getPlateau() {
        return plateau;
    }

    public Piece(int x, int y, PieceColor color, Plateau plateau, PieceType type) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.plateau = plateau;
        this.type = type;
        this.initDecorateur();
        setImagePath();
    }


    protected abstract void initDecorateur(); // À implémenter dans les sous-classes

    protected abstract void initDecorateur(Plateau plateau);

    protected abstract void setImagePath();

    public List<Case> getCasesAccessibles() {
        return decorateur.getCasesAccessibles();
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public PieceColor getColor() { return color; }
    public PieceType getType() { return type; }
    public String getImagePath() { return imagePath; }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

}