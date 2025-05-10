package controlleur;

import modele.jeu.Jeu;

import java.util.Observable;
import java.util.Observer;

/**
 * Controlleur de parties : singleton qui initialise et pilote
 * le thread de chaque nouvelle partie.
 */
public class Controlleur implements Observer {
    private static final Controlleur instance = new Controlleur();

    private Jeu jeu;
    private Thread gameThread;

    /** Accès unique au contrôleur */
    public static Controlleur getInstance() {
        return instance;
    }

    // Constructeur privé pour garantir le singleton
    private Controlleur() { }

    /**
     * Initialise une nouvelle partie :
     *  - interrompt celle en cours,
     *  - crée le Jeu,
     *  - s'abonne aux changements du Plateau,
     *  - démarre un thread dédié.
     */
    public synchronized void initJeu() {
        // Arrêter l’ancienne partie si besoin
        stopPartie();

        // Créer le modèle de jeu
        this.jeu = new Jeu();
        // S’abonner aux notifications du plateau
        jeu.getPlateau().addObserver(this);

        // Tâche de gestion (à enrichir selon besoin)
        Runnable gameTask = () -> {
            System.out.println("Partie démarrée [" + Thread.currentThread().getName() + "]");
            // Boucle / logique périodique ici
        };

        gameThread = new Thread(gameTask, "JeuThread-" + System.currentTimeMillis());
        gameThread.start();
    }

    /** Interrompt proprement le thread de la partie en cours. */
    public synchronized void stopPartie() {
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
            System.out.println("Partie interrompue [" + gameThread.getName() + "]");
        }
    }

    /** @return l’objet Jeu de la partie active (ou null s’il n’est pas encore initialisé) */
    public Jeu getJeu() {
        return jeu;
    }

    /**
     * Callback appelé à chaque modification du Plateau.
     * @param o   l’Observable (le Plateau)
     * @param arg argument facultatif
     */
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Plateau mis à jour -> " + arg);
    }
}
