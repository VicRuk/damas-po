package model;

import enums.Color;
import enums.PieceType;

public class Rainha extends Piece {
    public Rainha(Color c) { super(c, PieceType.RAINHA); }

    @Override
    public boolean permiteDirecao(int dRow) { return true; } // pode subir/ descer
}
