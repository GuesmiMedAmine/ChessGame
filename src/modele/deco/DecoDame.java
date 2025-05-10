package modele.deco;

import modele.pieces.Dame;
import modele.plateau.Case;
import modele.plateau.Direction;

import java.util.Arrays;
import java.util.List;

public class DecoDame extends Deco {
    public DecoDame(Dame wrapped) {
        super(wrapped);
    }

    @Override
    public List<Case> getCasesAccessibles() {
        return slideInDirections(getX(), getY(), Arrays.asList(Direction.values()));
    }
}
