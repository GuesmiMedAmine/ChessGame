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
 * - ne stocke que le wrapped,
 * - override tous les getters pour déléguer,
 * - implémente slideInDirections()
 */
public abstract class Deco extends Piece {
    protected final Piece wrapped;

    public Deco(Piece wrapped) {
        // on passe des valeurs factices au super, mais **on ne s'en sert jamais**
        super(wrapped.getX(), wrapped.getY(), wrapped.getColor(), wrapped.getPlateau(), wrapped.getType());
        this.wrapped = wrapped;
    }

    // On délègue **tout** à wrapped :
    @Override public int getX()               { return wrapped.getX(); }
    @Override public int getY()               { return wrapped.getY(); }
    @Override public PieceColor getColor()    { return wrapped.getColor(); }
    @Override public PieceType getType()      { return wrapped.getType(); }
    @Override public String getImagePath()    { return wrapped.getImagePath(); }
    @Override public Plateau getPlateau()     { return wrapped.getPlateau(); }
    @Override public void setPosition(int x,int y) { wrapped.setPosition(x,y); }

    /** On vire complètement initDecorateur() et setImagePath() hérités */
    @Override protected final void initDecorateur() { /* plus rien */ }

    /** Glissement générique */
    protected List<Case> slideInDirections(int x, int y, List<Direction> dirs) {
        List<Case> result = new ArrayList<>();
        for (Direction dir : dirs) {
            int step = 1;
            while (true) {
                Case c = getPlateau().getCaseRelative(
                        getPlateau().getCase(x, y),
                        dir.dx * step, dir.dy * step
                );
                if (c == null) break;
                if (c.getPiece() == null || c.getPiece().getColor() != getColor()) {
                    result.add(c);
                }
                if (c.getPiece() != null) break;
                step++;
            }
        }
        return result;
    }

    /** Chaque décorateur spécifique implémente ses accès */
    @Override public abstract List<Case> getCasesAccessibles();
}
