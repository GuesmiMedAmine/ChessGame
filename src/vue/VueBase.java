package vue;

import modele.plateau.Case;
import modele.plateau.Plateau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

/**
 * Classe de base abstraite pour les vues de l'échiquier.
 * Contient les fonctionnalités communes à toutes les vues.
 */
public abstract class VueBase extends JPanel implements Observer {
    protected final JLabel[][] grid = new JLabel[8][8];

    /**
     * Initialise l'interface utilisateur avec une grille de labels.
     */
    protected void initUI() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                JLabel lbl = new JLabel();
                lbl.setOpaque(true);
                lbl.setPreferredSize(new Dimension(80, 80));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setVerticalAlignment(SwingConstants.CENTER);
                lbl.putClientProperty("pos", new Point(x, y));
                grid[x][y] = lbl;
                add(lbl);
            }
        }
    }

    /**
     * Applique un listener de clics sur toutes les cases.
     */
    public void addCaseClickListener(MouseListener ml) {
        for (var row : grid)
            for (var lbl : row)
                lbl.addMouseListener(ml);
    }

    /**
     * Retourne la couleur de fond d'une case en fonction de sa position.
     */
    protected Color getCaseColor(int x, int y) {
        return (x + y) % 2 == 0
                ? new Color(238, 238, 210)  // Couleur claire
                : new Color(118, 150, 86);  // Couleur foncée
    }

    /**
     * Met à jour l'affichage des pièces sur l'échiquier.
     */
    protected void updatePieces(Plateau plateau) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                JLabel lbl = grid[x][y];
                Case c = plateau.getCase(x, y);
                
                // Fond de la case
                lbl.setBackground(getCaseColor(x, y));
                
                // Pièce
                if (c.getPiece() != null) {
                    ImageIcon icon = new ImageIcon(
                            getClass().getResource(c.getPiece().getImagePath())
                    );
                    Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    lbl.setIcon(new ImageIcon(img));
                } else {
                    lbl.setIcon(null);
                }
            }
        }
    }
}