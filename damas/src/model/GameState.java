package model;

import enums.Color;
import java.io.Serializable;

public class GameState implements Serializable {
    private final Player branco;
    private final Player preto;
    private final Board  board = new Board();
    private enums.Color  turno = enums.Color.BRANCA;
    private boolean terminado  = false;

    public GameState(Player branco, Player preto) {
        this.branco = branco;
        this.preto  = preto;
    }

    public Player getWhitePlayer() { return branco; }
    public Player getBlackPlayer() { return preto;  }

    public Board  getBoard()       { return board;  }
    public enums.Color getTurno()  { return turno;  }
    public Player getJogadorDaVez(){ return turno==enums.Color.BRANCA ? branco : preto; }

    public void alternarTurno()    { turno = turno==enums.Color.BRANCA ? enums.Color.PRETA : enums.Color.BRANCA; }
    public boolean isTerminado()   { return terminado; }
    public void encerrar()         { terminado = true; }
}
