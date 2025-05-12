package events;

import java.util.EventObject;

/**
 * Event that is fired when the user wants to redo the last undone move.
 * This event can be used to notify the controller that the last undone move should be redone.
 */
public class RedoEvent extends EventObject {
    
    /**
     * Creates a new RedoEvent.
     * 
     * @param source The object that fired the event
     */
    public RedoEvent(Object source) {
        super(source);
    }
}