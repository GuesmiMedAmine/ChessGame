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
    private final JButton[][] grid;
    private Case selectedCase;
    private List<Case> validMoves = new ArrayList<>();

    public VueControleur() {
        jeu = new Jeu();
        grid = new JButton[8][8];
        setLayout(new GridLayout(8, 8));
        initialiserUI();
        rafraichirUI();
    }

    private void initialiserUI() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(80, 80)); // Taille fixe
                btn.setHorizontalAlignment(SwingConstants.CENTER);
                btn.setVerticalAlignment(SwingConstants.CENTER);

                // Gestion des clics
                final int x = i;
                final int y = j;
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        gererClicCase(x, y);
                    }
                });

                grid[i][j] = btn;
                add(btn);
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
        }
        rafraichirUI();
    }

    private void rafraichirUI() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton btn = grid[i][j];
                Case c = jeu.getPlateau().getCase(i, j);

                // Réinitialisation de l'apparence
                btn.setBackground((i + j) % 2 == 0 ? new Color(238, 238, 210) : new Color(118, 150, 86));
                btn.setIcon(null);

                // Mise à jour de l'icône
                if (c.getPiece() != null) {
                    ImageIcon icon = new ImageIcon(getClass().getResource(c.getPiece().getImagePath()));
                    Image img = icon.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
                    btn.setIcon(new ImageIcon(img));
                }

                // Surbrillance
                if (selectedCase != null && selectedCase.equals(c)) {
                    btn.setBackground(new Color(255, 255, 0, 150)); // Jaune transparent
                }
                else if (validMoves.contains(c)) {
                    btn.setBackground(new Color(0, 255, 0, 150)); // Vert transparent
                }
            }
        }
        repaint();
    }
}