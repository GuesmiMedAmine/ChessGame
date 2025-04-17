package vuecontroleur;

import modele.jeu.Jeu;
import modele.jeu.Piece;
import modele.jeu.PieceColor;
import modele.jeu.PieceType;
import modele.plateau.Case;
import modele.plateau.Plateau;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class VueControleur extends JFrame {
    private final Jeu jeu;
    private final JLabel[][] casesUI;
    private Case caseSelectionnee;

    // Champ pour stocker toutes les icônes
    private final Map<String, ImageIcon> images = new HashMap<>();

    public VueControleur(Jeu jeu) {
        this.jeu = jeu;
        this.casesUI = new JLabel[Plateau.SIZE][Plateau.SIZE];
        configurerUI();
        chargerImages();
        // Quand le modèle change, on rafraîchit les icônes
        jeu.getPlateau().addObserver((o, arg) -> actualiserUI());
        actualiserUI();
    }

    private void configurerUI() {
        setTitle("Jeu d'échecs - POO Lyon1");
        setLayout(new GridLayout(Plateau.SIZE, Plateau.SIZE));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        for (int y = 0; y < Plateau.SIZE; y++) {
            for (int x = 0; x < Plateau.SIZE; x++) {
                JLabel lbl = new JLabel();
                lbl.setOpaque(true);
                lbl.setBackground(Deco.getCouleurCase(x, y));
                lbl.setPreferredSize(new Dimension(60, 60));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);

                final int fx = x, fy = y;
                lbl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        gererClicCase(fx, fy);
                    }
                });

                casesUI[x][y] = lbl;
                add(lbl);
            }
        }
        pack();
    }

    private void chargerImages() {
        // On attend les fichiers sous src/main/resources/images/
        String[] couleurs = {"w", "b"};              // w = blanc, b = noir
        String[] types    = {"K", "Q", "R", "B", "N", "P"};

        for (String coul : couleurs) {
            for (String t : types) {
                String key      = coul + t;             // ex. "wK"
                String path     = "/images/" + key + ".png";
                URL url         = getClass().getResource(path);
                if (url == null) {
                    System.err.println(" Icône introuvable: " + path);
                    continue;
                }
                ImageIcon ico   = new ImageIcon(url);
                Image img       = ico.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                images.put(key, new ImageIcon(img));
            }
        }

        // Débogage : affiche ce qui a été chargé
        System.out.println("Images chargées : " + images.keySet());
    }

    private void actualiserUI() {
        for (int x = 0; x < Plateau.SIZE; x++) {
            for (int y = 0; y < Plateau.SIZE; y++) {
                Piece p = jeu.getPlateau().getCase(x, y).getPiece();
                casesUI[x][y].setIcon(null);
                if (p != null) {
                    String clef = clefPourPiece(p);
                    ImageIcon ico = images.get(clef);
                    casesUI[x][y].setIcon(ico);
                } else {
                    casesUI[x][y].setIcon(null);
                }
            }
        }
        repaint();
    }

    private void actualiserCouleurs() {
        for (int x = 0; x < Plateau.SIZE; x++) {
            for (int y = 0; y < Plateau.SIZE; y++) {
                casesUI[x][y].setBackground(Deco.getCouleurCase(x, y));
            }
        }
    }

    private String clefPourPiece(Piece piece) {
        String coul = (piece.getColor() == PieceColor.BLANC) ? "w" : "b";
        String sym;
        switch (piece.getType()) {
            case ROI:      sym = "K"; break;
            case DAME:     sym = "Q"; break;
            case TOUR:     sym = "R"; break;
            case FOU:      sym = "B"; break;
            case CAVALIER: sym = "N"; break;
            case PION:     sym = "P"; break;
            default:       sym = "?"; break;
        }
        return coul + sym;
    }

    private void gererClicCase(int x, int y) {
        Case caseCourante = jeu.getPlateau().getCase(x, y);

        if (caseSelectionnee == null) {
            // Premier clic : on nettoie d'abord tout l'affichage
            actualiserUI();        // remet toutes les icônes en place
            actualiserCouleurs();  // remet toutes les cases à leur couleur origine

            if (caseCourante.getPiece() != null
                    && caseCourante.getPiece().getColor() == jeu.getJoueurActuel()) {

                // On marque la sélection
                caseSelectionnee = caseCourante;
                casesUI[x][y].setBackground(Deco.getCouleurSelection());

                // Puis on surligne les déplacements possibles
                highlightValidMoves(x, y);
            }
        } else {
            // Deuxième clic : tentative de déplacement
            if (caseCourante != caseSelectionnee) {
                jeu.deplacerPiece(caseSelectionnee, caseCourante);
            }
            caseSelectionnee = null;
            actualiserCouleurs();
        }
    }

    private void highlightValidMoves(int x, int y) {
        Piece piece = jeu.getPlateau().getCase(x, y).getPiece();
        if (piece == null) return;

        for (int destX = 0; destX < Plateau.SIZE; destX++) {
            for (int destY = 0; destY < Plateau.SIZE; destY++) {
                if (piece.mouvementValide(destX, destY, jeu.getPlateau())) {
                    // (optionnel) supprimer l'icône si par hasard
                    casesUI[destX][destY].setIcon(null);

                    // on colore en vert
                    casesUI[destX][destY].setBackground(Deco.getCouleurHighlight());
                }
            }
        }
        // on force un repaint complet
        repaint();
    }



}


