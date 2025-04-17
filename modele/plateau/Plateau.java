package modele.plateau;

import modele.jeu.Piece;
import java.util.Observable;
import modele.jeu.PieceColor;
import modele.jeu.PieceType;

public class Plateau extends Observable {
    public static final int SIZE = 8;
    private final Case[][] cases;

    public Plateau() {
        cases = new Case[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                cases[x][y] = new Case(x, y);
            }
        }
    }

    public Case getCase(int x, int y) {
        return cases[x][y];
    }

    public void mettreAJour() {
        setChanged();
        notifyObservers();
    }
    public Case getRoi(PieceColor couleur) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Piece p = getCase(x, y).getPiece();
                if (p != null && p.getType() == PieceType.ROI && p.getColor() == couleur) {
                    return getCase(x, y);
                }
            }
        }
        return null;
    }

    public boolean roiEnEchec(PieceColor couleur) {
        Case roi = getRoi(couleur);
        if (roi == null) return true;

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Piece p = getCase(x, y).getPiece();
                if (p != null && p.getColor() != couleur) {
                    if (p.mouvementValide(roi.getX(), roi.getY(), this)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
