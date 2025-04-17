package modele.jeu;

import modele.plateau.Plateau;

public class Piece {
    private int x, y;
    private final PieceType type;
    private final PieceColor color;

    public Piece(int x, int y, PieceType type, PieceColor color) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.color = color;
    }

    public boolean mouvementValide(int newX, int newY, Plateau plateau) {
        if (newX < 0 || newX >= Plateau.SIZE || newY < 0 || newY >= Plateau.SIZE) return false;

        // Vérification pièce alliée sur case destination
        Piece destinationPiece = plateau.getCase(newX, newY).getPiece();
        if (destinationPiece != null && destinationPiece.color == this.color) return false;

        int dx = Math.abs(newX - x);
        int dy = Math.abs(newY - y);

        return switch (type) {
            case ROI -> dx <= 1 && dy <= 1;
            case DAME -> deplacementLineaireValide(newX, newY, plateau) || deplacementDiagonalValide(newX, newY, plateau);
            case TOUR -> deplacementLineaireValide(newX, newY, plateau);
            case FOU -> deplacementDiagonalValide(newX, newY, plateau);
            case CAVALIER -> (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
            case PION -> validationPion(newX, newY, plateau);
        };
    }

    private boolean deplacementLineaireValide(int newX, int newY, Plateau plateau) {
        if (newX != x && newY != y) return false;
        int stepX = Integer.compare(newX - x, 0);
        int stepY = Integer.compare(newY - y, 0);

        int currentX = x + stepX;
        int currentY = y + stepY;

        while (currentX != newX || currentY != newY) {
            if (plateau.getCase(currentX, currentY).getPiece() != null) return false;
            currentX += stepX;
            currentY += stepY;
        }
        return true;
    }

    private boolean deplacementDiagonalValide(int newX, int newY, Plateau plateau) {
        if (Math.abs(newX - x) != Math.abs(newY - y)) return false;
        int stepX = Integer.compare(newX - x, 0);
        int stepY = Integer.compare(newY - y, 0);

        int currentX = x + stepX;
        int currentY = y + stepY;

        while (currentX != newX && currentY != newY) {
            if (plateau.getCase(currentX, currentY).getPiece() != null) return false;
            currentX += stepX;
            currentY += stepY;
        }
        return true;
    }

    private boolean validationPion(int newX, int newY, Plateau plateau) {
        int direction = color == PieceColor.BLANC ? 1 : -1;
        int startY = color == PieceColor.BLANC ? 1 : 6;

        // Déplacement avant
        if (newX == x && newY == y + direction) {
            return plateau.getCase(newX, newY).getPiece() == null;
        }

        // Premier déplacement (2 cases)
        if (y == startY && newX == x && newY == y + 2 * direction) {
            return plateau.getCase(x, y + direction).getPiece() == null
                    && plateau.getCase(newX, newY).getPiece() == null;
        }

        // Prise diagonale
        return Math.abs(newX - x) == 1 && newY == y + direction
                && plateau.getCase(newX, newY).getPiece() != null;
    }

    // Getters et setters
    public int getX() { return x; }
    public int getY() { return y; }
    public PieceType getType() { return type; }
    public PieceColor getColor() { return color; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
}
