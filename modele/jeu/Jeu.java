package modele.jeu;
import modele.pieces.PieceColor;
import modele.plateau.Case;
import modele.plateau.Plateau;
import modele.pieces.*;
import java.util.ArrayList;
import java.util.List;

public class Jeu {
    private final Plateau plateau;
    private final List<Coup> historique;
    private PieceColor joueurActuel;
    private final List<Piece> pieces;

    public Jeu() {
        plateau = new Plateau();
        historique = new ArrayList<>();
        pieces = new ArrayList<>();
        joueurActuel = PieceColor.WHITE;
        initialiserPieces();
    }



        private void initialiserPieces() {
// Pièces blanches
            pieces.add(new Tour(0, 0, PieceColor.WHITE, plateau));
            pieces.add(new Cavalier(1, 0, PieceColor.WHITE, plateau));
            pieces.add(new Fou(2, 0, PieceColor.WHITE, plateau));
            pieces.add(new Dame(3, 0, PieceColor.WHITE, plateau));
            pieces.add(new Roi(4, 0, PieceColor.WHITE, plateau));
            pieces.add(new Fou(5, 0, PieceColor.WHITE, plateau));
            pieces.add(new Cavalier(6, 0, PieceColor.WHITE, plateau));
            pieces.add(new Tour(7, 0, PieceColor.WHITE, plateau));
            for (int i = 0; i < 8; i++) {
                pieces.add(new Pion(i, 1, PieceColor.WHITE, plateau));
            }

// Pièces noires
            pieces.add(new Tour(0, 7, PieceColor.BLACK, plateau));
            pieces.add(new Cavalier(1, 7, PieceColor.BLACK, plateau));
            pieces.add(new Fou(2, 7, PieceColor.BLACK, plateau));
            pieces.add(new Dame(3, 7, PieceColor.BLACK, plateau));
            pieces.add(new Roi(4, 7, PieceColor.BLACK, plateau));
            pieces.add(new Fou(5, 7, PieceColor.BLACK, plateau));
            pieces.add(new Cavalier(6, 7, PieceColor.BLACK, plateau));
            pieces.add(new Tour(7, 7, PieceColor.BLACK, plateau));
            for (int i = 0; i < 8; i++) {
                pieces.add(new Pion(i, 6, PieceColor.BLACK, plateau));
            }

            // Placement dans le plateau
            for (Piece p : pieces) {
                Case c = plateau.getCase(p.getX(), p.getY());
                c.setPiece(p);
            }
        }

        public Plateau getPlateau() {
            return plateau;
        }

    public boolean jouerCoup(Case depart, Case arrivee) {
        if (depart.getPiece() == null) return false;
        Piece piece = depart.getPiece();

        // Validation globale avec MoveValidator
        if (!MoveValidator.isValid(piece, arrivee, plateau)) return false;

        // Vérifier les cas spéciaux
        boolean isRoque = false;
        boolean isEnPassant = false;

        if (piece instanceof Roi && Math.abs(arrivee.getX() - depart.getX()) == 2) {
            isRoque = MoveValidator.validerRoque((Roi) piece, arrivee, plateau);
        }
        else if (piece instanceof Pion) {
            isEnPassant = MoveValidator.validerPriseEnPassant((Pion) piece, arrivee, plateau);
        }

        // Création du coup avec les flags
        Coup coup = new Coup(piece, arrivee, isRoque, isEnPassant);
        historique.add(coup);

        // Exécution du mouvement spécial
        if (isRoque) {
            executerRoque((Roi) piece, arrivee);
        }
        else if (isEnPassant) {
            executerPriseEnPassant((Pion) piece, arrivee);
        }
        else {
            // Déplacement standard
            arrivee.setPiece(piece);
            depart.setPiece(null);
            piece.setPosition(arrivee.getX(), arrivee.getY());
        }

        // Gestion des états spéciaux
        if (piece instanceof Pion && Math.abs(arrivee.getY() - depart.getY()) == 2) {
            ((Pion) piece).setPriseEnPassantPossible(true);
        }

        // Log et changement de joueur
        System.out.println(coup);
        joueurActuel = (joueurActuel == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;
        return true;
    }

    private void executerRoque(Roi roi, Case arrivee) {
        int direction = arrivee.getX() == 6 ? 1 : -1; // 6 = roque droit, 2 = roque gauche
        int rookStartX = direction == 1 ? 7 : 0;
        int rookEndX = direction == 1 ? 5 : 3;

        Case rookStart = plateau.getCase(rookStartX, roi.getY());
        Tour tour = (Tour) rookStart.getPiece();

        // Déplacer le roi
        arrivee.setPiece(roi);
        plateau.getCase(roi.getX(), roi.getY()).setPiece(null);
        roi.setPosition(arrivee.getX(), arrivee.getY());

        // Déplacer la tour
        plateau.getCase(rookEndX, roi.getY()).setPiece(tour);
        rookStart.setPiece(null);
        tour.setPosition(rookEndX, roi.getY());
    }

    private void executerPriseEnPassant(Pion pion, Case arrivee) {
        // Capture du pion adverse
        int direction = pion.getColor() == PieceColor.WHITE ? -1 : 1;
        Case caseCapture = pion.getPlateau().getCase(arrivee.getX(), arrivee.getY() + direction);
        caseCapture.setPiece(null);

        // Déplacement du pion
        arrivee.setPiece(pion);
        pion.getPlateau().getCase(pion.getX(), pion.getY()).setPiece(null);
        pion.setPosition(arrivee.getX(), arrivee.getY());
    }
        public PieceColor getJoueurActuel() {
            return joueurActuel;
    }
    }
