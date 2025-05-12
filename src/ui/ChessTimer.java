package ui;

import modele.pieces.PieceColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

/**
 * A reusable chess timer component that displays and manages time for both players.
 * Each player has 1 minute (60 seconds) by default.
 */
public class ChessTimer extends JPanel implements Observer {
    private static final int DEFAULT_TIME = 60; // 60 seconds = 1 minute
    
    private final JLabel whiteTimeLabel;
    private final JLabel blackTimeLabel;
    private final Timer timer;
    
    private int whiteTimeRemaining;
    private int blackTimeRemaining;
    private PieceColor activePlayer;
    private boolean isRunning;
    
    /**
     * Creates a new chess timer with the default time (1 minute per player).
     */
    public ChessTimer() {
        this(DEFAULT_TIME);
    }
    
    /**
     * Creates a new chess timer with the specified time per player.
     * 
     * @param secondsPerPlayer The number of seconds each player has
     */
    public ChessTimer(int secondsPerPlayer) {
        setLayout(new GridLayout(1, 2, 10, 0));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Initialize time values
        whiteTimeRemaining = secondsPerPlayer;
        blackTimeRemaining = secondsPerPlayer;
        activePlayer = PieceColor.WHITE; // White starts
        isRunning = false;
        
        // Create labels with initial time display
        whiteTimeLabel = createTimeLabel("White: " + formatTime(whiteTimeRemaining));
        blackTimeLabel = createTimeLabel("Black: " + formatTime(blackTimeRemaining));
        
        // Add labels to panel
        add(whiteTimeLabel);
        add(blackTimeLabel);
        
        // Create timer that fires every second
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTime();
            }
        });
    }
    
    /**
     * Creates a formatted time label.
     */
    private JLabel createTimeLabel(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return label;
    }
    
    /**
     * Updates the time for the active player and updates the display.
     */
    private void updateTime() {
        if (activePlayer == PieceColor.WHITE) {
            whiteTimeRemaining--;
            whiteTimeLabel.setText("White: " + formatTime(whiteTimeRemaining));
            
            // Highlight active player
            whiteTimeLabel.setBackground(new Color(255, 255, 200));
            whiteTimeLabel.setOpaque(true);
            blackTimeLabel.setOpaque(false);
            
            if (whiteTimeRemaining <= 0) {
                timeUp(PieceColor.WHITE);
            }
        } else {
            blackTimeRemaining--;
            blackTimeLabel.setText("Black: " + formatTime(blackTimeRemaining));
            
            // Highlight active player
            blackTimeLabel.setBackground(new Color(255, 255, 200));
            blackTimeLabel.setOpaque(true);
            whiteTimeLabel.setOpaque(false);
            
            if (blackTimeRemaining <= 0) {
                timeUp(PieceColor.BLACK);
            }
        }
    }
    
    /**
     * Formats the time as MM:SS.
     */
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d", minutes, secs);
    }
    
    /**
     * Called when a player's time is up.
     */
    private void timeUp(PieceColor player) {
        stopTimer();
        String playerName = (player == PieceColor.WHITE) ? "White" : "Black";
        JOptionPane.showMessageDialog(this, 
                playerName + "'s time is up! " + 
                ((player == PieceColor.WHITE) ? "Black" : "White") + " wins!", 
                "Time's Up", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Starts the timer.
     */
    public void startTimer() {
        if (!isRunning) {
            timer.start();
            isRunning = true;
            
            // Highlight active player
            if (activePlayer == PieceColor.WHITE) {
                whiteTimeLabel.setBackground(new Color(255, 255, 200));
                whiteTimeLabel.setOpaque(true);
                blackTimeLabel.setOpaque(false);
            } else {
                blackTimeLabel.setBackground(new Color(255, 255, 200));
                blackTimeLabel.setOpaque(true);
                whiteTimeLabel.setOpaque(false);
            }
        }
    }
    
    /**
     * Stops the timer.
     */
    public void stopTimer() {
        if (isRunning) {
            timer.stop();
            isRunning = false;
            
            // Remove highlights
            whiteTimeLabel.setOpaque(false);
            blackTimeLabel.setOpaque(false);
            repaint();
        }
    }
    
    /**
     * Resets the timer to the default time.
     */
    public void resetTimer() {
        resetTimer(DEFAULT_TIME);
    }
    
    /**
     * Resets the timer to the specified time.
     * 
     * @param secondsPerPlayer The number of seconds each player has
     */
    public void resetTimer(int secondsPerPlayer) {
        stopTimer();
        whiteTimeRemaining = secondsPerPlayer;
        blackTimeRemaining = secondsPerPlayer;
        activePlayer = PieceColor.WHITE;
        whiteTimeLabel.setText("White: " + formatTime(whiteTimeRemaining));
        blackTimeLabel.setText("Black: " + formatTime(blackTimeRemaining));
    }
    
    /**
     * Switches the active player.
     */
    public void switchPlayer() {
        activePlayer = (activePlayer == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        
        // Highlight active player
        if (isRunning) {
            if (activePlayer == PieceColor.WHITE) {
                whiteTimeLabel.setBackground(new Color(255, 255, 200));
                whiteTimeLabel.setOpaque(true);
                blackTimeLabel.setOpaque(false);
            } else {
                blackTimeLabel.setBackground(new Color(255, 255, 200));
                blackTimeLabel.setOpaque(true);
                whiteTimeLabel.setOpaque(false);
            }
            repaint();
        }
    }
    
    /**
     * Sets the active player.
     * 
     * @param player The player to set as active
     */
    public void setActivePlayer(PieceColor player) {
        activePlayer = player;
        
        // Highlight active player
        if (isRunning) {
            if (activePlayer == PieceColor.WHITE) {
                whiteTimeLabel.setBackground(new Color(255, 255, 200));
                whiteTimeLabel.setOpaque(true);
                blackTimeLabel.setOpaque(false);
            } else {
                blackTimeLabel.setBackground(new Color(255, 255, 200));
                blackTimeLabel.setOpaque(true);
                whiteTimeLabel.setOpaque(false);
            }
            repaint();
        }
    }
    
    /**
     * Observer method to update the timer when the model changes.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof PieceColor) {
            setActivePlayer((PieceColor) arg);
        }
    }
}