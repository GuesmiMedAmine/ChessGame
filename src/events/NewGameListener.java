package events;

import java.util.EventListener;

/**
 * Interface for listeners that want to be notified when a new game event occurs.
 */
public interface NewGameListener extends EventListener {
    
    /**
     * Called when a new game event occurs.
     * 
     * @param event The new game event
     */
    void newGameRequested(NewGameEvent event);
}