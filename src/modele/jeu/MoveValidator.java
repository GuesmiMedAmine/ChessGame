package modele.jeu;

import modele.deco.DecoRoi;
import modele.deco.DecoPion;
import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.pieces.*;

public class MoveValidator {
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
}
