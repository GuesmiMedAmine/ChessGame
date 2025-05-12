package modele.jeu.command;

import modele.pieces.Pion;
import modele.pieces.Piece;
import modele.plateau.Case;

/**
 * Commande pour effectuer une prise en passant.
 * Implémente le pattern Command.
 */
public class PriseEnPassantCommand implements Command {
    private final Pion pion;
    private final Case depart;
    private final Case arrivee;
    private final Pion pionCapture;
    private final Case casePionCapture;

    /**
     * Constructeur pour une prise en passant
     * @param pion Le pion qui effectue la prise en passant
     * @param arrivee La case d'arrivée du pion
     */
    public PriseEnPassantCommand(Pion pion, Case arrivee) {
        this.pion = pion;
        this.depart = pion.getCurrentCase();
        this.arrivee = arrivee;

        // Le pion capturé est sur la même colonne que la case d'arrivée, mais sur la même ligne que le pion qui capture
        this.casePionCapture = pion.getPlateau().getCase(arrivee.getX(), pion.getY());
        this.pionCapture = (Pion) casePionCapture.getPiece();
    }

    @Override
    public void execute() {
        // Déplacer le pion
        depart.setPiece(null);
        arrivee.setPiece(pion);
        pion.setPosition(arrivee.getX(), arrivee.getY());

        // Capturer le pion adverse
        casePionCapture.setPiece(null);
    }

    @Override
    public void undo() {
        // Remettre le pion à sa position d'origine
        arrivee.setPiece(null);
        depart.setPiece(pion);
        pion.setPosition(depart.getX(), depart.getY());

        // Remettre le pion capturé
        casePionCapture.setPiece(pionCapture);
    }
}
