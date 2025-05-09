package modele.deco;

import modele.pieces.Fou;
import modele.plateau.Case;
import modele.plateau.Plateau.Direction;

import java.util.List;

public class DecoFou extends Deco {
    public DecoFou(Fou wrapped) {
        super(wrapped);
    }

    @Override
    public List<Case> getCasesAccessibles() {
        return slideInDirections(getX(), getY(),
                List.of(Direction.UP_LEFT, Direction.UP_RIGHT,
                        Direction.DOWN_LEFT, Direction.DOWN_RIGHT));
    }
}

