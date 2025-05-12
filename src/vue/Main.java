package vue;

import controlleur.Controlleur;
import utils.ThreadManager;
import vue.IView;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

/**
 * Point d'entrée : lance l'application d'échecs.
 * Affiche un menu textuel permettant à l'utilisateur de choisir entre le mode console et le mode graphique.
 */
public class Main {
    public static void main(String[] args) {
        // Afficher le menu textuel
        System.out.println("Bienvenue au jeu d'échecs !");
        System.out.println("Veuillez choisir un mode de jeu :");
        System.out.println();
        System.out.println("1. Mode Console");
        System.out.println();
        System.out.println("2. Mode Graphique");
        System.out.println("Saisissez 1 ou 2 :");

        // Lire l'entrée utilisateur
        Scanner scanner = new Scanner(System.in);
        int choix = 0;

        // Boucle jusqu'à ce que l'utilisateur entre une valeur valide
        while (choix != 1 && choix != 2) {
            try {
                String input = scanner.nextLine().trim();
                choix = Integer.parseInt(input);

                if (choix != 1 && choix != 2) {
                    System.out.println("Veuillez saisir 1 pour le mode Console ou 2 pour le mode Graphique :");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez saisir 1 pour le mode Console ou 2 pour le mode Graphique :");
            }
        }

        // Lancer le mode choisi
        if (choix == 1) {
            System.out.println("Démarrage en mode console...");
            launchConsoleView();
        } else {
            System.out.println("Démarrage en mode graphique...");
            SwingUtilities.invokeLater(() -> {
                launchGraphicalView();
            });
        }
    }

    /**
     * Lance l'application en mode graphique.
     */
    private static void launchGraphicalView() {
        // Créer le contrôleur qui initialise le modèle et la vue graphique
        Controlleur ctrl = new Controlleur(true);

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
        frame.add((JPanel) ctrl.getVue());

        // Informer le contrôleur de la fenêtre principale
        ctrl.setFrame(frame);

        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Lance l'application en mode console.
     */
    private static void launchConsoleView() {
        // Créer le contrôleur qui initialise le modèle et la vue console
        Controlleur ctrl = new Controlleur(false);

        // Afficher un message d'accueil
        System.out.println("Bienvenue dans le jeu d'échecs en mode console !");
        System.out.println("Pour quitter, appuyez sur Ctrl+C.");

        // Garder l'application en vie
        try {
            // Attendre indéfiniment (l'utilisateur peut quitter avec Ctrl+C)
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            // Arrêter proprement les threads
            ThreadManager.getInstance().shutdown();
        }
    }
}
