package vuecontroleur;

import modele.jeu.Jeu;
import modele.pieces.Piece;
import modele.plateau.Case;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class VueControleur extends JPanel {
    private final Jeu jeu;
    private final JLabel[][] grid;
    private Case selectedCase;
    private List<Case> validMoves = new ArrayList<>();

    public VueControleur() {
        jeu = new Jeu();
        grid = new JLabel[8][8];
        setLayout(new GridLayout(8, 8));
        initialiserUI();
        rafraichirUI();
    }

    private void initialiserUI() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JLabel lbl = new JLabel();
                lbl.setOpaque(true);
                lbl.setPreferredSize(new Dimension(80, 80));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setVerticalAlignment(SwingConstants.CENTER);


                // Gestion des clics
                final int x = i;
                final int y = j;
                lbl.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        gererClicCase(x, y);
                    }
                });

                grid[i][j] = lbl;
                add(lbl);
            }
        }
    }

    private void gererClicCase(int x, int y) {
        Case clickedCase = jeu.getPlateau().getCase(x, y);

        if (selectedCase == null) {
            // Sélection d'une pièce
            if (clickedCase.getPiece() != null &&
                    clickedCase.getPiece().getColor() == jeu.getJoueurActuel()) {
                selectedCase = clickedCase;
                validMoves = selectedCase.getPiece().getCasesAccessibles();
            }
        } else {
            // Déplacement
            if (validMoves.contains(clickedCase)) {
                jeu.jouerCoup(selectedCase, clickedCase);
            }
            selectedCase = null;
            validMoves.clear();
            getParent().repaint();
        }
        rafraichirUI();
    }

    private void rafraichirUI() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JLabel lbl = grid[i][j];
                Case c = jeu.getPlateau().getCase(i, j);

                // Réinitialisation complète
                lbl.setBackground((i + j) % 2 == 0 ? new Color(238, 238, 210) : new Color(118, 150, 86));
                lbl.setIcon(null);
                lbl.setBorder(null);

                // Afficher l'icône seulement si nécessaire
                if (c.getPiece() != null && !validMoves.contains(c)) {
                    ImageIcon icon = new ImageIcon(getClass().getResource(c.getPiece().getImagePath()));
                    Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    lbl.setIcon(new ImageIcon(img));
                }

                // Surbrillance sans icône
                if (selectedCase != null && selectedCase.equals(c)) {
                    lbl.setBackground(new Color(255, 255, 0, 150)); // Jaune
                }
                else if (validMoves.contains(c)) {
                    lbl.setBackground(new Color(144, 238, 144, 150)); // Vert
                    lbl.setIcon(null); // Forcer la suppression
                }
            }
        }
        repaint(); // Rafraîchissement global
    }}
