package vue;

import controlleur.Controlleur;
import utils.ThreadManager;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Point d'entrée : lance l'interface graphique.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Créer le contrôleur qui initialise le modèle et la vue
            Controlleur ctrl = new Controlleur();

            // Configurer et afficher la fenêtre principale
            JFrame frame = new JFrame("Chess MVC");

            // Ajouter un gestionnaire de fermeture pour arrêter proprement les threads
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // Arrêter le pool de threads
                    ThreadManager.getInstance().shutdown();
                    // Fermer l'application
                    System.exit(0);
                }
            });

            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.add(ctrl.getVue());
            frame.setSize(600, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
