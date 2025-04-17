package vuecontroleur;

import java.awt.Color;

public class Deco {
    public static Color getCouleurCase(int x, int y) {
        return (x + y) % 2 == 0
                ? new Color(240, 217, 181) // Beige clair
                : new Color(181, 136, 99); // Marron
    }

    public static Color getCouleurSelection() {
        return Color.YELLOW; // Jaune simple pour la sélection
    }
}