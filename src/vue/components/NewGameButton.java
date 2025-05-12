package vue.components;

import events.NewGameEvent;
import events.NewGameListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A button that fires a NewGameEvent when clicked.
 * This button can be used to start a new game.
 */
public class NewGameButton extends JButton {
    
    private final List<NewGameListener> listeners;
    private final boolean isProfessional;
    
    /**
     * Creates a new NewGameButton with the default text "Nouvelle partie".
     */
    public NewGameButton() {
        this(false);
    }
    
    /**
     * Creates a new NewGameButton with the specified professional mode.
     * 
     * @param isProfessional Whether the new game should be a professional game
     */
    public NewGameButton(boolean isProfessional) {
        super("Nouvelle partie");
        this.listeners = new ArrayList<>();
        this.isProfessional = isProfessional;
        
        // Add action listener to fire event when clicked
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireNewGameEvent();
            }
        });
    }
    
    /**
     * Adds a listener to be notified when the button is clicked.
     * 
     * @param listener The listener to add
     */
    public void addNewGameListener(NewGameListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Removes a listener.
     * 
     * @param listener The listener to remove
     */
    public void removeNewGameListener(NewGameListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Fires a NewGameEvent to all registered listeners.
     */
    private void fireNewGameEvent() {
        NewGameEvent event = new NewGameEvent(this, isProfessional);
        for (NewGameListener listener : listeners) {
            listener.newGameRequested(event);
        }
    }
}