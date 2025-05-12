package modele.jeu;

import modele.deco.DecoRoi;
import modele.deco.DecoPion;
import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.pieces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire pour valider les mouvements d'échecs.
 * Implémente des algorithmes de backtracking pour détecter l'échec, l'échec et mat, et le pat.
 */
public class MoveValidator {
    /**
     * Vérifie si un mouvement est valide.
     * @param p La pièce à déplacer
     * @param tgt La case cible
     * @param plat Le plateau
     * @return true si le mouvement est valide, false sinon
     */
    public static boolean isValid(Piece p, Case tgt, Plateau plat) {
        // Vérifications spéciales avant la validation standard
        if (p instanceof Roi && Math.abs(tgt.getX() - p.getX()) == 2) {
            // Déléguer la validation du roque au décorateur du roi
            DecoRoi decoRoi = (DecoRoi) ((Roi)p).getDecorateur();
            return decoRoi.validerRoque((Roi)p, tgt);
        }

        if (p instanceof Pion && p.getDecorateur() instanceof DecoPion) {
            // Déléguer la validation de la prise en passant au décorateur du pion
            DecoPion decoPion = (DecoPion) p.getDecorateur();
            if (decoPion.isPriseEnPassantCase((Pion)p, tgt)) {
                return decoPion.validerPriseEnPassant((Pion)p, tgt);
            }
        }

        // Validation standard
        return p.getCasesAccessibles().contains(tgt)
                && !simulerEchec(p, tgt, plat);
    }

    /**
     * Simule un mouvement pour vérifier si le roi serait en échec après.
     * @param p La pièce à déplacer
     * @param tgt La case cible
     * @param plat Le plateau
     * @return true si le roi serait en échec après le mouvement, false sinon
     */
    private static boolean simulerEchec(Piece p, Case tgt, Plateau plat) {
        // Stocker la position initiale et la case cible
        int xOriginal = p.getX();
        int yOriginal = p.getY();
        Case caseOriginale = p.getCurrentCase();
        Piece pieceCapturee = tgt.getPiece();

        // Simulation du mouvement pour vérifier l'échec
        p.setPosition(tgt.getX(), tgt.getY());
        tgt.setPiece(p);
        caseOriginale.setPiece(null);

        boolean enEchec = plat.estEnEchec(p.getColor());

        // Annulation de la simulation
        p.setPosition(xOriginal, yOriginal);
        caseOriginale.setPiece(p);
        tgt.setPiece(pieceCapturee);

        return enEchec;
    }

    /**
     * Vérifie si un joueur est en échec.
     * @param plateau Le plateau
     * @param color La couleur du joueur
     * @return true si le joueur est en échec, false sinon
     */
    public static boolean isInCheck(Plateau plateau, PieceColor color) {
        return plateau.estEnEchec(color);
    }

    /**
     * Vérifie si un joueur a une défense légale contre l'échec en utilisant un algorithme de backtracking.
     * @param plateau Le plateau
     * @param defender La couleur du joueur qui doit se défendre
     * @return true si le joueur a une défense légale, false sinon
     */
    public static boolean hasLegalDefense(Plateau plateau, PieceColor defender) {
        // Cloner le plateau pour les simulations
        Plateau clonedPlateau = plateau.clone();

        // Parcourir toutes les pièces du défenseur
        for (Piece piece : clonedPlateau.getPieces()) {
            if (piece.getColor() == defender) {
                // Générer tous les coups légaux pour cette pièce
                List<Case> accessibleCases = piece.getCasesAccessibles();

                // Pour chaque coup légal, vérifier s'il permet d'échapper à l'échec
                for (Case targetCase : accessibleCases) {
                    // Stocker l'état actuel
                    Case originalCase = piece.getCurrentCase(clonedPlateau);
                    Piece capturedPiece = targetCase.getPiece();

                    // Simuler le mouvement
                    originalCase.setPiece(null);
                    piece.setPosition(targetCase.getX(), targetCase.getY());
                    targetCase.setPiece(piece);

                    // Vérifier si le roi est toujours en échec après le mouvement
                    boolean stillInCheck = clonedPlateau.estEnEchec(defender);

                    // Restaurer l'état
                    piece.setPosition(originalCase.getX(), originalCase.getY());
                    originalCase.setPiece(piece);
                    targetCase.setPiece(capturedPiece);

                    // Si un coup permet d'échapper à l'échec, retourner true
                    if (!stillInCheck) {
                        return true;
                    }
                }
            }
        }

        // Aucun coup ne permet d'échapper à l'échec
        return false;
    }

    /**
     * Vérifie si un joueur est en échec et mat.
     * @param plateau Le plateau
     * @param color La couleur du joueur
     * @return true si le joueur est en échec et mat, false sinon
     */
    public static boolean isCheckmate(Plateau plateau, PieceColor color) {
        // Un joueur est en échec et mat s'il est en échec et n'a aucune défense légale
        return isInCheck(plateau, color) && !hasLegalDefense(plateau, color);
    }

    /**
     * Vérifie si un joueur est en pat.
     * @param plateau Le plateau
     * @param color La couleur du joueur
     * @return true si le joueur est en pat, false sinon
     */
    public static boolean isStalemate(Plateau plateau, PieceColor color) {
        // Un joueur est en pat s'il n'est pas en échec mais n'a aucun coup légal
        return !isInCheck(plateau, color) && !hasLegalDefense(plateau, color);
    }
}
