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

        public boolean jouerCoup (Case depart, Case arrivee){
            if (!depart.getPiece().getCasesAccessibles().contains(arrivee)) return false;

            // Exécution du coup
            Coup coup = new Coup(depart.getPiece(), arrivee);
            historique.add(coup);

            arrivee.setPiece(depart.getPiece());
            depart.setPiece(null);
            arrivee.getPiece().setPosition(arrivee.getX(), arrivee.getY());

            // Gestion spéciale prise en passant
            if (arrivee.getPiece() instanceof Pion) {
                ((Pion) arrivee.getPiece()).setPriseEnPassantPossible(false);
            }

            joueurActuel = (joueurActuel == PieceColor.WHITE) ?
                    PieceColor.BLACK : PieceColor.WHITE;

            return true;
        }
        public PieceColor getJoueurActuel() {
            return joueurActuel;
    }
    }
