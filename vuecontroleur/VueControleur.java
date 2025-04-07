package vuecontroleur;

import modele.jeu.*;
import modele.plateau.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class VueControleur extends JFrame {
    private Plateau plateau;
    private Jeu jeu;
    private final int sizeX = 8, sizeY = 8;
    private static final int pxCase = 50;
    private JLabel[][] tabJLabel;
    private Case caseClic1 = null;
    private Map<String, ImageIcon> icones;

    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        plateau.addObserver((o, arg) -> mettreAJourAffichage());
        chargerLesIcones();
        placerLesComposantsGraphiques();
        mettreAJourAffichage();
    }

    private void chargerLesIcones() {
        icones = new HashMap<>();
        for (PieceType type : PieceType.values()) {
            for (PieceColor color : PieceColor.values()) {
                String nomFichier = "Images/" +
                        (color == PieceColor.BLANC ? "w" : "b") +
                        switch (type) {
                            case ROI -> "K";
                            case DAME -> "Q";
                            case TOUR -> "R";
                            case FOU -> "B";
                            case CAVALIER -> "N";
                            case PION -> "P";
                        } + ".png";

                ImageIcon icon = new ImageIcon(nomFichier);
                Image img = icon.getImage().getScaledInstance(pxCase, pxCase, Image.SCALE_SMOOTH);
                icones.put(type + "_" + color, new ImageIcon(img));
            }
        }
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
                jlab.setHorizontalAlignment(SwingConstants.CENTER);
                jlab.setVerticalAlignment(SwingConstants.CENTER);
                tabJLabel[x][y] = jlab;

                final int xx = x, yy = y;
                jlab.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Case clickedCase = plateau.getCases()[xx][yy];
                        if (caseClic1 == null) {
                            if (clickedCase.getPiece() != null) {
                                caseClic1 = clickedCase;
                                tabJLabel[xx][yy].setBackground(Color.YELLOW);
                            }
                        } else {
                            jeu.demandeDeplacementPiece(caseClic1, clickedCase);
                            caseClic1 = null;
                            resetBoardColors();
                        }
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
                    Piece p = c.getPiece();
                    String key = p.getType() + "_" + p.getColor();
                    tabJLabel[x][y].setIcon(icones.get(key));
                }

            }
            }
        }

    }

