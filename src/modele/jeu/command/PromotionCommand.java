package modele.jeu.command;

import modele.pieces.*;
import modele.plateau.Case;

/**
 * Commande pour effectuer une promotion de pion.
 * Implémente le pattern Command.
 */
public class PromotionCommand implements Command {
    private final Pion pion;
    private final Case depart;
    private final Case arrivee;
    private final Piece pieceCapturee;
    private final PieceType typePromotion;
    private Piece piecePromue;

    /**
     * Constructeur pour une promotion de pion
     * @param pion Le pion à promouvoir
     * @param arrivee La case d'arrivée du pion
     * @param typePromotion Le type de pièce en laquelle promouvoir le pion
     */
    public PromotionCommand(Pion pion, Case arrivee, PieceType typePromotion) {
        this.pion = pion;
        this.depart = pion.getCurrentCase();
        this.arrivee = arrivee;
        this.pieceCapturee = arrivee.getPiece();
        this.typePromotion = typePromotion;
    }

    @Override
    public void execute() {
        // Déplacer le pion
        depart.setPiece(null);

        // Créer la nouvelle pièce selon le type de promotion
        switch (typePromotion) {
            case DAME:
                piecePromue = new Dame(arrivee.getX(), arrivee.getY(), pion.getColor(), pion.getPlateau());
                break;
            case TOUR:
                piecePromue = new Tour(arrivee.getX(), arrivee.getY(), pion.getColor(), pion.getPlateau());
                break;
            case FOU:
                piecePromue = new Fou(arrivee.getX(), arrivee.getY(), pion.getColor(), pion.getPlateau());
                break;
            case CAVALIER:
                piecePromue = new Cavalier(arrivee.getX(), arrivee.getY(), pion.getColor(), pion.getPlateau());
                break;
            default:
                // Par défaut, promouvoir en dame
                piecePromue = new Dame(arrivee.getX(), arrivee.getY(), pion.getColor(), pion.getPlateau());
        }

        // Placer la nouvelle pièce sur la case d'arrivée
        arrivee.setPiece(piecePromue);
    }

    @Override
    public void undo() {
        // Remettre le pion à sa position d'origine
        arrivee.setPiece(pieceCapturee);
        depart.setPiece(pion);
    }
}
