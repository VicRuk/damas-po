package model;

import enums.Color;
import enums.PieceType;

public class Peao extends Piece {
    public Peao(Color c) { super(c, PieceType.PEAO); }

    @Override
    public boolean permiteDirecao(int dRow) {
        return (getColor() == Color.BRANCA && dRow < 0) ||
                (getColor() == Color.PRETA  && dRow > 0);
    }
}
