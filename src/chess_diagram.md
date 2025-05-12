@startuml Chess Game Class Diagram

title Chess Game Class Diagram

' Package modele
package modele {
  ' Piece hierarchy
  package pieces {
    abstract class Piece {
      # int x, y
      # PieceColor color
      # PieceType type
      # Plateau plateau
      # Deco decorateur
      # String imagePath
      + List<Case> getCasesAccessibles()
      + int getX()
      + int getY()
      + PieceColor getColor()
      + PieceType getType()
      + String getImagePath()
      + Plateau getPlateau()
      + Deco getDecorateur()
      + Case getCurrentCase()
      + Case getCurrentCase(Plateau)
      + void setPosition(int, int)
      # abstract void initDecorateur()
      # void setImagePath()
    }
    
    enum PieceColor {
      WHITE
      BLACK
    }
    
    enum PieceType {
      ROI
      DAME
      TOUR
      FOU
      CAVALIER
      PION
    }
    
    class Roi extends Piece {
      - boolean hasMoved
      + boolean hasMoved()
      + void setHasMoved(boolean)
    }
    
    class Dame extends Piece {
    }
    
    class Tour extends Piece {
      - boolean hasMoved
      + boolean hasMoved()
      + void setHasMoved(boolean)
    }
    
    class Fou extends Piece {
    }
    
    class Cavalier extends Piece {
    }
    
    class Pion extends Piece {
      - boolean priseEnPassantPossible
      + boolean isPriseEnPassantPossible()
      + void setPriseEnPassantPossible(boolean)
    }
  }
  
  ' Decorator pattern
  package deco {
    abstract class AbstractDecorator extends Piece {
      # Piece wrapped
      + int getX()
      + int getY()
      + PieceColor getColor()
      + PieceType getType()
      + String getImagePath()
      + Plateau getPlateau()
      + void setPosition(int, int)
      # void initDecorateur()
      + abstract List<Case> getCasesAccessibles()
    }
    
    abstract class Deco extends AbstractDecorator {
      # List<Case> slideInDirections(int, int, List<Direction>)
      + abstract List<Case> getCasesAccessibles()
    }
    
    class DecoRoi extends Deco {
      - List<Case> getAdjacentSquares(int, int)
      - Case calculRoque(Roi, boolean)
      - List<Case> filtrerEchec(List<Case>, Roi)
      + boolean validerRoque(Roi, Case)
      + List<Case> getCasesAccessibles()
    }
    
    class DecoDame extends Deco {
      + List<Case> getCasesAccessibles()
    }
    
    class DecoTour extends Deco {
      + List<Case> getCasesAccessibles()
    }
    
    class DecoFou extends Deco {
      + List<Case> getCasesAccessibles()
    }
    
    class DecoCavalier extends Deco {
      + List<Case> getCasesAccessibles()
    }
    
    class DecoPion extends Deco {
      + boolean validerPriseEnPassant(Pion, Case)
      + boolean isPriseEnPassantCase(Pion, Case)
      + List<Case> getCasesAccessibles()
    }
  }
  
  ' Plateau
  package plateau {
    class Plateau extends Observable implements Cloneable {
      - Case[][] cases
      - List<Piece> pieces
      + void notifierObservers()
      + Case getCase(int, int)
      + Case getCaseRelative(Case, int, int)
      + List<Piece> getPieces()
      + boolean estEnEchec(PieceColor)
      + boolean estEnEchec(PieceColor, boolean)
      + Case getRoi(PieceColor)
      + Plateau clone()
      - void initPieces()
    }
    
    class Case {
      - int x, y
      - Piece piece
      + int getX()
      + int getY()
      + Piece getPiece()
      + void setPiece(Piece)
    }
    
    enum Direction {
      UP
      DOWN
      LEFT
      RIGHT
      UP_LEFT
      UP_RIGHT
      DOWN_LEFT
      DOWN_RIGHT
      + int dx
      + int dy
    }
  }
  
  ' Game logic
  package jeu {
    class Jeu {
      - Plateau plateau
      - List<Coup> historique
      - Stack<Command> commandesExecutees
      - Stack<Command> commandesAnnulees
      - PieceColor joueurActuel
      - boolean partieTerminee
      + Plateau getPlateau()
      + PieceColor getJoueurActuel()
      + boolean estEnEchec(PieceColor)
      + boolean estPartieTerminee()
      + void terminerPartie(PieceColor)
      + PieceColor getVainqueur()
      + boolean jouerCoup(Case, Case)
      + boolean annulerDernierCoup()
      + boolean refaireDernierCoup()
      + boolean estEchecEtMat(PieceColor)
      + boolean estPat(PieceColor)
      - String notationAlgebrique(Case)
    }
    
    class Coup {
      - Piece piece
      - Case depart
      - Case arrivee
      - Piece pieceCapturee
      - boolean roque
      - boolean priseEnPassant
      + Piece getPiece()
      + Case getDepart()
      + Case getArrivee()
      + Piece getPieceCapturee()
      + String toString()
    }
    
    class MoveValidator {
      + static boolean isValid(Piece, Case, Plateau)
      - static boolean simulerEchec(Piece, Case, Plateau)
      + static boolean isInCheck(Plateau, PieceColor)
      + static boolean hasLegalDefense(Plateau, PieceColor)
      + static boolean isCheckmate(Plateau, PieceColor)
      + static boolean isStalemate(Plateau, PieceColor)
    }
    
    ' Command pattern
    package command {
      interface Command {
        + void execute()
        + void undo()
      }
      
      class DeplacerPieceCommand implements Command {
        - Piece piece
        - Case depart
        - Case arrivee
        - Piece pieceCapturee
        + void execute()
        + void undo()
      }
      
      class RoqueCommand implements Command {
        - Roi roi
        - Tour tour
        - Case departRoi
        - Case arriveeRoi
        - Case departTour
        - Case arriveeTour
        - boolean petitRoque
        + void execute()
        + void undo()
      }
      
      class PriseEnPassantCommand implements Command {
        - Pion pion
        - Case depart
        - Case arrivee
        - Pion pionCapture
        - Case casePionCapture
        + void execute()
        + void undo()
      }
      
      class PromotionCommand implements Command {
        - Pion pion
        - Case depart
        - Case arrivee
        - Piece pieceCapturee
        - PieceType typePromotion
        - Piece piecePromue
        + void execute()
        + void undo()
      }
    }
  }
}

' Package vue
package vue {
  interface IView extends Observer {
    + void addCaseClickListener(MouseListener)
    + void selectCase(Case, List<Case>)
    + void clearSelection()
    + void startTimer()
    + void stopTimer()
    + void switchTimerPlayer()
    + void addGameEndListener(GameEndListener)
    + void removeGameEndListener(GameEndListener)
    + void addNewGameListener(NewGameListener)
    + void removeNewGameListener(NewGameListener)
    + void addUndoListener(UndoListener)
    + void removeUndoListener(UndoListener)
    + void addRedoListener(RedoListener)
    + void removeRedoListener(RedoListener)
  }
  
  abstract class VueBase extends JPanel implements Observer {
    # JLabel[][] grid
    # void initUI()
    + void addCaseClickListener(MouseListener)
    # Color getCaseColor(int, int)
    # void updatePieces(Plateau)
  }
  
  class Main {
    + {static} void main(String[])
    - {static} void launchGraphicalView()
    - {static} void launchConsoleView()
  }
  
  package graphique {
    class GraphicalView extends VueBase implements IView {
      - Jeu jeu
      - Case selectedCase
      - List<Case> validMoves
      - ChessTimer chessTimer
      - JButton endGameButton
      - NewGameButton newGameButton
      - UndoButton undoButton
      - RedoButton redoButton
      - JMenuBar menuBar
      - List<GameEndListener> gameEndListeners
      - List<NewGameListener> newGameListeners
      - List<UndoListener> undoListeners
      - List<RedoListener> redoListeners
      + void update(Observable, Object)
      - void highlightSelection(Plateau)
      + void selectCase(Case, List<Case>)
      + void clearSelection()
      + JMenuBar getMenuBar()
      + ChessTimer getChessTimer()
      + void startTimer()
      + void stopTimer()
      + void switchTimerPlayer()
      + void addGameEndListener(GameEndListener)
      + void removeGameEndListener(GameEndListener)
      + void addNewGameListener(NewGameListener)
      + void removeNewGameListener(NewGameListener)
      + void addUndoListener(UndoListener)
      + void removeUndoListener(UndoListener)
      + void addRedoListener(RedoListener)
      + void removeRedoListener(RedoListener)
      - void fireGameEndEvent()
      - void fireNewGameEvent(boolean)
      - void fireUndoEvent()
      - void fireRedoEvent()
      - void initChessboard(JPanel)
      - JMenuBar createMenuBar()
    }
  }
  
  package console {
    class ConsoleView implements IView, Observer {
      - Jeu jeu
      - Case selectedCase
      - List<Case> validMoves
      - List<GameEndListener> gameEndListeners
      - List<NewGameListener> newGameListeners
      - List<UndoListener> undoListeners
      - List<RedoListener> redoListeners
      - void displayBoard()
      - char getPieceSymbol(Piece)
      - void displayValidMoves(Case, List<Case>)
      - String getCaseNotation(Case)
      + void update(Observable, Object)
      + void addCaseClickListener(MouseListener)
      + void selectCase(Case, List<Case>)
      + void clearSelection()
      + void startTimer()
      + void stopTimer()
      + void switchTimerPlayer()
      + void addGameEndListener(GameEndListener)
      + void removeGameEndListener(GameEndListener)
      + void addNewGameListener(NewGameListener)
      + void removeNewGameListener(NewGameListener)
      + void addUndoListener(UndoListener)
      + void removeUndoListener(UndoListener)
      + void addRedoListener(RedoListener)
      + void removeRedoListener(RedoListener)
    }
  }
  
  package components {
    class ChessTimer extends JPanel implements Observer {
      - {static} int DEFAULT_TIME
      - JLabel whiteTimeLabel
      - JLabel blackTimeLabel
      - Timer timer
      - int whiteTimeRemaining
      - int blackTimeRemaining
      - PieceColor activePlayer
      - boolean isRunning
      - JLabel createTimeLabel(String)
      - void updateTime()
      - String formatTime(int)
      - void timeUp(PieceColor)
      + void startTimer()
      + void stopTimer()
      + void resetTimer()
      + void resetTimer(int)
      + void switchPlayer()
      + void setActivePlayer(PieceColor)
      + void update(Observable, Object)
    }
    
    class NewGameButton extends JButton {
      - List<NewGameListener> listeners
      - boolean isProfessional
      + void addNewGameListener(NewGameListener)
      + void removeNewGameListener(NewGameListener)
      - void fireNewGameEvent()
    }
    
    class UndoButton extends JButton {
      - List<UndoListener> listeners
      + void addUndoListener(UndoListener)
      + void removeUndoListener(UndoListener)
      - void fireUndoEvent()
    }
    
    class RedoButton extends JButton {
      - List<RedoListener> listeners
      + void addRedoListener(RedoListener)
      + void removeRedoListener(RedoListener)
      - void fireRedoEvent()
    }
  }
}

' Package controlleur
package controlleur {
  class Controlleur implements GameEndListener, NewGameListener, UndoListener, RedoListener {
    - Jeu jeu
    - IView vue
    - JFrame frame
    - boolean isGraphicalView
    + Controlleur()
    + Controlleur(boolean)
    + void setFrame(JFrame)
    + IView getVue()
    + void gameEndRequested(GameEndEvent)
    + void newGameRequested(NewGameEvent)
    + void startNewStandardGame()
    + void startNewProGame()
    + void undoRequested(UndoEvent)
    + void redoRequested(RedoEvent)
    - class CaseClickHandler extends MouseAdapter
  }
}

' Package events
package events {
  class GameEndEvent extends EventObject {
  }
  
  interface GameEndListener extends EventListener {
    + void gameEndRequested(GameEndEvent)
  }
  
  class NewGameEvent extends EventObject {
    - boolean isProfessional
    + boolean isProfessional()
  }
  
  interface NewGameListener extends EventListener {
    + void newGameRequested(NewGameEvent)
  }
  
  class UndoEvent extends EventObject {
  }
  
  interface UndoListener extends EventListener {
    + void undoRequested(UndoEvent)
  }
  
  class RedoEvent extends EventObject {
  }
  
  interface RedoListener extends EventListener {
    + void redoRequested(RedoEvent)
  }
}

' Package factory
package factory {
  class GameFactory {
    + {static} Jeu createStandardGame()
    + {static} Jeu createProGame()
    + {static} Jeu createCustomGame(PieceColor)
  }
}

' Package utils
package utils {
  class ThreadManager {
    - {static} ThreadManager instance
    - ExecutorService threadPool
    + {static} ThreadManager getInstance()
    + void executeInBackground(Runnable)
    + void executeInBackgroundThenOnEDT(Runnable, Runnable)
    + void executeOnEDT(Runnable)
    + void shutdown()
  }
}

' Relationships
Piece --> PieceColor
Piece --> PieceType
Piece --> Plateau
Piece --> Deco
Piece ..> Case

AbstractDecorator --> Piece

Deco --> Piece
Deco ..> Direction

DecoRoi --> Roi
DecoDame --> Dame
DecoTour --> Tour
DecoFou --> Fou
DecoCavalier --> Cavalier
DecoPion --> Pion

Plateau --> Case
Plateau --> Piece

Jeu --> Plateau
Jeu --> Coup
Jeu --> Command
Jeu --> PieceColor
Jeu ..> MoveValidator

MoveValidator ..> Piece
MoveValidator ..> Case
MoveValidator ..> Plateau
MoveValidator ..> PieceColor
MoveValidator ..> DecoRoi
MoveValidator ..> DecoPion

DeplacerPieceCommand --> Piece
DeplacerPieceCommand --> Case

RoqueCommand --> Roi
RoqueCommand --> Tour
RoqueCommand --> Case

PriseEnPassantCommand --> Pion
PriseEnPassantCommand --> Case

PromotionCommand --> Pion
PromotionCommand --> Case
PromotionCommand --> PieceType
PromotionCommand --> Piece

IView ..> Case
IView ..> GameEndListener
IView ..> NewGameListener
IView ..> UndoListener
IView ..> RedoListener

VueBase ..> Plateau
VueBase ..> Case

GraphicalView --> Jeu
GraphicalView --> Case
GraphicalView --> ChessTimer
GraphicalView --> NewGameButton
GraphicalView --> UndoButton
GraphicalView --> RedoButton
GraphicalView --> GameEndListener
GraphicalView --> NewGameListener
GraphicalView --> UndoListener
GraphicalView --> RedoListener

ConsoleView --> Jeu
ConsoleView --> Case
ConsoleView --> GameEndListener
ConsoleView --> NewGameListener
ConsoleView --> UndoListener
ConsoleView --> RedoListener

ChessTimer --> PieceColor

NewGameButton --> NewGameListener
UndoButton --> UndoListener
RedoButton --> RedoListener

Controlleur --> Jeu
Controlleur --> IView
Controlleur ..> GameFactory
Controlleur ..> ThreadManager

Main ..> Controlleur
Main ..> ThreadManager

@enduml