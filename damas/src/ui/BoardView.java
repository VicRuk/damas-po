package ui;

import javax.swing.*;
import java.awt.*;
import model.*;
import enums.PieceType;

class BoardView extends JPanel {
    private static final int SIZE = 640;
    private GameController ctrl;
    private final SpriteSheet sprites;

    BoardView(SpriteSheet sprites) {
        this.sprites = sprites;
        setPreferredSize(new Dimension(SIZE,SIZE));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (ctrl == null) return;
                int sq = SIZE / 8;
                ctrl.onSquareClicked(e.getY()/sq, e.getX()/sq);
            }
        });
    }
    void setController(GameController c) { this.ctrl = c; }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (ctrl == null) return;

        int sq = SIZE / 8;
        GameState gs = ctrl.getGameState();

        for (int r=0; r<8; r++)
            for (int c=0; c<8; c++) {
                g.setColor((r+c)%2==1 ? new Color(0x7A995B) : new Color(0xEEEED3));
                g.fillRect(c*sq, r*sq, sq, sq);

                if (ctrl.isSelected(r,c)) {
                    g.setColor(new Color(0,255,0,120));
                    g.fillRect(c*sq,r*sq,sq,sq);
                } else if (ctrl.mustCapture(r,c)) {
                    g.setColor(new Color(255,0,0,60));
                    g.fillRect(c*sq,r*sq,sq,sq);
                }

                Piece p = gs.getBoard().at(new Position(r,c)).orElse(null);
                if (p != null) {
                    g.drawImage(sprites.get(p.getColor(), p.getType()), c*sq+4, r*sq+4, null);
                }
            }
    }
}
