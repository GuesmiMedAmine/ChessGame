package modele.jeu.command;

import modele.pieces.Roi;
import modele.pieces.Tour;
import modele.plateau.Case;

/**
 * Commande pour effectuer un roque (petit ou grand).
 * Implémente le pattern Command.
 */
public class RoqueCommand implements Command {
    private final Roi roi;
    private final Tour tour;
    private final Case departRoi;
    private final Case arriveeRoi;
    private final Case departTour;
    private final Case arriveeTour;
    private final boolean petitRoque; // true pour petit roque, false pour grand roque

    /**
     * Constructeur pour un roque
     * @param roi Le roi qui roque
     * @param tour La tour impliquée dans le roque
     * @param arriveeRoi La case d'arrivée du roi
     * @param arriveeTour La case d'arrivée de la tour
     */
    public RoqueCommand(Roi roi, Tour tour, Case arriveeRoi, Case arriveeTour) {
        this.roi = roi;
        this.tour = tour;
        this.departRoi = roi.getCurrentCase();
        this.arriveeRoi = arriveeRoi;
        this.departTour = tour.getCurrentCase();
        this.arriveeTour = arriveeTour;
        this.petitRoque = arriveeRoi.getX() > departRoi.getX();
    }

    /**
     * Constructeur simplifié pour un roque
     * @param roi Le roi qui roque
     * @param petitRoque true pour petit roque, false pour grand roque
     */
    public RoqueCommand(Roi roi, boolean petitRoque) {
        this.roi = roi;
        this.petitRoque = petitRoque;
        this.departRoi = roi.getCurrentCase();

        int y = roi.getY();
        int rookX = petitRoque ? 7 : 0;
        Case rookCase = roi.getPlateau().getCase(rookX, y);
        this.tour = (Tour) rookCase.getPiece();
        this.departTour = rookCase;

        // Positions finales après le roque
        this.arriveeRoi = roi.getPlateau().getCase(petitRoque ? 6 : 2, y);
        this.arriveeTour = roi.getPlateau().getCase(petitRoque ? 5 : 3, y);
    }

    @Override
    public void execute() {
        // Déplacer le roi
        departRoi.setPiece(null);
        arriveeRoi.setPiece(roi);
        roi.setPosition(arriveeRoi.getX(), arriveeRoi.getY());

        // Déplacer la tour
        departTour.setPiece(null);
        arriveeTour.setPiece(tour);
        tour.setPosition(arriveeTour.getX(), arriveeTour.getY());
    }

    @Override
    public void undo() {
        // Remettre le roi à sa position d'origine
        arriveeRoi.setPiece(null);
        departRoi.setPiece(roi);
        roi.setPosition(departRoi.getX(), departRoi.getY());

        // Remettre la tour à sa position d'origine
        arriveeTour.setPiece(null);
        departTour.setPiece(tour);
        tour.setPosition(departTour.getX(), departTour.getY());
    }
}
