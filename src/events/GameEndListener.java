package events;

import java.util.EventListener;

/**
 * Interface for listeners that want to be notified when a game end event occurs.
 */
public interface GameEndListener extends EventListener {
    
    /**
     * Called when a game end event occurs.
     * 
     * @param event The game end event
     */
    void gameEndRequested(GameEndEvent event);
}