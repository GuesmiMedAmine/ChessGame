package vue.console;

import events.GameEndListener;
import events.NewGameListener;
import events.RedoListener;
import events.UndoListener;
import modele.jeu.Jeu;
import modele.pieces.Piece;
import modele.pieces.PieceColor;
import modele.pieces.PieceType;
import modele.plateau.Case;
import modele.plateau.Plateau;
import vue.IView;

import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Vue console du plateau d'échecs, observe le modèle et affiche l'état du jeu dans la console.
 * Implémente l'interface IView et Observer.
 */
public class ConsoleView implements IView, Observer {
    private final Jeu jeu;
    private Case selectedCase;
    private List<Case> validMoves;
    
    // Gestion des événements
    private final List<GameEndListener> gameEndListeners;
    private final List<NewGameListener> newGameListeners;
    private final List<UndoListener> undoListeners;
    private final List<RedoListener> redoListeners;
    
    /**
     * Crée une nouvelle vue console pour le jeu d'échecs.
     * 
     * @param jeu Le jeu à observer
     */
    public ConsoleView(Jeu jeu) {
        this.jeu = jeu;
        this.gameEndListeners = new ArrayList<>();
        this.newGameListeners = new ArrayList<>();
        this.undoListeners = new ArrayList<>();
        this.redoListeners = new ArrayList<>();
        
        // S'abonner aux mises à jour du plateau
        jeu.getPlateau().addObserver(this);
        
        // Afficher l'état initial du jeu
        displayBoard();
        System.out.println("C'est au tour des " + (jeu.getJoueurActuel() == PieceColor.WHITE ? "Blancs" : "Noirs") + " de jouer.");
    }
    
    /**
     * Affiche le plateau d'échecs dans la console.
     */
    private void displayBoard() {
        Plateau plateau = jeu.getPlateau();
        
        System.out.println("\n  a b c d e f g h");
        System.out.println(" +-+-+-+-+-+-+-+-+");
        
        for (int y = 7; y >= 0; y--) {
            System.out.print((y + 1) + "|");
            
            for (int x = 0; x < 8; x++) {
                Case c = plateau.getCase(x, y);
                Piece p = c.getPiece();
                
                if (p == null) {
                    System.out.print(" |");
                } else {
                    char symbol = getPieceSymbol(p);
                    System.out.print(symbol + "|");
                }
            }
            
            System.out.println(" " + (y + 1));
            System.out.println(" +-+-+-+-+-+-+-+-+");
        }
        
        System.out.println("  a b c d e f g h\n");
        
        // Afficher les informations supplémentaires
        if (jeu.estEnEchec(PieceColor.WHITE)) {
            System.out.println("Les Blancs sont en échec!");
        }
        if (jeu.estEnEchec(PieceColor.BLACK)) {
            System.out.println("Les Noirs sont en échec!");
        }
        
        if (jeu.estPartieTerminee()) {
            PieceColor vainqueur = jeu.getVainqueur();
            if (vainqueur != null) {
                System.out.println("ÉCHEC ET MAT! Les " + (vainqueur == PieceColor.WHITE ? "Blancs" : "Noirs") + " ont gagné!");
            } else {
                System.out.println("PAT! La partie est nulle.");
            }
        }
    }
    
    /**
     * Retourne le symbole correspondant à une pièce.
     * 
     * @param p La pièce
     * @return Le symbole de la pièce
     */
    private char getPieceSymbol(Piece p) {
        char symbol;
        
        switch (p.getType()) {
            case ROI:
                symbol = 'R';
                break;
            case DAME:
                symbol = 'D';
                break;
            case TOUR:
                symbol = 'T';
                break;
            case FOU:
                symbol = 'F';
                break;
            case CAVALIER:
                symbol = 'C';
                break;
            case PION:
                symbol = 'P';
                break;
            default:
                symbol = '?';
        }
        
        // Utiliser des minuscules pour les pièces noires
        if (p.getColor() == PieceColor.BLACK) {
            symbol = Character.toLowerCase(symbol);
        }
        
        return symbol;
    }
    
    /**
     * Affiche les coups possibles pour une case sélectionnée.
     * 
     * @param selected La case sélectionnée
     * @param moves Les coups possibles
     */
    private void displayValidMoves(Case selected, List<Case> moves) {
        if (selected == null || moves == null || moves.isEmpty()) {
            return;
        }
        
        System.out.println("Coups possibles pour " + getCaseNotation(selected) + ":");
        
        for (Case move : moves) {
            System.out.print(getCaseNotation(move) + " ");
        }
        
        System.out.println();
    }
    
    /**
     * Retourne la notation algébrique d'une case (ex: "e4").
     * 
     * @param c La case
     * @return La notation algébrique de la case
     */
    private String getCaseNotation(Case c) {
        char colonne = (char) ('a' + c.getX());
        int ligne = c.getY() + 1;
        return colonne + "" + ligne;
    }
    
    /**
     * Met à jour l'affichage lorsque le modèle change.
     */
    @Override
    public void update(Observable o, Object arg) {
        displayBoard();
        System.out.println("C'est au tour des " + (jeu.getJoueurActuel() == PieceColor.WHITE ? "Blancs" : "Noirs") + " de jouer.");
        
        if (selectedCase != null && validMoves != null) {
            displayValidMoves(selectedCase, validMoves);
        }
    }
    
    /**
     * Ajoute un écouteur de clics sur les cases.
     * Dans la vue console, cette méthode ne fait rien.
     */
    @Override
    public void addCaseClickListener(MouseListener listener) {
        // Ne fait rien dans la vue console
    }
    
    /**
     * Sélectionne une case et affiche les mouvements possibles.
     */
    @Override
    public void selectCase(Case selected, List<Case> moves) {
        this.selectedCase = selected;
        this.validMoves = moves;
        
        if (selected != null) {
            System.out.println("Case sélectionnée: " + getCaseNotation(selected));
            displayValidMoves(selected, moves);
        }
    }
    
    /**
     * Efface la sélection actuelle.
     */
    @Override
    public void clearSelection() {
        this.selectedCase = null;
        this.validMoves = null;
        System.out.println("Sélection effacée.");
    }
    
    /**
     * Démarre le timer pour le joueur actuel.
     * Dans la vue console, cette méthode affiche simplement un message.
     */
    @Override
    public void startTimer() {
        System.out.println("Timer démarré pour les " + (jeu.getJoueurActuel() == PieceColor.WHITE ? "Blancs" : "Noirs") + ".");
    }
    
    /**
     * Arrête le timer.
     * Dans la vue console, cette méthode affiche simplement un message.
     */
    @Override
    public void stopTimer() {
        System.out.println("Timer arrêté.");
    }
    
    /**
     * Change le joueur actif du timer.
     * Dans la vue console, cette méthode affiche simplement un message.
     */
    @Override
    public void switchTimerPlayer() {
        System.out.println("Timer passé aux " + (jeu.getJoueurActuel() == PieceColor.WHITE ? "Blancs" : "Noirs") + ".");
    }
    
    /**
     * Ajoute un écouteur pour les événements de fin de partie.
     */
    @Override
    public void addGameEndListener(GameEndListener listener) {
        gameEndListeners.add(listener);
    }
    
    /**
     * Retire un écouteur pour les événements de fin de partie.
     */
    @Override
    public void removeGameEndListener(GameEndListener listener) {
        gameEndListeners.remove(listener);
    }
    
    /**
     * Ajoute un écouteur pour les événements de nouvelle partie.
     */
    @Override
    public void addNewGameListener(NewGameListener listener) {
        newGameListeners.add(listener);
    }
    
    /**
     * Retire un écouteur pour les événements de nouvelle partie.
     */
    @Override
    public void removeNewGameListener(NewGameListener listener) {
        newGameListeners.remove(listener);
    }
    
    /**
     * Ajoute un écouteur pour les événements d'annulation.
     */
    @Override
    public void addUndoListener(UndoListener listener) {
        undoListeners.add(listener);
    }
    
    /**
     * Retire un écouteur pour les événements d'annulation.
     */
    @Override
    public void removeUndoListener(UndoListener listener) {
        undoListeners.remove(listener);
    }
    
    /**
     * Ajoute un écouteur pour les événements de rétablissement.
     */
    @Override
    public void addRedoListener(RedoListener listener) {
        redoListeners.add(listener);
    }
    
    /**
     * Retire un écouteur pour les événements de rétablissement.
     */
    @Override
    public void removeRedoListener(RedoListener listener) {
        redoListeners.remove(listener);
    }
}