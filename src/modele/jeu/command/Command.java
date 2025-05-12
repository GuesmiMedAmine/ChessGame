package modele.jeu.command;

/**
 * Interface Command du pattern Command pour les coups d'échecs.
 * Chaque commande représente un coup qui peut être exécuté et annulé.
 */
public interface Command {
    /**
     * Exécute le coup
     */
    void execute();
    
    /**
     * Annule le coup
     */
    void undo();
}