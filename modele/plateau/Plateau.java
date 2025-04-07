package modele.plateau;

import modele.jeu.Piece;

import java.util.Observable;

public class Plateau extends Observable {
    private Case[][] cases;

    public Plateau(int largeur, int hauteur) {
        cases = new Case[largeur][hauteur];
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                cases[x][y] = new Case(x, y);
            }
        }
    }

    public Case getCase(int x, int y) {
        return cases[x][y];
    }

    public Case[][] getCases() {
        return cases;
    }

    public void setPiece(int x, int y, Piece piece) {
        cases[x][y].setPiece(piece);
        setChanged();
        notifyObservers();
    }

    public Piece getPiece(int x, int y) {
        return cases[x][y].getPiece();
    }
}