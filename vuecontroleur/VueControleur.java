package vuecontroleur;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.image.BufferedImage;
import modele.jeu.Jeu;
import modele.jeu.Piece;
import modele.jeu.Roi;
import modele.plateau.Case;
import modele.plateau.Plateau;

public class VueControleur extends JFrame {
    private Plateau plateau;
    private Jeu jeu;
    private final int sizeX = 8, sizeY = 8;
    private static final int pxCase = 50;
    private JLabel[][] tabJLabel;
    private Case caseClic1;
    private ImageIcon icoRoi;

    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        chargerLesIcones();
        placerLesComposantsGraphiques();
        mettreAJourAffichage();
    }

    private void chargerLesIcones() {
        icoRoi = chargerIcone("Images/wK.png"); // Chemin de l'icône du roi
    }

    private ImageIcon chargerIcone(String urlIcone) {
        ImageIcon icon = new ImageIcon(urlIcone);
        Image img = icon.getImage().getScaledInstance(pxCase, pxCase, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Jeu d'Échecs");
        setResizable(false);
        setSize(sizeX * pxCase, sizeY * pxCase);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX));
        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                jlab.setOpaque(true);
                tabJLabel[x][y] = jlab;

                final int xx = x, yy = y;
                jlab.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        resetBoardColors();
                        tabJLabel[xx][yy].setBackground(Color.YELLOW);
                        caseClic1 = plateau.getCases()[xx][yy];
                    }
                });

                if ((y % 2 == 0 && x % 2 == 0) || (y % 2 != 0 && x % 2 != 0)) {
                    jlab.setBackground(new Color(50, 50, 110));
                } else {
                    jlab.setBackground(new Color(150, 150, 210));
                }
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }

    private void resetBoardColors() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if ((y % 2 == 0 && x % 2 == 0) || (y % 2 != 0 && x % 2 != 0)) {
                    tabJLabel[x][y].setBackground(new Color(50, 50, 110));
                } else {
                    tabJLabel[x][y].setBackground(new Color(150, 150, 210));
                }
            }
        }
    }

    private void mettreAJourAffichage() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                Case c = plateau.getCases()[x][y];
                tabJLabel[x][y].setIcon(null);
                if (c.getPiece() != null) {
                    if (c.getPiece() instanceof Roi) {
                        tabJLabel[x][y].setIcon(icoRoi);
                    }
                }
            }
        }
    }
}
