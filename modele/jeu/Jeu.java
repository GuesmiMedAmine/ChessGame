package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;

public class Jeu {
    private Plateau plateau;

    public Jeu() {
        plateau = new Plateau();
        plateau.getCases()[4][0].setPiece(new Roi(4, 0));
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public void demandeDeplacementPiece(Case depart, Case arrivee) {
        if (depart.getPiece() != null && depart.getPiece().mouvementValide(arrivee.getX(), arrivee.getY())) {
            arrivee.setPiece(depart.getPiece());
            depart.setPiece(null);
            plateau.updatePlateau();
        }
    }
}
