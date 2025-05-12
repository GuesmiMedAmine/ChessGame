package events;

import java.util.EventListener;

/**
 * Interface for listeners that want to be notified when a redo event occurs.
 */
public interface RedoListener extends EventListener {
    
    /**
     * Called when a redo event occurs.
     * 
     * @param event The redo event
     */
    void redoRequested(RedoEvent event);
}