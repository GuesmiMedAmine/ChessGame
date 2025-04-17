package modele.plateau;

import modele.jeu.Piece;
import java.util.Observable;

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
}
