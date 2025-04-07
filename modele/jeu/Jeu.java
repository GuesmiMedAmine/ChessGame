package modele.jeu;

import modele.plateau.Plateau;
import modele.plateau.Case;

import java.util.ArrayList;
import java.util.List;

public class Jeu {
    private Plateau plateau;
    private PieceColor joueurCourant;
    private List<String> historiqueCoups;
    private String coupBuffer;
    private Thread boucleJeu;

    public Jeu() {
        plateau = new Plateau(8, 8);
        joueurCourant = PieceColor.BLANC;
        historiqueCoups = new ArrayList<>();
        initialiserPlateauComplet();
        startBoucleJeu();
    }

    private void initialiserPlateauComplet() {
        for (int x = 0; x < 8; x++) {
            plateau.setPiece(x, 1, new Piece(PieceType.PION, PieceColor.NOIR));
            plateau.setPiece(x, 6, new Piece(PieceType.PION, PieceColor.BLANC));
        }

        PieceType[] ordre = {PieceType.TOUR, PieceType.CAVALIER, PieceType.FOU, PieceType.DAME,
                PieceType.ROI, PieceType.FOU, PieceType.CAVALIER, PieceType.TOUR};

        for (int x = 0; x < 8; x++) {
            plateau.setPiece(x, 0, new Piece(ordre[x], PieceColor.NOIR));
            plateau.setPiece(x, 7, new Piece(ordre[x], PieceColor.BLANC));
        }
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public PieceColor getJoueurCourant() {
        return joueurCourant;
    }

    public void changerJoueur() {
        joueurCourant = (joueurCourant == PieceColor.BLANC) ? PieceColor.NOIR : PieceColor.BLANC;
    }

    public void setCoup(String coup) {
        historiqueCoups.add(coup);
    }

    public String getCoup() {
        if (historiqueCoups.isEmpty()) return null;
        return historiqueCoups.get(historiqueCoups.size() - 1);
    }

    public List<String> getHistoriqueCoups() {
        return historiqueCoups;
    }

    public void setCoupBuffer(String coup) {
        this.coupBuffer = coup;
    }

    private void startBoucleJeu() {
        boucleJeu = new Thread(() -> {
            while (true) {
                if (coupBuffer != null) {
                    traiterCoup(coupBuffer);
                    coupBuffer = null;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        boucleJeu.start();
    }

    private void traiterCoup(String coup) {
        if (coup.length() != 4) return;

        int sx = coup.charAt(0) - 'a';
        int sy = 8 - Character.getNumericValue(coup.charAt(1));
        int dx = coup.charAt(2) - 'a';
        int dy = 8 - Character.getNumericValue(coup.charAt(3));

        Case source = plateau.getCase(sx, sy);
        Case destination = plateau.getCase(dx, dy);

        demandeDeplacementPiece(source, destination);
    }

    public boolean demandeDeplacementPiece(Case source, Case destination) {
        Piece piece = source.getPiece();
        if (piece == null) return false;

        String coup = "" + (char)('a' + source.getX()) + (8 - source.getY())
                + (char)('a' + destination.getX()) + (8 - destination.getY());
        setCoup(coup);

        destination.setPiece(piece);
        source.setPiece(null);

        changerJoueur();
        return true;
    }
}
