package model;

import enums.Color;
import enums.PieceType;
import exceptions.InvalidMoveException;
import exceptions.MandatoryCaptureException;

import java.io.Serializable;
import java.util.*;

/*
 Regras:
 - Captura obrigatória
 - Captura em cadeia
 - Promoção
*/

public class Board implements Serializable {

    private final Piece[][] grid = new Piece[8][8];

    public Board() { iniciarPecasPadrao(); }

    // Configurações iniciais
    private void iniciarPecasPadrao() {
        for (int r = 0; r < 3; r++)
            for (int c = 0; c < 8; c++)
                if ((r + c) % 2 == 1) grid[r][c] = new Peao(Color.PRETA);

        for (int r = 5; r < 8; r++)
            for (int c = 0; c < 8; c++)
                if ((r + c) % 2 == 1) grid[r][c] = new Peao(Color.BRANCA);
    }

    public Optional<Piece> at(Position p) {
        return p.dentroDoTabuleiro() ? Optional.ofNullable(grid[p.row()][p.col()]) : Optional.empty();
    }
    public void set(Position p, Piece piece) { grid[p.row()][p.col()] = piece; }

    // Movimento Principal
    // Faz o lance <from,to>.  Retorna {@code true} se houve captura

    public boolean move(Position from, Position to, Color turno)
            throws InvalidMoveException, MandatoryCaptureException {

        Piece piece = at(from).orElseThrow(() -> new InvalidMoveException("Não há peça na origem"));
        if (piece.getColor() != turno)
            throw new InvalidMoveException("Peça não pertence ao jogador da vez");

        boolean deveCapturar = !capturasDisponiveis(turno).isEmpty();
        boolean lanceEhCaptura = isCaptura(piece, from, to);

        if (deveCapturar && !lanceEhCaptura)
            throw new MandatoryCaptureException("Captura obrigatória disponível");

        if (!isMovimentoValido(piece, from, to))
            throw new InvalidMoveException("Movimento inválido");

        boolean capturou = aplicarMovimento(piece, from, to);

        // PROMOÇÃO
        if (piece.getType() == PieceType.PEAO &&
                ((piece.getColor() == Color.BRANCA && to.row() == 0) ||
                        (piece.getColor() == Color.PRETA  && to.row() == 7))) {
            piece.promote();
        }
        return capturou;
    }

    private boolean aplicarMovimento(Piece piece, Position from, Position to) {
        grid[to.row()][to.col()] = piece;
        grid[from.row()][from.col()] = null;

        int dR = to.row() - from.row();
        int dC = to.col() - from.col();

        // Peão
        if (piece.getType() == PieceType.PEAO && Math.abs(dR) == 2) {
            grid[from.row() + dR / 2][from.col() + dC / 2] = null;
            return true;
        }

        // Dama
        if (piece.getType() == PieceType.RAINHA) {
            int sR = Integer.signum(dR), sC = Integer.signum(dC);
            int r = from.row() + sR, c = from.col() + sC;
            while (r != to.row()) {
                if (grid[r][c] != null) { grid[r][c] = null; return true; }
                r += sR; c += sC;
            }
        }
        return false;
    }

    private boolean isCaptura(Piece p, Position from, Position to) {
        if (p.getType() == PieceType.PEAO) return Math.abs(from.row() - to.row()) == 2;

        // Dama – vê se há inimigo
        int sR = Integer.signum(to.row() - from.row());
        int sC = Integer.signum(to.col() - from.col());
        int r = from.row() + sR, c = from.col() + sC;
        while (r != to.row()) {
            if (grid[r][c] != null && grid[r][c].getColor() != p.getColor())
                return true;
            r += sR; c += sC;
        }
        return false;
    }

    private boolean isMovimentoValido(Piece p, Position from, Position to) {
        if (!to.dentroDoTabuleiro() || at(to).isPresent()) return false;
        int dR = to.row() - from.row(), dC = to.col() - from.col();
        if (Math.abs(dR) != Math.abs(dC)) return false;  // diagonal

        if (p.getType() == PieceType.RAINHA) return trajetoDamaValido(p, from, to);

        // Peão
        if (!p.permiteDirecao(dR)) return false;
        if (Math.abs(dR) == 1) return true;               // simples
        if (Math.abs(dR) == 2) {                          // captura
            Position mid = new Position(from.row() + dR / 2, from.col() + dC / 2);
            return at(mid).filter(m -> m.getColor() != p.getColor()).isPresent();
        }
        return false;
    }

    private boolean trajetoDamaValido(Piece p, Position from, Position to) {
        int sR = Integer.signum(to.row() - from.row());
        int sC = Integer.signum(to.col() - from.col());
        int r = from.row() + sR, c = from.col() + sC;
        boolean encontrouInimigo = false;

        while (r != to.row()) {
            if (grid[r][c] != null) {
                if (grid[r][c].getColor() == p.getColor()) return false;
                if (encontrouInimigo) return false;
                encontrouInimigo = true;
            }
            r += sR; c += sC;
        }
        return true;
    }

    // Capturas Disponíveis
    public List<Position> capturasDisponiveis(Color turno) {
        List<Position> list = new ArrayList<>();
        for (int r = 0; r < 8; r++)
            for (int c = 0; c < 8; c++) {
                Position pos = new Position(r, c);
                Piece p = at(pos).orElse(null);
                if (p != null && p.getColor() == turno &&
                        !destinosDeCaptura(p, pos).isEmpty())
                    list.add(pos);
            }
        return list;
    }

    // Destino válido
    public List<Position> destinosDeCaptura(Piece p, Position from) {
        List<Position> out = new ArrayList<>();
        int[] d = {-1, 1};

        if (p.getType() == PieceType.PEAO) {
            for (int dr : d)
                for (int dc : d) {
                    if (!p.permiteDirecao(dr)) continue;
                    Position mid  = new Position(from.row() + dr, from.col() + dc);
                    Position dest = new Position(from.row() + 2*dr, from.col() + 2*dc);
                    if (!dest.dentroDoTabuleiro()) continue;
                    if (at(dest).isPresent()) continue;
                    if (at(mid).filter(m -> m.getColor() != p.getColor()).isEmpty()) continue;
                    out.add(dest);
                }
        } else {
            // dama
            for (int dr : d)
                for (int dc : d) {
                    int r = from.row() + dr, c = from.col() + dc;
                    boolean foundEnemy = false;
                    while (true) {
                        Position cur = new Position(r, c);
                        if (!cur.dentroDoTabuleiro()) break;
                        if (at(cur).isPresent()) {
                            if (at(cur).get().getColor() == p.getColor()) break;
                            if (foundEnemy) break;
                            foundEnemy = true;
                        } else if (foundEnemy) {
                            out.add(cur);  // casa vazia depois de 1 inimigo
                        }
                        r += dr; c += dc;
                    }
                }
        }
        return out;
    }

    /* ----------------- Debug ------------------------------------------- */
    @Override public String toString() {
        StringBuilder sb = new StringBuilder("    0 1 2 3 4 5 6 7\n");
        for (int r = 0; r < 8; r++) {
            sb.append(r).append(" |");
            for (int c = 0; c < 8; c++) {
                sb.append(" ");
                sb.append(at(new Position(r, c))
                        .map(Board::charDaPeca)
                        .orElse(' '));
            }
            sb.append(" |\n");
        }
        return sb.toString();
    }
    private static char charDaPeca(Piece p) {
        return switch (p.getColor()) {
            case BRANCA -> p.getType()== PieceType.PEAO ? 'o' : 'O';
            case PRETA  -> p.getType()== PieceType.PEAO ? 'x' : 'X';
        };
    }
}
