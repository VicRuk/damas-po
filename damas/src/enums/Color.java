package enums;

public enum Color {
    BRANCA, PRETA;

    public Color adversaria() {
        return this == BRANCA ? PRETA : BRANCA;
    }
}
