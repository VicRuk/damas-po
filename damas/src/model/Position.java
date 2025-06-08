package model;

import java.io.Serializable;

public record Position(int row, int col) implements Serializable {
    public boolean dentroDoTabuleiro() { return row >= 0 && row < 8 && col >= 0 && col < 8; }
}
