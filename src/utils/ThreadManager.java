package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;

/**
 * Gestionnaire de threads pour l'application d'échecs.
 * Fournit un pool de threads pour exécuter des tâches en arrière-plan
 * et des méthodes utilitaires pour exécuter des tâches sur l'EDT.
 */
public class ThreadManager {
    // Singleton pour le gestionnaire de threads
    private static ThreadManager instance;
    
    // Pool de threads pour les tâches en arrière-plan
    private final ExecutorService threadPool;
    
    private ThreadManager() {
        // Créer un pool de threads avec un nombre de threads égal au nombre de processeurs
        int processors = Runtime.getRuntime().availableProcessors();
        threadPool = Executors.newFixedThreadPool(processors);
    }
    
    /**
     * Obtient l'instance unique du gestionnaire de threads.
     * @return L'instance du gestionnaire de threads
     */
    public static synchronized ThreadManager getInstance() {
        if (instance == null) {
            instance = new ThreadManager();
        }
        return instance;
    }
    
    /**
     * Exécute une tâche en arrière-plan.
     * @param task La tâche à exécuter
     */
    public void executeInBackground(Runnable task) {
        threadPool.execute(task);
    }
    
    /**
     * Exécute une tâche en arrière-plan et appelle une autre tâche sur l'EDT une fois terminée.
     * @param backgroundTask La tâche à exécuter en arrière-plan
     * @param onComplete La tâche à exécuter sur l'EDT une fois la tâche d'arrière-plan terminée
     */
    public void executeInBackgroundThenOnEDT(Runnable backgroundTask, Runnable onComplete) {
        threadPool.execute(() -> {
            backgroundTask.run();
            SwingUtilities.invokeLater(onComplete);
        });
    }
    
    /**
     * Exécute une tâche sur l'EDT.
     * @param task La tâche à exécuter
     */
    public void executeOnEDT(Runnable task) {
        SwingUtilities.invokeLater(task);
    }
    
    /**
     * Arrête le pool de threads.
     * À appeler lors de la fermeture de l'application.
     */
    public void shutdown() {
        threadPool.shutdown();
    }
}