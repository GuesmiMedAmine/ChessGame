package events;

import java.util.EventObject;

/**
 * Event that is fired when the user wants to start a new game.
 * This event can be used to notify the controller that a new game should be created.
 */
public class NewGameEvent extends EventObject {
    private final boolean isProfessional;
    
    /**
     * Creates a new NewGameEvent.
     * 
     * @param source The object that fired the event
     * @param isProfessional Whether the new game should be a professional game
     */
    public NewGameEvent(Object source, boolean isProfessional) {
        super(source);
        this.isProfessional = isProfessional;
    }
    
    /**
     * Returns whether the new game should be a professional game.
     * 
     * @return true if the new game should be a professional game, false otherwise
     */
    public boolean isProfessional() {
        return isProfessional;
    }
}