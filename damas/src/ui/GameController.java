package ui;

import model.*;
import enums.Color;

class GameController {
    private final GameState gs;
    private final BoardView board;
    private final SidebarView sidebar;

    private Position selected;      // peça escolhida
    private boolean  emCadeia;      // true se ainda deve capturar
    private Position chainStart;    // início da cadeia

    GameController(GameState gs, BoardView b, SidebarView s) {
        this.gs = gs; this.board = b; this.sidebar = s;
    }

    GameState getGameState()            { return gs; }
    boolean isSelected(int r,int c)     { return selected!=null && selected.row()==r && selected.col()==c; }
    boolean mustCapture(int r,int c) {
        return gs.getBoard().capturasDisponiveis(gs.getTurno())
                .stream().anyMatch(p->p.row()==r && p.col()==c);
    }

    // BoardView
    void onSquareClicked(int row,int col) {
        if (emCadeia) { tryMove(row,col); return; }

        if (selected == null)           { trySelect(row,col); return; }
        if (isSelected(row,col))        { selected = null; board.repaint(); return; }
        tryMove(row,col);
    }

    // Seleção da Peça
    private void trySelect(int r,int c) {
        Piece p = gs.getBoard().at(new Position(r,c)).orElse(null);
        if (p == null || p.getColor() != gs.getTurno()) return;

        boolean temObrig = gs.getBoard().capturasDisponiveis(gs.getTurno())
                .stream().anyMatch(pos -> pos.row()==r && pos.col()==c);
        if (!gs.getBoard().capturasDisponiveis(gs.getTurno()).isEmpty() && !temObrig) return;

        selected = new Position(r,c);
        board.repaint();
    }

    // Movimento
    private void tryMove(int r,int c) {
        if (selected == null) return;
        Position from = selected;
        Position to   = new Position(r,c);

        try {
            boolean cap = gs.getBoard().move(from, to, gs.getTurno());

            if (cap) {
                if (!emCadeia) chainStart = from;          // marca início
                emCadeia = !gs.getBoard()
                        .destinosDeCaptura(gs.getBoard().at(to).get(), to)
                        .isEmpty();
            }

            if (emCadeia) {
                selected = to;               // continua cadeia
            } else {
                // registra apenas UM lance
                Position origem = (chainStart != null) ? chainStart : from;
                sidebar.addLog(origem, to);
                chainStart = null;
                selected = null;
                gs.alternarTurno();
                sidebar.highlight(gs.getTurno());
            }
            board.repaint();
        } catch (Exception ignored) {}
    }
}
