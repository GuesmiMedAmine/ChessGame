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
public class Plateau extends Observable implements Cloneable {
    public static final int SIZE = 8;

    private final Case[][] cases;
    private final List<Piece> pieces;

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
            Case c = p.getCurrentCase(this);
            c.setPiece(p);
        }
    }
    public void notifierObservers() {
        setChanged();           // accessible ici, car on est dans Plateau
        notifyObservers();
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

    /**
     * Vérifie si le roi de la couleur spécifiée est en échec.
     * @param couleurJoueur Couleur du joueur à vérifier
     * @return true si le roi est en échec
     */
    public boolean estEnEchec(PieceColor couleurJoueur) {
        return estEnEchec(couleurJoueur, true);
    }

    /**
     * Vérifie si le roi de la couleur spécifiée est en échec.
     * @param couleurJoueur Couleur du joueur à vérifier
     * @param checkKingMoves Si true, vérifie aussi les mouvements des rois adverses
     * @return true si le roi est en échec
     */
    public boolean estEnEchec(PieceColor couleurJoueur, boolean checkKingMoves) {
        Case roi = getRoi(couleurJoueur);
        if (roi == null) return false;
        PieceColor adv = (couleurJoueur == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        for (Piece p : pieces) {
            if (p.getColor() == adv) {
                // Si on ne vérifie pas les mouvements des rois ou si la pièce n'est pas un roi
                if (!checkKingMoves && p.getType() == PieceType.ROI) {
                    continue;
                }
                if (p.getCasesAccessibles().contains(roi)) return true;
            }
        }
        return false;
    }

    public Case getRoi(PieceColor couleur) {
        for (Piece p : pieces) {
            if (p.getType() == PieceType.ROI && p.getColor() == couleur) {
                return p.getCurrentCase(this);
            }
        }
        return null;
    }

    /**
     * Crée une copie profonde du plateau.
     * Cette méthode est utilisée pour simuler des coups sans modifier le plateau original.
     * @return Une copie profonde du plateau
     */
    @Override
    public Plateau clone() {
        try {
            Plateau clone = (Plateau) super.clone();

            // Créer un nouveau tableau de cases
            Case[][] clonedCases = new Case[SIZE][SIZE];
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    clonedCases[x][y] = new Case(x, y);
                }
            }

            // Créer une nouvelle liste de pièces
            List<Piece> clonedPieces = new ArrayList<>();

            // Copier les pièces et les placer sur les cases clonées
            for (Piece originalPiece : pieces) {
                // Créer une nouvelle pièce du même type
                Piece clonedPiece = null;

                switch (originalPiece.getType()) {
                    case ROI:
                        clonedPiece = new Roi(originalPiece.getX(), originalPiece.getY(), 
                                             originalPiece.getColor(), clone);
                        break;
                    case DAME:
                        clonedPiece = new Dame(originalPiece.getX(), originalPiece.getY(), 
                                              originalPiece.getColor(), clone);
                        break;
                    case TOUR:
                        clonedPiece = new Tour(originalPiece.getX(), originalPiece.getY(), 
                                              originalPiece.getColor(), clone);
                        break;
                    case FOU:
                        clonedPiece = new Fou(originalPiece.getX(), originalPiece.getY(), 
                                             originalPiece.getColor(), clone);
                        break;
                    case CAVALIER:
                        clonedPiece = new Cavalier(originalPiece.getX(), originalPiece.getY(), 
                                                  originalPiece.getColor(), clone);
                        break;
                    case PION:
                        clonedPiece = new Pion(originalPiece.getX(), originalPiece.getY(), 
                                              originalPiece.getColor(), clone);
                        break;
                }

                if (clonedPiece != null) {
                    clonedPieces.add(clonedPiece);
                    clonedCases[clonedPiece.getX()][clonedPiece.getY()].setPiece(clonedPiece);
                }
            }

            // Utiliser la réflexion pour définir les champs privés finaux
            java.lang.reflect.Field casesField = Plateau.class.getDeclaredField("cases");
            casesField.setAccessible(true);
            casesField.set(clone, clonedCases);

            java.lang.reflect.Field piecesField = Plateau.class.getDeclaredField("pieces");
            piecesField.setAccessible(true);
            piecesField.set(clone, clonedPieces);

            return clone;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors du clonage du plateau", e);
        }
    }
}
