package modele.jeu;

import modele.plateau.Case;
import modele.plateau.Plateau;
import java.util.ArrayList;
import java.util.List;
import vuecontroleur.Deco;
public class Jeu {
    private Plateau plateau;
    private List<String> historique;
    private PieceColor joueurActuel;
    private String coupBuffer;

    public Jeu() {
        initialiserJeu();
    }

    private void initialiserJeu() {
        plateau = new Plateau();
        historique = new ArrayList<>();
        joueurActuel = PieceColor.BLANC;

        placerPiece(0, 0, PieceType.TOUR, PieceColor.BLANC);
        placerPiece(1, 0, PieceType.CAVALIER, PieceColor.BLANC);
        placerPiece(2, 0, PieceType.FOU, PieceColor.BLANC);
        placerPiece(3, 0, PieceType.DAME, PieceColor.BLANC);
        placerPiece(4, 0, PieceType.ROI, PieceColor.BLANC);
        placerPiece(5, 0, PieceType.FOU, PieceColor.BLANC);
        placerPiece(6, 0, PieceType.CAVALIER, PieceColor.BLANC);
        placerPiece(7, 0, PieceType.TOUR, PieceColor.BLANC);

        // Pions blancs (rangée 1)
        for (int x = 0; x < 8; x++) {
            placerPiece(x, 1, PieceType.PION, PieceColor.BLANC);
        }

        // Pions noirs (rangée 6)
        for (int x = 0; x < 8; x++) {
            placerPiece(x, 6, PieceType.PION, PieceColor.NOIR);
        }

        // Pièces noires (rangée 7)
        placerPiece(0, 7, PieceType.TOUR, PieceColor.NOIR);
        placerPiece(1, 7, PieceType.CAVALIER, PieceColor.NOIR);
        placerPiece(2, 7, PieceType.FOU, PieceColor.NOIR);
        placerPiece(3, 7, PieceType.DAME, PieceColor.NOIR);
        placerPiece(4, 7, PieceType.ROI, PieceColor.NOIR);
        placerPiece(5, 7, PieceType.FOU, PieceColor.NOIR);
        placerPiece(6, 7, PieceType.CAVALIER, PieceColor.NOIR);
        placerPiece(7, 7, PieceType.TOUR, PieceColor.NOIR);

        plateau.mettreAJour();
    }

    private void placerPiece(int x, int y, PieceType type, PieceColor color) {
        plateau.getCase(x, y).setPiece(new Piece(x, y, type, color));
    }

    public void deplacerPiece(Case depart, Case arrivee) {
        Piece piece = depart.getPiece();
        if (piece == null || piece.getColor() != joueurActuel) return;

        // Vérifier si le mouvement est valide
        if (piece.mouvementValide(arrivee.getX(), arrivee.getY(), plateau)) {
            // Effectuer le déplacement
            arrivee.setPiece(piece);
            depart.setPiece(null);
            piece.setPosition(arrivee.getX(), arrivee.getY());

            // Changer de joueur
            joueurActuel = (joueurActuel == PieceColor.BLANC)
                    ? PieceColor.NOIR : PieceColor.BLANC;

            // Mettre à jour l'historique
            String notation = String.format("%s de %c%d à %c%d",
                    piece.getType(),
                    (char) ('a' + depart.getX()), depart.getY() + 1,
                    (char) ('a' + arrivee.getX()), arrivee.getY() + 1);
            historique.add(notation);

            // Affichage dans la console avec style (optionnel)
            System.out.println(Deco.prefixeLog() + " " + notation);

            // Notifier l'UI
            plateau.mettreAJour();
        }
    }
    /**
     * Stocke la représentation textuelle du coup saisi par l'utilisateur.
     */
    public void setCoupBuffer(String coup) {
        this.coupBuffer = coup;
    }

    /**
     * Récupère la représentation textuelle du dernier coup saisi.
     */
    public String getCoupBuffer() {
        return this.coupBuffer;
    }


    // Getters
    public Plateau getPlateau() { return plateau; }
    public PieceColor getJoueurActuel() { return joueurActuel; }
}

