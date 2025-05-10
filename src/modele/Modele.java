package modele;

import java.util.Observable;

public class Modele extends Observable {
    private int i, j;

    public Modele() { i = 0; j = 0; }

    public void setPosition(int i, int j) {
        this.i = i;
        this.j = j;
        setChanged();
        notifyObservers();
    }

    public int getI() { return i; }
    public int getJ() { return j; }
}