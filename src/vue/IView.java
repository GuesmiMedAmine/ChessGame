package vue;

import events.GameEndListener;
import events.NewGameListener;
import events.RedoListener;
import events.UndoListener;
import modele.plateau.Case;
import modele.plateau.Plateau;

import java.awt.event.MouseListener;
import java.util.List;
import java.util.Observer;

/**
 * Interface commune pour toutes les vues du jeu d'échecs.
 * Définit les méthodes nécessaires pour interagir avec le contrôleur.
 */
public interface IView extends Observer {
    
    /**
     * Ajoute un écouteur de clics sur les cases.
     * 
     * @param listener L'écouteur à ajouter
     */
    void addCaseClickListener(MouseListener listener);
    
    /**
     * Sélectionne une case et affiche les mouvements possibles.
     * 
     * @param selected La case sélectionnée
     * @param moves Les mouvements possibles
     */
    void selectCase(Case selected, List<Case> moves);
    
    /**
     * Efface la sélection actuelle.
     */
    void clearSelection();
    
    /**
     * Démarre le timer pour le joueur actuel.
     */
    void startTimer();
    
    /**
     * Arrête le timer.
     */
    void stopTimer();
    
    /**
     * Change le joueur actif du timer.
     */
    void switchTimerPlayer();
    
    /**
     * Ajoute un écouteur pour les événements de fin de partie.
     * 
     * @param listener L'écouteur à ajouter
     */
    void addGameEndListener(GameEndListener listener);
    
    /**
     * Retire un écouteur pour les événements de fin de partie.
     * 
     * @param listener L'écouteur à retirer
     */
    void removeGameEndListener(GameEndListener listener);
    
    /**
     * Ajoute un écouteur pour les événements de nouvelle partie.
     * 
     * @param listener L'écouteur à ajouter
     */
    void addNewGameListener(NewGameListener listener);
    
    /**
     * Retire un écouteur pour les événements de nouvelle partie.
     * 
     * @param listener L'écouteur à retirer
     */
    void removeNewGameListener(NewGameListener listener);
    
    /**
     * Ajoute un écouteur pour les événements d'annulation.
     * 
     * @param listener L'écouteur à ajouter
     */
    void addUndoListener(UndoListener listener);
    
    /**
     * Retire un écouteur pour les événements d'annulation.
     * 
     * @param listener L'écouteur à retirer
     */
    void removeUndoListener(UndoListener listener);
    
    /**
     * Ajoute un écouteur pour les événements de rétablissement.
     * 
     * @param listener L'écouteur à ajouter
     */
    void addRedoListener(RedoListener listener);
    
    /**
     * Retire un écouteur pour les événements de rétablissement.
     * 
     * @param listener L'écouteur à retirer
     */
    void removeRedoListener(RedoListener listener);
}