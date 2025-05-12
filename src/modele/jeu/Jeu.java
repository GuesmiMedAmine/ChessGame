// src/main/java/modele/jeu/Jeu.java
package modele.jeu;

import java.util.ArrayList;
import java.util.List;
import modele.plateau.Plateau;
import modele.plateau.Case;
import modele.pieces.*;
import modele.pieces.PieceColor;
import java.util.Observable;
/**
 * Logique de la partie : historique, tour de jeu, validation, etc.
 * L'initialisation des pièces se fait désormais dans Plateau.
 */
public class Jeu {
    private final Plateau plateau;
    private final List<Coup> historique;
    private PieceColor joueurActuel;

    public Jeu() {
        this.plateau = new Plateau();
        this.historique = new ArrayList<>();
        this.joueurActuel = PieceColor.WHITE;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public PieceColor getJoueurActuel() {
        return joueurActuel;
    }

    /**
     * Vérifie si le joueur spécifié est en échec.
     * @param couleur Couleur du joueur à vérifier
     * @return true si le joueur est en échec
     */
    public boolean estEnEchec(PieceColor couleur) {
        return plateau.estEnEchec(couleur);
    }

    /**
     * Vérifie si la partie est terminée (échec et mat ou pat).
     * @return true si la partie est terminée
     */
    public boolean estPartieTerminee() {
        return estEchecEtMat(PieceColor.WHITE) || estEchecEtMat(PieceColor.BLACK) 
               || estPat(PieceColor.WHITE) || estPat(PieceColor.BLACK);
    }

    /**
     * Retourne le vainqueur de la partie, ou null si la partie n'est pas terminée ou est nulle.
     * @return La couleur du vainqueur, ou null
     */
    public PieceColor getVainqueur() {
        if (estEchecEtMat(PieceColor.WHITE)) return PieceColor.BLACK;
        if (estEchecEtMat(PieceColor.BLACK)) return PieceColor.WHITE;
        return null;
    }

    public boolean jouerCoup(Case depart, Case arrivee) {
        if (depart.getPiece() == null) return false;
        Piece piece = depart.getPiece();

        if (!MoveValidator.isValid(piece, arrivee, plateau)) return false;

        boolean isRoque = false, isEnPassant = false;
        if (piece instanceof Roi && Math.abs(arrivee.getX() - depart.getX()) == 2) {
            isRoque = MoveValidator.validerRoque((Roi) piece, arrivee, plateau);
        } else if (piece instanceof Pion) {
            isEnPassant = MoveValidator.validerPriseEnPassant((Pion) piece, arrivee, plateau);
        }

        Coup coup = new Coup(piece, arrivee, isRoque, isEnPassant);
        historique.add(coup);

        // Afficher le coup dans la console
        String typeMove = isRoque ? "roque" : (isEnPassant ? "prise en passant" : "déplacement");
        System.out.println(joueurActuel + " joue : " + piece.getType() + " de " + 
                           notationAlgebrique(depart) + " à " + notationAlgebrique(arrivee) + 
                           " (" + typeMove + ")");

        if (isRoque) {
            executerRoque((Roi) piece, arrivee);
        } else if (isEnPassant) {
            executerPriseEnPassant((Pion) piece, arrivee);
        } else {
            arrivee.setPiece(piece);
            depart.setPiece(null);
            piece.setPosition(arrivee.getX(), arrivee.getY());
        }

        if (piece instanceof Pion && Math.abs(arrivee.getY() - depart.getY()) == 2) {
            ((Pion) piece).setPriseEnPassantPossible(true);
        }

        PieceColor adversaire = joueurActuel;
        joueurActuel = (joueurActuel == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

        // Vérifier les conditions de fin de partie
        boolean estEnEchec = plateau.estEnEchec(adversaire);
        boolean estMat = estEchecEtMat(adversaire);
        boolean estPat = estPat(adversaire);

        // Afficher l'état du jeu dans la console
        if (estEnEchec) {
            System.out.println(adversaire + " est en échec!");
        }
        if (estMat) {
            System.out.println("ÉCHEC ET MAT! " + joueurActuel + " a gagné la partie!");
        } else if (estPat) {
            System.out.println("PAT! La partie est nulle.");
        }

        plateau.notifierObservers();
        return true;
    }

    private void executerRoque(Roi roi, Case arrivee) {
        int dir = (arrivee.getX() == 6) ? 1 : -1;
        int rookStartX = (dir == 1) ? 7 : 0;
        int rookEndX   = (dir == 1) ? 5 : 3;
        Case rookCase = plateau.getCase(rookStartX, roi.getY());
        Tour rook = (Tour) rookCase.getPiece();

        arrivee.setPiece(roi);
        plateau.getCase(roi.getX(), roi.getY()).setPiece(null);
        roi.setPosition(arrivee.getX(), arrivee.getY());

        plateau.getCase(rookEndX, roi.getY()).setPiece(rook);
        rookCase.setPiece(null);
        rook.setPosition(rookEndX, roi.getY());
    }

    private void executerPriseEnPassant(Pion pion, Case arrivee) {
        int dir = (pion.getColor() == PieceColor.WHITE) ? -1 : 1;
        Case cap = plateau.getCase(arrivee.getX(), arrivee.getY() + dir);
        cap.setPiece(null);

        arrivee.setPiece(pion);
        plateau.getCase(pion.getX(), pion.getY()).setPiece(null);
        pion.setPosition(arrivee.getX(), arrivee.getY());
    }

    public boolean estEchecEtMat(PieceColor couleur) {
        if (!plateau.estEnEchec(couleur)) return false;
        return !aDesMouvementsValides(couleur);
    }

    public boolean estPat(PieceColor couleur) {
        if (plateau.estEnEchec(couleur)) return false;
        return !aDesMouvementsValides(couleur);
    }

    private boolean aDesMouvementsValides(PieceColor couleur) {
        for (Piece p : plateau.getPieces()) {
            if (p.getColor() == couleur) {
                Case origine = p.getCurrentCase();
                for (Case dest : p.getCasesAccessibles()) {
                    Piece backup = dest.getPiece();
                    dest.setPiece(p);
                    origine.setPiece(null);
                    boolean valide = !plateau.estEnEchec(couleur);
                    origine.setPiece(p);
                    dest.setPiece(backup);
                    if (valide) return true;
                }
            }
        }
        return false;
    }

    /**
     * Convertit une case en notation algébrique (ex: "e4").
     * @param c La case à convertir
     * @return La notation algébrique de la case
     */
    private String notationAlgebrique(Case c) {
        char colonne = (char) ('a' + c.getX());
        int ligne = c.getY() + 1;
        return colonne + "" + ligne;
    }
}
