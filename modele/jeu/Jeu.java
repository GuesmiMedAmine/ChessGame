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

        if (!MoveValidator.isValid(piece, arrivee, plateau)) return false;

        boolean isRoque = false;
        boolean isEnPassant = false;

        if (piece instanceof Roi && Math.abs(arrivee.getX() - depart.getX()) == 2) {
            isRoque = MoveValidator.validerRoque((Roi) piece, arrivee, plateau);
        }
        else if (piece instanceof Pion) {
            isEnPassant = MoveValidator.validerPriseEnPassant((Pion) piece, arrivee, plateau);
        }

        Coup coup = new Coup(piece, arrivee, isRoque, isEnPassant);
        historique.add(coup);

        // Ajout: Afficher le mouvement
        System.out.println("Joueur " + joueurActuel + " joue: " + coup);

        if (isRoque) {
            executerRoque((Roi) piece, arrivee);
        }
        else if (isEnPassant) {
            executerPriseEnPassant((Pion) piece, arrivee);
        }
        else {
            arrivee.setPiece(piece);
            depart.setPiece(null);
            piece.setPosition(arrivee.getX(), arrivee.getY());
        }

        if (piece instanceof Pion && Math.abs(arrivee.getY() - depart.getY()) == 2) {
            ((Pion) piece).setPriseEnPassantPossible(true);
        }

        PieceColor adversaire = joueurActuel;
        joueurActuel = (joueurActuel == PieceColor.WHITE) ? PieceColor.BLACK : PieceColor.WHITE;

        // Ajout: Vérification et affichage de l'échec simple
        if (plateau.estEnEchec(adversaire)) {
            System.out.println("Échec au joueur " + adversaire + " !");
        }

        if (estEchecEtMat(adversaire)) {
            System.out.println("Échec et mat ! Joueur " + adversaire + " perd.");
        }
        else if (estPat(adversaire)) {
            System.out.println("Pat ! Match nul.");
        }

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
    public boolean estEchecEtMat(PieceColor couleur) {
        // 1. Vérifier si le joueur est en échec
        if (!plateau.estEnEchec(couleur)) return false;

        // 2. Vérifier s'il existe au moins un coup légal
        return !aDesMouvementsValides(couleur);
    }

    public boolean estPat(PieceColor couleur) {
        // 1. Vérifier si le joueur n'est PAS en échec
        if (plateau.estEnEchec(couleur)) return false;

        // 2. Vérifier s'il n'a aucun coup légal
        return !aDesMouvementsValides(couleur);
    }

    private boolean aDesMouvementsValides(PieceColor couleur) {
        for (int x = 0; x < Plateau.SIZE; x++) {
            for (int y = 0; y < Plateau.SIZE; y++) {
                Case c = plateau.getCase(x, y);
                Piece p = c.getPiece();

                if (p != null && p.getColor() == couleur) {
                    for (Case destination : p.getCasesAccessibles()) {
                        // Simulation du mouvement
                        Piece backup = destination.getPiece();
                        destination.setPiece(p);
                        c.setPiece(null);

                        boolean estValide = !plateau.estEnEchec(couleur);

                        // Annulation simulation
                        c.setPiece(p);
                        destination.setPiece(backup);

                        if (estValide) return true;
                    }
                }
            }
        }
        return false;
    }
}

