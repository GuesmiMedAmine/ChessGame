package modele.plateau;

import java.util.Observable;

public class Plateau extends Observable {
    public static final int SIZE = 8;
    private final Case[][] cases;

    public Plateau() {
        cases = new Case[SIZE][SIZE];
        for(int x=0; x<SIZE; x++)
            for(int y=0; y<SIZE; y++)
                cases[x][y] = new Case(x, y);
    }

    public Case getCase(int x, int y) {
        if(x < 0 || y < 0 || x >= SIZE || y >= SIZE) return null;
        return cases[x][y];
    }

    public Case getCaseRelative(Case origine, int dx, int dy) {
        return getCase(origine.getX() + dx, origine.getY() + dy);
    }

    public void mettreAJour() {
        setChanged();
        notifyObservers();
    }
}