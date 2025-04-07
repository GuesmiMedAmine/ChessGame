package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;
import java.util.ArrayList;
import java.util.List;

public class Jeu {
    private Plateau plateau;
    private List<String> coups;

    public Jeu() {
        nouvellePartie();
    }

    public void nouvellePartie() {
        plateau = new Plateau();
        coups = new ArrayList<>();

        // Exemple : initialisation de quelques pièces
        plateau.getCases()[0][0].setPiece(new Piece(0, 0, PieceType.TOUR, PieceColor.BLANC));
        plateau.getCases()[4][0].setPiece(new Piece(4, 0, PieceType.ROI, PieceColor.BLANC));
        plateau.updatePlateau();
    }

    public Plateau getPlateau() { return plateau; }
    public List<String> getCoups() { return coups; }
    public void setCoups(List<String> coups) { this.coups = coups; }

    public void demandeDeplacementPiece(Case depart, Case arrivee) {
        if (depart.getPiece() != null && depart.getPiece().mouvementValide(arrivee.getX(), arrivee.getY())) {
            Piece p = depart.getPiece();
            p.setPosition(arrivee.getX(), arrivee.getY());
            arrivee.setPiece(p);
            depart.setPiece(null);

            coups.add(String.format("%s de (%d,%d) à (%d,%d)",
                    p.getType(), p.getX(), p.getY(), arrivee.getX(), arrivee.getY()));

            plateau.updatePlateau();
        }
    }
}
