package modele.plateau;

import java.util.Observable;

public class Plateau extends Observable {
    public final int SIZE_X = 8;
    public final int SIZE_Y = 8;
    private Case[][] cases;

    public Plateau() {
        cases = new Case[SIZE_X][SIZE_Y];
        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                cases[x][y] = new Case(x, y);
            }
        }
    }

    public Case[][] getCases() {
        return cases;
    }

    public void updatePlateau() {
        setChanged();
        notifyObservers();
    }
}
