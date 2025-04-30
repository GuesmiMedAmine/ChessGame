package vuecontroleur;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Créer et ajouter notre VueControleur
            VueControleur vueControleur = new VueControleur();
            frame.add(vueControleur);

            // Configuration de la fenêtre
            frame.setSize(600, 600);
            frame.setLocationRelativeTo(null); // Centrer la fenêtre
            frame.setVisible(true);
        });
    }
}