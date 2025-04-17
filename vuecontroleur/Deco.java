package vuecontroleur;

import java.awt.Color;

/**
 * Centralisation des couleurs et préfixes pour l'UI et les logs.
 */
public class Deco {
    /** Couleur de base des cases (alternance beige/marron) */
    public static Color getCouleurCase(int x, int y) {
        return (x + y) % 2 == 0
                ? new Color(250, 238, 238)
                : new Color(137, 191, 97);
    }

    /** Couleur de la case sélectionnée (jaune doux) */
    public static Color getCouleurSelection() {
        return new Color(255, 255, 102);
    }

    /** Couleur de surbrillance des déplacements valides (vert clair transparent) */
    public static Color getCouleurHighlight() {
        return new Color(144, 238, 144, 150);
    }

    /** Couleur des messages d'erreur (rouge clair transparent) */
    public static Color getCouleurErreur() {
        return new Color(255, 102, 102, 180);
    }

    /** Préfixe utilisé pour les logs de déplacement */
    public static String prefixeLog() {
        return "[Déplacement]";
    }
}
