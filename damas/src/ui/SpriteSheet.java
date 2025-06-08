package ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.nio.file.Paths;
import java.util.EnumMap;
import enums.Color;
import enums.PieceType;

class SpriteSheet {
    private final EnumMap<Color, Image> peao = new EnumMap<>(Color.class);
    private final EnumMap<Color, Image> dama = new EnumMap<>(Color.class);

    SpriteSheet() throws Exception {
        load(Color.BRANCA, "data/white.png",  "data/qwhite.png");
        load(Color.PRETA , "data/black.png",  "data/qblack.png");
    }
    private void load(Color cor, String peaoPath, String damaPath) throws Exception {
        peao.put(cor, read(peaoPath));
        dama.put(cor, read(damaPath));
    }
    private Image read(String path) throws Exception {
        return ImageIO.read(Paths.get(path).toFile())
                .getScaledInstance(72, 72, Image.SCALE_SMOOTH);
    }
    Image get(Color cor, PieceType tipo) {
        return tipo == PieceType.PEAO ? peao.get(cor) : dama.get(cor);
    }
}
