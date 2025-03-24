import java.util.Observable;

public class Modele extends Observable {
    public int i;
    public int j;

    public Modele() {
        i = 0;
        j = 0;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    }
    
