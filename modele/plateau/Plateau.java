// src/main/java/modele/plateau/Plateau.java
package modele.plateau;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import modele.pieces.*;

/**
 * Représente l’échiquier, stocke les cases et la liste des pièces,
 * et définit les directions via un enum interne.
 */
public class Plateau extends Observable {
    public static final int SIZE = 8;

    private final Case[][] cases;
    private final List<Piece> pieces;

    /**
     * Les 8 directions possibles, avec leur vecteur (dx, dy).
     */
    public enum Direction {
        UP(-1, 0), DOWN(1, 0),
        LEFT(0, -1), RIGHT(0, 1),
        UP_LEFT(-1, -1), UP_RIGHT(-1, 1),
        DOWN_LEFT(1, -1), DOWN_RIGHT(1, 1);

        private final int dx, dy;
        Direction(int dx, int dy) { this.dx = dx; this.dy = dy; }
        public int dx() { return dx; }
        public int dy() { return dy; }
    }

    public Plateau() {
        // Création du damier
        cases = new Case[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                cases[x][y] = new Case(x, y);
            }
        }

        // Initialisation de la liste de pièces
        pieces = new ArrayList<>();
        initPieces();
    }

    /**
     * Place toutes les pièces sur le plateau au démarrage.
     * (Déplacé depuis Jeu.initialiserPieces())
     */
    private void initPieces() {
        // Pièces blanches
        pieces.add(new Tour    (0, 0, PieceColor.WHITE, this));
        pieces.add(new Cavalier(1, 0, PieceColor.WHITE, this));
        pieces.add(new Fou     (2, 0, PieceColor.WHITE, this));
        pieces.add(new Dame    (3, 0, PieceColor.WHITE, this));
        pieces.add(new Roi     (4, 0, PieceColor.WHITE, this));
        pieces.add(new Fou     (5, 0, PieceColor.WHITE, this));
        pieces.add(new Cavalier(6, 0, PieceColor.WHITE, this));
        pieces.add(new Tour    (7, 0, PieceColor.WHITE, this));
        for (int i = 0; i < SIZE; i++) {
            pieces.add(new Pion(i, 1, PieceColor.WHITE, this));
        }

        // Pièces noires
        pieces.add(new Tour    (0, 7, PieceColor.BLACK, this));
        pieces.add(new Cavalier(1, 7, PieceColor.BLACK, this));
        pieces.add(new Fou     (2, 7, PieceColor.BLACK, this));
        pieces.add(new Dame    (3, 7, PieceColor.BLACK, this));
        pieces.add(new Roi     (4, 7, PieceColor.BLACK, this));
        pieces.add(new Fou     (5, 7, PieceColor.BLACK, this));
        pieces.add(new Cavalier(6, 7, PieceColor.BLACK, this));
        pieces.add(new Tour    (7, 7, PieceColor.BLACK, this));
        for (int i = 0; i < SIZE; i++) {
            pieces.add(new Pion(i, 6, PieceColor.BLACK, this));
        }

        // Placement sur les cases
        for (Piece p : pieces) {
            Case c = getCase(p.getX(), p.getY());
            c.setPiece(p);
        }
    }

    public Case getCase(int x, int y) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) return null;
        return cases[x][y];
    }

    public Case getCaseRelative(Case origine, int dx, int dy) {
        return getCase(origine.getX() + dx, origine.getY() + dy);
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public boolean estEnEchec(PieceColor couleurJoueur) {
        Case roi = getRoi(couleurJoueur);
        if (roi == null) return false;
        PieceColor adv = (couleurJoueur == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        for (Piece p : pieces) {
            if (p.getColor() == adv) {
                if (p.getCasesAccessibles().contains(roi)) return true;
            }
        }
        return false;
    }

    public Case getRoi(PieceColor couleur) {
        for (Piece p : pieces) {
            if (p.getType() == PieceType.ROI && p.getColor() == couleur) {
                return getCase(p.getX(), p.getY());
            }
        }
        return null;
    }
}
