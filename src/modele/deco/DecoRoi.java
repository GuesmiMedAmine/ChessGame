package modele.deco;

import modele.pieces.Roi;
import modele.pieces.Tour;
import modele.plateau.Case;
import modele.plateau.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DecoRoi extends Deco {
    public DecoRoi(Roi wrapped) {
        super(wrapped);
    }

    @Override
    public List<Case> getCasesAccessibles() {
        List<Case> result = new ArrayList<>();
        // mouvements d'une case
        result.addAll(slideInDirections(getX(), getY(), Arrays.asList(Direction.values())));

        // roque
        Roi roi = (Roi) wrapped;
        if (!roi.hasMoved()) {
            int y = getY();
            // côté roi
            Case tR = getPlateau().getCase(7, y);
            if (tR.getPiece() instanceof Tour && !((Tour) tR.getPiece()).hasMoved()) {
                if (getPlateau().getCase(5, y).getPiece() == null
                        && getPlateau().getCase(6, y).getPiece() == null) {
                    result.add(getPlateau().getCase(6, y));
                }
            }
            // côté reine
            Case tQ = getPlateau().getCase(0, y);
            if (tQ.getPiece() instanceof Tour && !((Tour) tQ.getPiece()).hasMoved()) {
                if (getPlateau().getCase(1, y).getPiece() == null
                        && getPlateau().getCase(2, y).getPiece() == null
                        && getPlateau().getCase(3, y).getPiece() == null) {
                    result.add(getPlateau().getCase(2, y));
                }
            }
        }
        return result;
    }
}
