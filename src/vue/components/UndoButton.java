package vue.components;

import events.UndoEvent;
import events.UndoListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A button that fires an UndoEvent when clicked.
 * This button can be used to undo the last move in the game.
 */
public class UndoButton extends JButton {
    
    private final List<UndoListener> listeners;
    
    /**
     * Creates a new UndoButton with the text "Undo".
     */
    public UndoButton() {
        super("Undo");
        this.listeners = new ArrayList<>();
        
        // Add action listener to fire event when clicked
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireUndoEvent();
            }
        });
    }
    
    /**
     * Adds a listener to be notified when the button is clicked.
     * 
     * @param listener The listener to add
     */
    public void addUndoListener(UndoListener listener) {
        listeners.add(listener);
    }
    
    /**
     * Removes a listener.
     * 
     * @param listener The listener to remove
     */
    public void removeUndoListener(UndoListener listener) {
        listeners.remove(listener);
    }
    
    /**
     * Fires an UndoEvent to all registered listeners.
     */
    private void fireUndoEvent() {
        UndoEvent event = new UndoEvent(this);
        for (UndoListener listener : listeners) {
            listener.undoRequested(event);
        }
    }
}