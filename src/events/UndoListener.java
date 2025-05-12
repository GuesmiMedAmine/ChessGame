package events;

import java.util.EventListener;

/**
 * Interface for listeners that want to be notified when an undo event occurs.
 */
public interface UndoListener extends EventListener {
    
    /**
     * Called when an undo event occurs.
     * 
     * @param event The undo event
     */
    void undoRequested(UndoEvent event);
}