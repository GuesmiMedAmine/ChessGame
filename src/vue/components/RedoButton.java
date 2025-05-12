package vue.components;

import events.RedoEvent;
import events.RedoListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A button that fires a RedoEvent when clicked.
 * This button can be used to redo the last undone move in the game.
 */
public class RedoButton extends JButton {
    
    private final List<RedoListener> listeners;
    
    /**
     * Creates a new RedoButton with the text "Redo".
     */
    public RedoButton() {
        super("Redo");
        this.listeners = new ArrayList<>();
        
        // Add action listener to fire event when clicked
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireRedoEvent();
            }
        });
    }
    
    /**
     * Adds a listener to be notified when the button is clicked.
     * 
     * @param listener The listener to add
     */
    public void addRedoListener(RedoListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Removes a listener.
     * 
     * @param listener The listener to remove
     */
    public void removeRedoListener(RedoListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Fires a RedoEvent to all registered listeners.
     */
    private void fireRedoEvent() {
        RedoEvent event = new RedoEvent(this);
        for (RedoListener listener : listeners) {
            listener.redoRequested(event);
        }
    }
}