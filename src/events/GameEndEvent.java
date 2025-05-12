package events;

import java.util.EventObject;

/**
 * Event that is fired when the user wants to end the game.
 * This event can be used to notify the controller that the game should be terminated.
 */
public class GameEndEvent extends EventObject {
    
    /**
     * Creates a new GameEndEvent.
     * 
     * @param source The object that fired the event
     */
    public GameEndEvent(Object source) {
        super(source);
    }
}