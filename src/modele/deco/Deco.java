package modele.deco;

import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.pieces.Piece;
import modele.pieces.PieceColor;
import modele.pieces.PieceType;
import modele.plateau.Direction;

import java.util.ArrayList;
import java.util.List;

/**
 * Décorateur générique pour Piece :
 * délégation de l'API de Piece au wrapped,
 * implémente le glissement pour les directions fournies.
 */
public abstract class Deco extends Piece {
    protected final Piece wrapped;

    public Deco(Piece wrapped) {
        super(wrapped.getX(), wrapped.getY(), wrapped.getColor(), wrapped.getPlateau(), wrapped.getType());
        this.wrapped = wrapped;
    }

    @Override public int getX()               { return wrapped.getX(); }
    @Override public int getY()               { return wrapped.getY(); }
    @Override public PieceColor getColor()    { return wrapped.getColor(); }
    @Override public String getImagePath()    { return wrapped.getImagePath(); }
    @Override public void setPosition(int x,int y) { wrapped.setPosition(x,y); }
    @Override public Plateau getPlateau()     { return wrapped.getPlateau(); }

    @Override protected void initDecorateur() {}
    @Override protected void setImagePath() {}

    /**
     * Utility: slide in given directions until blocked.
     */
    protected List<Case> slideInDirections(int x, int y, List<Direction> dirs) {
        List<Case> result = new ArrayList<>();
        for (Direction dir : dirs) {
            int step = 1;
            while (true) {
                Case c = getPlateau().getCaseRelative(getPlateau().getCase(x, y), dir.dx * step, dir.dy * step);
                if (c == null) break;
                if (c.getPiece() == null) {
                    result.add(c);
                } else {
                    if (c.getPiece().getColor() != getColor()) {
                        result.add(c);
                    }
                    break;
                }
                step++;
            }
        }
        return result;
    }

    @Override public abstract List<Case> getCasesAccessibles();
}