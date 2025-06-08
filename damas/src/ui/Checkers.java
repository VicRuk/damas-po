package ui;

import model.GameState;
import persistence.GameIO;

import javax.swing.*;
import java.awt.BorderLayout;
import java.nio.file.Paths;

// P2
public class Checkers extends JFrame {

    public Checkers(GameState gs) throws Exception {
        super("Jogo de Damas");

        SpriteSheet  sprites = new SpriteSheet();
        SidebarView  sidebar = new SidebarView(gs);
        BoardView    board   = new BoardView(sprites);
        GameController ctrl  = new GameController(gs, board, sidebar);

        board.setController(ctrl);
        sidebar.highlight(gs.getTurno());

        setLayout(new BorderLayout());
        add(board,   BorderLayout.CENTER);
        add(sidebar, BorderLayout.EAST);

        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) throws Exception {
        GameState gs = GameIO.loadBinary(Paths.get("data/estado.bin"));
        SwingUtilities.invokeLater(() -> {
            try {
                new Checkers(gs).setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
