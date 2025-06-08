package model;

import enums.Color;
import enums.PieceType;
import java.io.Serializable;

public abstract class Piece implements Serializable {
    private final Color color;
    private PieceType type;

    protected Piece(Color color, PieceType type) {
        this.color = color;
        this.type  = type;
    }

    public Color getColor()         { return color; }
    public PieceType getType()      { return type; }

    public void promote()           { type = PieceType.RAINHA; }

    public abstract boolean permiteDirecao(int dRow);
}
