package vue.graphique;

import events.GameEndEvent;
import events.GameEndListener;
import events.NewGameEvent;
import events.NewGameListener;
import events.UndoEvent;
import events.UndoListener;
import events.RedoEvent;
import events.RedoListener;
import modele.jeu.Jeu;
import modele.pieces.PieceColor;
import modele.plateau.Case;
import modele.plateau.Plateau;
import vue.IView;
import vue.VueBase;
import vue.components.ChessTimer;
import vue.components.NewGameButton;
import vue.components.UndoButton;
import vue.components.RedoButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Vue graphique du plateau d'échecs, observe le modèle et gère l'affichage.
 * Étend la classe de base VueBase et implémente l'interface IView.
 */
public class GraphicalView extends VueBase implements IView {
    private final Jeu jeu;

    // Sélection et coups possibles pour le highlight
    private Case selectedCase;
    private List<Case> validMoves;

    // Composants UI additionnels
    private final ChessTimer chessTimer;
    private final JButton endGameButton;
    private final NewGameButton newGameButton;
    private final UndoButton undoButton;
    private final RedoButton redoButton;
    private final JMenuBar menuBar;

    // Gestion des événements
    private final List<GameEndListener> gameEndListeners;
    private final List<NewGameListener> newGameListeners;
    private final List<UndoListener> undoListeners;
    private final List<RedoListener> redoListeners;

    public GraphicalView(Jeu jeu) {
        this.jeu = jeu;
        Plateau plateau = jeu.getPlateau();
        plateau.addObserver(this);

        // Initialiser la liste des listeners
        this.gameEndListeners = new ArrayList<>();
        this.newGameListeners = new ArrayList<>();
        this.undoListeners = new ArrayList<>();
        this.redoListeners = new ArrayList<>();

        // Créer le layout principal
        setLayout(new BorderLayout());

        // Créer le panneau d'échiquier
        JPanel chessboardPanel = new JPanel(new GridLayout(8, 8));
        initChessboard(chessboardPanel);
        add(chessboardPanel, BorderLayout.CENTER);

        // Créer le timer
        this.chessTimer = new ChessTimer();

        // Créer le bouton de fin de partie
        this.endGameButton = new JButton("Fin de partie");
        endGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireGameEndEvent();
            }
        });

        // Créer les boutons de nouvelle partie, undo et redo
        this.newGameButton = new NewGameButton();
        this.undoButton = new UndoButton();
        this.redoButton = new RedoButton();

        // Ajouter les listeners aux boutons
        newGameButton.addNewGameListener(new NewGameListener() {
            @Override
            public void newGameRequested(NewGameEvent event) {
                fireNewGameEvent(event.isProfessional());
            }
        });

        undoButton.addUndoListener(new UndoListener() {
            @Override
            public void undoRequested(UndoEvent event) {
                fireUndoEvent();
            }
        });

        redoButton.addRedoListener(new RedoListener() {
            @Override
            public void redoRequested(RedoEvent event) {
                fireRedoEvent();
            }
        });

        // Créer le panneau de contrôle (timer + boutons)
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(chessTimer, BorderLayout.CENTER);

        // Créer un panneau pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);
        buttonPanel.add(newGameButton);
        buttonPanel.add(endGameButton);

        controlPanel.add(buttonPanel, BorderLayout.EAST);
        add(controlPanel, BorderLayout.NORTH);

        // Créer la barre de menu
        this.menuBar = createMenuBar();

        // Mettre à jour l'affichage
        update(plateau, null);
    }

    /**
     * Initialise l'échiquier avec une grille de labels.
     */
    private void initChessboard(JPanel chessboardPanel) {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                JLabel lbl = new JLabel();
                lbl.setOpaque(true);
                lbl.setPreferredSize(new Dimension(80, 80));
                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setVerticalAlignment(SwingConstants.CENTER);
                lbl.putClientProperty("pos", new Point(x, y));
                grid[x][y] = lbl;
                chessboardPanel.add(lbl);
            }
        }
    }

    /**
     * Crée la barre de menu.
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu Partie
        JMenu gameMenu = new JMenu("Partie");

        // Option Nouvelle partie Pro
        JMenuItem newProGameItem = new JMenuItem("Nouvelle partie Pro");
        newProGameItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireNewGameEvent(true);
            }
        });
        gameMenu.add(newProGameItem);

        menuBar.add(gameMenu);
        return menuBar;
    }

    /**
     * Met à jour l'affichage : pièces, sélection et coups possibles.
     */
    @Override
    public void update(Observable o, Object arg) {
        Plateau p = (Plateau) o;

        // Mettre à jour les pièces en utilisant la méthode de la classe de base
        updatePieces(p);

        // Ajouter la surbrillance pour la sélection et les coups valides
        highlightSelection(p);

        revalidate();
        repaint();
    }

    /**
     * Ajoute la surbrillance pour la case sélectionnée, les coups valides,
     * et les rois en échec.
     */
    private void highlightSelection(Plateau p) {
        // Récupérer les cases des rois
        Case roiBlanc = p.getRoi(modele.pieces.PieceColor.WHITE);
        Case roiNoir = p.getRoi(modele.pieces.PieceColor.BLACK);

        // Vérifier si les rois sont en échec
        boolean roiBlancEnEchec = jeu.estEnEchec(modele.pieces.PieceColor.WHITE);
        boolean roiNoirEnEchec = jeu.estEnEchec(modele.pieces.PieceColor.BLACK);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                JLabel lbl = grid[x][y];
                Case c = p.getCase(x, y);

                // Réinitialiser le fond avec la couleur de base
                lbl.setBackground(getCaseColor(x, y));

                // Surbrillance sélection et coups valides
                if (selectedCase != null && selectedCase.equals(c)) {
                    lbl.setBackground(new Color(22, 116, 255, 255));
                } else if (validMoves != null && validMoves.contains(c)) {
                    lbl.setBackground(new Color(8, 63, 140, 255));
                }

                // Surbrillance rouge pour les rois en échec
                if ((roiBlancEnEchec && c.equals(roiBlanc)) || 
                    (roiNoirEnEchec && c.equals(roiNoir))) {
                    lbl.setBackground(new Color(255, 0, 0, 150));
                }

                lbl.setBorder(null);
            }
        }
    }

    /**
     * Définir la case sélectionnée et les déplacements valides à surligner.
     */
    @Override
    public void selectCase(Case selected, List<Case> moves) {
        this.selectedCase = selected;
        this.validMoves   = moves;
        highlightSelection(jeu.getPlateau());
        revalidate();
        repaint();
    }

    /**
     * Effacer la sélection et les surbrillances.
     */
    @Override
    public void clearSelection() {
        this.selectedCase = null;
        if (validMoves != null) validMoves.clear();
        repaint();
    }

    /**
     * Retourne la barre de menu.
     */
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    /**
     * Retourne le timer.
     */
    public ChessTimer getChessTimer() {
        return chessTimer;
    }

    /**
     * Démarre le timer pour le joueur actuel.
     */
    @Override
    public void startTimer() {
        chessTimer.setActivePlayer(jeu.getJoueurActuel());
        chessTimer.startTimer();
    }

    /**
     * Arrête le timer.
     */
    @Override
    public void stopTimer() {
        chessTimer.stopTimer();
    }

    /**
     * Change le joueur actif du timer.
     */
    @Override
    public void switchTimerPlayer() {
        chessTimer.switchPlayer();
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

    /**
     * Déclenche un événement de fin de partie.
     */
    private void fireGameEndEvent() {
        GameEndEvent event = new GameEndEvent(this);
        for (GameEndListener listener : gameEndListeners) {
            listener.gameEndRequested(event);
        }
    }

    /**
     * Déclenche un événement de nouvelle partie.
     * 
     * @param isProfessional true si la nouvelle partie doit être une partie professionnelle
     */
    private void fireNewGameEvent(boolean isProfessional) {
        NewGameEvent event = new NewGameEvent(this, isProfessional);
        for (NewGameListener listener : newGameListeners) {
            listener.newGameRequested(event);
        }
    }

    /**
     * Déclenche un événement d'annulation.
     */
    private void fireUndoEvent() {
        UndoEvent event = new UndoEvent(this);
        for (UndoListener listener : undoListeners) {
            listener.undoRequested(event);
        }
    }

    /**
     * Déclenche un événement de rétablissement.
     */
    private void fireRedoEvent() {
        RedoEvent event = new RedoEvent(this);
        for (RedoListener listener : redoListeners) {
            listener.redoRequested(event);
        }
    }
}