package modele.jeu.command;

import modele.pieces.Piece;
import modele.plateau.Case;

/**
 * Commande pour déplacer une pièce d'une case à une autre.
 * Implémente le pattern Command.
 */
public class DeplacerPieceCommand implements Command {
    private final Piece piece;
    private final Case depart;
    private final Case arrivee;
    private final Piece pieceCapturee;

    /**
     * Constructeur pour un déplacement de pièce
     * @param piece La pièce à déplacer
     * @param arrivee La case d'arrivée
     */
    public DeplacerPieceCommand(Piece piece, Case arrivee) {
        this.piece = piece;
        this.depart = piece.getCurrentCase();
        this.arrivee = arrivee;
        this.pieceCapturee = arrivee.getPiece();
    }

    @Override
    public void execute() {
        // Sauvegarder la pièce capturée (si présente)
        // Déplacer la pièce
        depart.setPiece(null);
        arrivee.setPiece(piece);
        piece.setPosition(arrivee.getX(), arrivee.getY());
    }

    @Override
    public void undo() {
        // Remettre la pièce à sa position d'origine
        arrivee.setPiece(pieceCapturee);
        depart.setPiece(piece);
        piece.setPosition(depart.getX(), depart.getY());
    }
}
