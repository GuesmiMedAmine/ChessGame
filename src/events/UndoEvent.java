package events;

import java.util.EventObject;

/**
 * Event that is fired when the user wants to undo the last move.
 * This event can be used to notify the controller that the last move should be undone.
 */
public class UndoEvent extends EventObject {
    
    /**
     * Creates a new UndoEvent.
     * 
     * @param source The object that fired the event
     */
    public UndoEvent(Object source) {
        super(source);
    }
}