package vuecontroleur;

import java.awt.Color;

public class Deco {
    /**
     * Couleur de base des cases (alternance beige/marron)
     */
    public static Color getCouleurCase(int x, int y) {
        return (x + y) % 2 == 0
                ? new Color(240, 217, 181) // Beige clair
                : new Color(181, 136, 99);  // Marron
    }

    /**
     * Couleur de la case sélectionnée (jaune)
     */
    public static Color getCouleurSelection() {
        return new Color(255, 255, 102); // Jaune plus doux
    }

    /**
     * Couleur de surbrillance des déplacements valides (vert clair transparent)
     */
    public static Color getCouleurHighlight() {
        return new Color(144, 238, 144, 150);
    }

    /**
     * Couleur de fond pour les messages d'erreur ou avertissements (rouge clair)
     */
    public static Color getCouleurErreur() {
        return new Color(255, 102, 102, 180);
    }

    public static String prefixeLog() {
        return "[Déplacement]";
    }

}