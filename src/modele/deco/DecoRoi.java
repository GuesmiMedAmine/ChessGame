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

    /**
     * Calcule les cases accessibles pour le roque (côté roi ou côté reine)
     * @param roi Le roi qui tente de roquer
     * @param coteRoi true pour le roque côté roi, false pour le roque côté reine
     * @return La case de destination du roi si le roque est possible, null sinon
     */
    private Case calculRoque(Roi roi, boolean coteRoi) {
        int y = roi.getY();
        int rookX = coteRoi ? 7 : 0;
        int kingDestX = coteRoi ? 6 : 2;
        int[] casesAVerifier = coteRoi ? new int[]{5, 6} : new int[]{1, 2, 3};

        // Vérifier la tour
        Case rookCase = getPlateau().getCase(rookX, y);
        if (!(rookCase.getPiece() instanceof Tour) || ((Tour) rookCase.getPiece()).hasMoved()) {
            return null;
        }

        // Vérifier que les cases intermédiaires sont vides
        for (int x : casesAVerifier) {
            if (getPlateau().getCase(x, y).getPiece() != null) {
                return null;
            }
        }

        // Vérifier que le roi ne traverse pas une case en échec
        boolean casesEnEchec = false;
        for (int x : casesAVerifier) {
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

        return casesEnEchec ? null : getPlateau().getCase(kingDestX, y);
    }

    /**
     * Filtre les mouvements qui mettraient le roi en échec
     * @param coups Liste des coups possibles
     * @param roi Le roi qui se déplace
     * @return Liste des coups qui ne mettent pas le roi en échec
     */
    private List<Case> filtrerEchec(List<Case> coups, Roi roi) {
        List<Case> mouvementsValides = new ArrayList<>();
        for (Case destination : coups) {
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

    /**
     * Vérifie si un mouvement est un roque valide
     * @param roi Le roi qui tente de roquer
     * @param tgt La case cible
     * @return true si le mouvement est un roque valide, false sinon
     */
    public boolean validerRoque(Roi roi, Case tgt) {
        // Vérifier que le roi n'a pas bougé et n'est pas en échec
        if (roi.hasMoved() || getPlateau().estEnEchec(roi.getColor(), false)) {
            return false;
        }

        // Vérifier que le mouvement est horizontal de 2 cases
        if (Math.abs(tgt.getX() - roi.getX()) != 2 || tgt.getY() != roi.getY()) {
            return false;
        }

        // Déterminer si c'est un roque côté roi ou côté reine
        boolean coteRoi = tgt.getX() > roi.getX();

        // Calculer la case de destination du roque
        Case caseRoque = calculRoque(roi, coteRoi);

        // Vérifier que la case calculée correspond à la case cible
        return caseRoque != null && caseRoque.equals(tgt);
    }

    @Override
    public List<Case> getCasesAccessibles() {
        List<Case> result = new ArrayList<>();
        // mouvements d'une case (le roi ne peut se déplacer que d'une case à la fois)
        result.addAll(getAdjacentSquares(getX(), getY()));

        // roque
        Roi roi = (Roi) wrapped;
        if (!roi.hasMoved() && !getPlateau().estEnEchec(getColor(), false)) {
            // Calculer les options de roque
            Case roqueRoi = calculRoque(roi, true);
            if (roqueRoi != null) {
                result.add(roqueRoi);
            }

            Case roqueReine = calculRoque(roi, false);
            if (roqueReine != null) {
                result.add(roqueReine);
            }
        }

        // Filtrer les mouvements qui mettraient le roi en échec
        return filtrerEchec(result, roi);
    }
}
