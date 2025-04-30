
package modele.pieces;

public enum PieceType { PION('P'), TOUR('R'), CAVALIER('N'), FOU('B'), DAME('Q'), ROI('K');
    private final char letter;
    PieceType(char l){ letter=l; }
    public char getLetter(){ return letter; }
}
