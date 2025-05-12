package modele.deco;

import modele.pieces.Piece;
import modele.pieces.PieceColor;
import modele.pieces.PieceType;
import modele.plateau.Case;
import modele.plateau.Direction;
import modele.plateau.Plateau;

import java.util.ArrayList;
import java.util.List;

/**
 * Décorateur abstrait pour Piece :
 * - ne stocke que le wrapped,
 * - override tous les getters pour déléguer
 */
public abstract class AbstractDecorator extends Piece {
    protected final Piece wrapped;

    public AbstractDecorator(Piece wrapped) {
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
    @Override protected final void initDecorateur() {}

    /** Chaque décorateur spécifique implémente ses accès */
    @Override public abstract List<Case> getCasesAccessibles();
}