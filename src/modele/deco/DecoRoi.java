package modele.deco;

import modele.pieces.Piece;
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

    /**
     * Retourne les cases adjacentes (une case dans chaque direction)
     * @param x Position x de départ
     * @param y Position y de départ
     * @return Liste des cases adjacentes accessibles
     */
    private List<Case> getAdjacentSquares(int x, int y) {
        List<Case> result = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            Case c = getPlateau().getCaseRelative(
                    getPlateau().getCase(x, y),
                    dir.dx, dir.dy
            );
            if (c != null && (c.getPiece() == null || c.getPiece().getColor() != getColor())) {
                result.add(c);
            }
        }
        return result;
    }

    @Override
    public List<Case> getCasesAccessibles() {
        List<Case> result = new ArrayList<>();
        // mouvements d'une case (le roi ne peut se déplacer que d'une case à la fois)
        result.addAll(getAdjacentSquares(getX(), getY()));

        // roque
        Roi roi = (Roi) wrapped;
        if (!roi.hasMoved() && !getPlateau().estEnEchec(getColor(), false)) {
            int y = getY();
            // côté roi
            Case tR = getPlateau().getCase(7, y);
            if (tR.getPiece() instanceof Tour && !((Tour) tR.getPiece()).hasMoved()) {
                if (getPlateau().getCase(5, y).getPiece() == null
                        && getPlateau().getCase(6, y).getPiece() == null) {
                    // Vérifier que le roi ne traverse pas une case en échec
                    boolean casesEnEchec = false;
                    for (int x = 5; x <= 6; x++) {
                        // Simuler le roi sur cette case pour vérifier s'il serait en échec
                        Case caseTraversee = getPlateau().getCase(x, y);
                        Case caseOriginale = roi.getCurrentCase();

                        // Déplacer temporairement le roi
                        caseTraversee.setPiece(roi);
                        caseOriginale.setPiece(null);

                        // Vérifier si le roi est en échec
                        if (getPlateau().estEnEchec(getColor(), false)) {
                            casesEnEchec = true;
                        }

                        // Remettre le roi à sa place
                        caseOriginale.setPiece(roi);
                        caseTraversee.setPiece(null);

                        if (casesEnEchec) break;
                    }

                    if (!casesEnEchec) {
                        result.add(getPlateau().getCase(6, y));
                    }
                }
            }
            // côté reine
            Case tQ = getPlateau().getCase(0, y);
            if (tQ.getPiece() instanceof Tour && !((Tour) tQ.getPiece()).hasMoved()) {
                if (getPlateau().getCase(1, y).getPiece() == null
                        && getPlateau().getCase(2, y).getPiece() == null
                        && getPlateau().getCase(3, y).getPiece() == null) {
                    // Vérifier que le roi ne traverse pas une case en échec
                    boolean casesEnEchec = false;
                    for (int x = 1; x <= 3; x++) {
                        // Simuler le roi sur cette case pour vérifier s'il serait en échec
                        Case caseTraversee = getPlateau().getCase(x, y);
                        Case caseOriginale = roi.getCurrentCase();

                        // Déplacer temporairement le roi
                        caseTraversee.setPiece(roi);
                        caseOriginale.setPiece(null);

                        // Vérifier si le roi est en échec
                        if (getPlateau().estEnEchec(getColor(), false)) {
                            casesEnEchec = true;
                        }

                        // Remettre le roi à sa place
                        caseOriginale.setPiece(roi);
                        caseTraversee.setPiece(null);

                        if (casesEnEchec) break;
                    }

                    if (!casesEnEchec) {
                        result.add(getPlateau().getCase(2, y));
                    }
                }
            }
        }

        // Filtrer les mouvements qui mettraient le roi en échec
        List<Case> mouvementsValides = new ArrayList<>();
        for (Case destination : result) {
            Case origine = roi.getCurrentCase();
            Piece pieceCapturee = destination.getPiece();

            // Simuler le mouvement
            destination.setPiece(roi);
            origine.setPiece(null);

            // Vérifier si le roi est en échec après le mouvement
            if (!getPlateau().estEnEchec(getColor(), false)) {
                mouvementsValides.add(destination);
            }

            // Annuler le mouvement
            origine.setPiece(roi);
            destination.setPiece(pieceCapturee);
        }

        return mouvementsValides;
    }
}
