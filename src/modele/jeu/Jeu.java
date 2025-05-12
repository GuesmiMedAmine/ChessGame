// src/main/java/modele/jeu/Jeu.java
package modele.jeu;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import modele.deco.DecoRoi;
import modele.deco.DecoPion;
import modele.jeu.command.*;
import modele.plateau.Plateau;
import modele.plateau.Case;
import modele.pieces.*;
import modele.pieces.PieceColor;
import java.util.Observable;
/**
 * Logique de la partie : historique, tour de jeu, validation, etc.
 * L'initialisation des pièces se fait désormais dans Plateau.
 * Utilise le pattern Command pour l'exécution des coups.
 */
public class Jeu {
    private final Plateau plateau;
    private final List<Coup> historique;
    private final Stack<Command> commandesExecutees;
    private PieceColor joueurActuel;
    private boolean partieTerminee;

    public Jeu() {
        this.plateau = new Plateau();
        this.historique = new ArrayList<>();
        this.commandesExecutees = new Stack<>();
        this.joueurActuel = PieceColor.WHITE;
        this.partieTerminee = false;
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public PieceColor getJoueurActuel() {
        return joueurActuel;
    }

    /**
     * Vérifie si le joueur spécifié est en échec.
     * @param couleur Couleur du joueur à vérifier
     * @return true si le joueur est en échec
     */
    public boolean estEnEchec(PieceColor couleur) {
        return plateau.estEnEchec(couleur);
    }

    /**
     * Vérifie si la partie est terminée (échec et mat, pat, ou manuellement).
     * @return true si la partie est terminée
     */
    public boolean estPartieTerminee() {
        return partieTerminee || estEchecEtMat(PieceColor.WHITE) || estEchecEtMat(PieceColor.BLACK) 
               || estPat(PieceColor.WHITE) || estPat(PieceColor.BLACK);
    }

    /**
     * Termine manuellement la partie.
     * @param vainqueur La couleur du vainqueur, ou null pour une partie nulle
     */
    public void terminerPartie(PieceColor vainqueur) {
        this.partieTerminee = true;
        // Notifier les observateurs
        plateau.notifierObservers();

        // Afficher le résultat dans la console
        if (vainqueur != null) {
            System.out.println("Partie terminée manuellement. " + vainqueur + " a gagné.");
        } else {
            System.out.println("Partie terminée manuellement. Match nul.");
        }
    }

    /**
     * Retourne le vainqueur de la partie, ou null si la partie n'est pas terminée ou est nulle.
     * @return La couleur du vainqueur, ou null
     */
    public PieceColor getVainqueur() {
        if (estEchecEtMat(PieceColor.WHITE)) return PieceColor.BLACK;
        if (estEchecEtMat(PieceColor.BLACK)) return PieceColor.WHITE;
        return null;
    }

    public boolean jouerCoup(Case depart, Case arrivee) {
        // Si la partie est déjà terminée, ne rien faire
        if (partieTerminee) return false;

        if (depart.getPiece() == null) return false;
        Piece piece = depart.getPiece();

        if (!MoveValidator.isValid(piece, arrivee, plateau)) return false;

        // Créer et exécuter la commande appropriée
        Command command = null;
        boolean isRoque = false, isEnPassant = false, isPromotion = false;

        // Vérifier si c'est un roque
        if (piece instanceof Roi && Math.abs(arrivee.getX() - depart.getX()) == 2) {
            DecoRoi decoRoi = (DecoRoi) ((Roi) piece).getDecorateur();
            isRoque = decoRoi.validerRoque((Roi) piece, arrivee);
            if (isRoque) {
                boolean petitRoque = arrivee.getX() > depart.getX();
                command = new RoqueCommand((Roi) piece, petitRoque);
            }
        } 
        // Vérifier si c'est une prise en passant
        else if (piece instanceof Pion && piece.getDecorateur() instanceof DecoPion) {
            DecoPion decoPion = (DecoPion) piece.getDecorateur();
            isEnPassant = decoPion.isPriseEnPassantCase((Pion) piece, arrivee) && 
                          decoPion.validerPriseEnPassant((Pion) piece, arrivee);
            if (isEnPassant) {
                command = new PriseEnPassantCommand((Pion) piece, arrivee);
            }
        }

        // Vérifier si c'est une promotion (pion atteignant la dernière rangée)
        if (piece instanceof Pion) {
            int lastRank = (piece.getColor() == PieceColor.WHITE) ? 7 : 0;
            if (arrivee.getY() == lastRank) {
                isPromotion = true;
                // Par défaut, promouvoir en dame
                command = new PromotionCommand((Pion) piece, arrivee, PieceType.DAME);
            }
        }

        // Si ce n'est pas un coup spécial, c'est un déplacement normal
        if (command == null) {
            command = new DeplacerPieceCommand(piece, arrivee);
        }

        // Exécuter la commande
        command.execute();
        commandesExecutees.push(command);

        // Enregistrer le coup dans l'historique
        Coup coup = new Coup(piece, arrivee, isRoque, isEnPassant);
        historique.add(coup);

        // Afficher le coup dans la console
        String typeMove = isRoque ? "roque" : (isEnPassant ? "prise en passant" : (isPromotion ? "promotion" : "déplacement"));
        System.out.println(joueurActuel + " joue : " + piece.getType() + " de " + 
                           notationAlgebrique(depart) + " à " + notationAlgebrique(arrivee) + 
                           " (" + typeMove + ")");

        // Marquer le pion comme pouvant être pris en passant au prochain tour
        if (piece instanceof Pion && Math.abs(arrivee.getY() - depart.getY()) == 2) {
            ((Pion) piece).setPriseEnPassantPossible(true);
        }

        PieceColor adversaire = joueurActuel;
        joueurActuel = (joueurActuel == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

        // Vérifier les conditions de fin de partie
        boolean estEnEchec = plateau.estEnEchec(adversaire);
        boolean estMat = estEchecEtMat(adversaire);
        boolean estPat = estPat(adversaire);

        // Afficher l'état du jeu dans la console
        if (estEnEchec) {
            System.out.println(adversaire + " est en échec!");
        }
        if (estMat) {
            System.out.println("ÉCHEC ET MAT! " + joueurActuel + " a gagné la partie!");
        } else if (estPat) {
            System.out.println("PAT! La partie est nulle.");
        }

        plateau.notifierObservers();
        return true;
    }

    /**
     * Annule le dernier coup joué.
     * @return true si un coup a été annulé, false sinon
     */
    public boolean annulerDernierCoup() {
        if (commandesExecutees.isEmpty()) {
            return false;
        }

        // Récupérer et annuler la dernière commande
        Command dernierCoup = commandesExecutees.pop();
        dernierCoup.undo();

        // Retirer le dernier coup de l'historique
        if (!historique.isEmpty()) {
            historique.remove(historique.size() - 1);
        }

        // Changer le joueur actuel
        joueurActuel = (joueurActuel == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

        // Notifier les observateurs
        plateau.notifierObservers();

        return true;
    }

    public boolean estEchecEtMat(PieceColor couleur) {
        if (!plateau.estEnEchec(couleur)) return false;
        return !aDesMouvementsValides(couleur);
    }

    public boolean estPat(PieceColor couleur) {
        if (plateau.estEnEchec(couleur)) return false;
        return !aDesMouvementsValides(couleur);
    }

    private boolean aDesMouvementsValides(PieceColor couleur) {
        for (Piece p : plateau.getPieces()) {
            if (p.getColor() == couleur) {
                Case origine = p.getCurrentCase();
                for (Case dest : p.getCasesAccessibles()) {
                    Piece backup = dest.getPiece();
                    dest.setPiece(p);
                    origine.setPiece(null);
                    boolean valide = !plateau.estEnEchec(couleur);
                    origine.setPiece(p);
                    dest.setPiece(backup);
                    if (valide) return true;
                }
            }
        }
        return false;
    }

    /**
     * Convertit une case en notation algébrique (ex: "e4").
     * @param c La case à convertir
     * @return La notation algébrique de la case
     */
    private String notationAlgebrique(Case c) {
        char colonne = (char) ('a' + c.getX());
        int ligne = c.getY() + 1;
        return colonne + "" + ligne;
    }
}
