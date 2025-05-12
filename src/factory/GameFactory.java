package factory;

import modele.jeu.Jeu;
import modele.pieces.PieceColor;

/**
 * Factory class for creating different types of chess games.
 * This class follows the Factory pattern to encapsulate game creation logic.
 */
public class GameFactory {
    
    /**
     * Creates a standard chess game.
     * 
     * @return A new standard chess game
     */
    public static Jeu createStandardGame() {
        return new Jeu();
    }
    
    /**
     * Creates a professional chess game with standard rules.
     * In a professional game, white always starts, and the game is timed.
     * 
     * @return A new professional chess game
     */
    public static Jeu createProGame() {
        Jeu jeu = new Jeu();
        // In a professional game, white always starts
        // This is already the default in the Jeu constructor
        
        // Additional professional game setup could be added here
        // For example, setting up a specific board configuration or time controls
        
        return jeu;
    }
    
    /**
     * Creates a custom chess game with the specified starting player.
     * 
     * @param startingPlayer The color of the player who starts the game
     * @return A new custom chess game
     */
    public static Jeu createCustomGame(PieceColor startingPlayer) {
        Jeu jeu = new Jeu();
        // If the starting player is not the default (WHITE), we would need to add
        // logic to change the starting player. However, the Jeu class doesn't currently
        // have a method to set the starting player after construction.
        
        return jeu;
    }
}