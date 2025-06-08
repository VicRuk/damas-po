package model;

import enums.Color;
import java.io.Serializable;

public class Player implements Serializable {
    private final String name;
    private final Color  color;

    public Player(String name, Color color) {
        this.name  = name;
        this.color = color;
    }

    public String getName()  { return name; }
    public Color  getColor() { return color; }
}
