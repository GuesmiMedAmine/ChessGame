package modele.deco;

import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.pieces.Pion;

import modele.pieces.PieceColor;

import java.util.ArrayList;
import java.util.List;

public class DecoPion extends Deco {
    private final Pion pion;
    private final Plateau plateau;

    public DecoPion(Pion pion, Plateau plateau) {
        this.pion = pion;
        this.plateau = plateau;
    }

    @Override
    public List<Case> getCasesAccessibles() {
        List<Case> cases = new ArrayList<>();
        Case origine = plateau.getCase(pion.getX(), pion.getY());
        int dir = (pion.getColor() == PieceColor.WHITE) ? 1 : -1;

        // Déplacement avant
        Case devant = plateau.getCaseRelative(origine, 0, dir);
        if (devant != null && devant.getPiece() == null) {
            cases.add(devant);
            // Double pas initial
            if ((pion.getColor() == PieceColor.WHITE && origine.getY() == 1)
                    || (pion.getColor() == PieceColor.BLACK && origine.getY() == 6)) {
                Case devant2 = plateau.getCaseRelative(origine, 0, 2 * dir);
                if (devant2 != null && devant2.getPiece() == null) {
                    cases.add(devant2);
                }
            }
        }

        // Prise normale
        for (int dx : new int[]{-1, 1}) {
            Case diag = plateau.getCaseRelative(origine, dx, dir);
            if (diag != null && diag.getPiece() != null
                    && diag.getPiece().getColor() != pion.getColor()) {
                cases.add(diag);
            }
        }

        // Prise en passant
        for (int dx : new int[]{-1, 1}) {
            Case adj = plateau.getCaseRelative(origine, dx, 0);
            if (adj != null && adj.getPiece() instanceof Pion) {
                Pion pionAdj = (Pion) adj.getPiece();
                if (pionAdj.isPriseEnPassantPossible()) {
                    Case priseEP = plateau.getCaseRelative(origine, dx, dir);
                    cases.add(priseEP);
                }
            }
        }

        return cases;
    }
}