package modele.deco;

import modele.pieces.Tour;
import modele.plateau.Case;
import modele.plateau.Direction;

import java.util.List;

public class DecoTour extends Deco {
    public DecoTour(Tour wrapped) {
        super(wrapped);
    }

    @Override
    public List<Case> getCasesAccessibles() {
        return slideInDirections(getX(), getY(),
                List.of(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT));
    }
}
