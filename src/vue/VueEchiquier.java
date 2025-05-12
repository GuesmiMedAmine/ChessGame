package vue;

import modele.plateau.Plateau;

import java.util.Observable;

/**
 * Vue simple de l'échiquier sans fonctionnalités de sélection.
 * Étend la classe de base VueBase.
 */
public class VueEchiquier extends VueBase {

    public VueEchiquier(Plateau plateau) {
        plateau.addObserver(this);
        setLayout(new java.awt.GridLayout(8, 8));
        initUI();
        update(plateau, null);
    }

    @Override
    public void update(Observable o, Object arg) {
        Plateau p = (Plateau) o;
        updatePieces(p);
        revalidate();
        repaint();
    }
}
