package modele.plateau;

import modele.pieces.Piece;
import modele.pieces.PieceColor;
import modele.pieces.PieceType;

import java.util.Observable;

public class Plateau extends Observable {
    public static final int SIZE = 8;
    private final Case[][] cases;

    public Plateau() {
        cases = new Case[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++)
            for (int y = 0; y < SIZE; y++)
                cases[x][y] = new Case(x, y);
    }

    public Case getCase(int x, int y) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) return null;
        return cases[x][y];
    }

    public Case getCaseRelative(Case origine, int dx, int dy) {
        return getCase(origine.getX() + dx, origine.getY() + dy);
    }

    public boolean estEnEchec(PieceColor couleurJoueur) {
        Case roi = getRoi(couleurJoueur);
        if (roi == null) return false;

        // Vérifier toutes les pièces adverses
        PieceColor couleurAdverse = (couleurJoueur == PieceColor.WHITE) ?
                PieceColor.BLACK : PieceColor.WHITE;

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Case c = getCase(x, y);
                Piece p = c.getPiece();
                if (p != null && p.getColor() == couleurAdverse) {
                    if (p.getCasesAccessibles().contains(roi)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Case getRoi(PieceColor couleur) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Piece p = getCase(x, y).getPiece();
                if (p != null
                        && p.getType() == PieceType.ROI
                        && p.getColor() == couleur) {
                    return getCase(x, y);
                }
            }
        }
        return null; // Cas théorique (roi toujours présent)
    }

}