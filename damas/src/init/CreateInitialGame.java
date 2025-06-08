package init;

import enums.Color;
import exceptions.InvalidPlayersFileException;
import model.*;
import persistence.GameIO;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/*
 P1
 Formato do TXT: nome;COR   (COR = BRANCA ou PRETA)
*/

public class CreateInitialGame {

    public static void main(String[] args) {
        Path dataDir     = Paths.get("data");
        Path playersFile = dataDir.resolve("players.txt");
        Path binOut      = dataDir.resolve("estado.bin");

        try {
            List<Player> jogadores = lerJogadores(playersFile);
            if (jogadores.size() != 2)
                throw new InvalidPlayersFileException("Arquivo deve conter exatamente 2 linhas.");

            GameState gs = new GameState(jogadores.get(0), jogadores.get(1));
            GameIO.saveBinary(gs, binOut);
            System.out.println("Arquivo binário criado:\n" + binOut);

        } catch (IOException | InvalidPlayersFileException e) {
            System.err.println("Aviso: " + e.getMessage() +
                    "  – usando nomes padrão (Jogador Branco / Preto).");

            Player p1 = new Player("Jogador Branco", Color.BRANCA);
            Player p2 = new Player("Jogador Preto" , Color.PRETA );
            GameState gs = new GameState(p1, p2);

            try { GameIO.saveBinary(gs, binOut); }
            catch (IOException io) { io.printStackTrace(); }

            System.out.println("Arquivo binário criado com nomes padrão:\n" + binOut);
        }
    }

    private static List<Player> lerJogadores(Path file)
            throws IOException, InvalidPlayersFileException {

        List<String> linhas = Files.readAllLines(file, StandardCharsets.UTF_8);
        List<Player> out = new ArrayList<>();

        for (String linha : linhas) {
            out.add(parseLinha(linha));
        }
        return out;
    }

    // Converte "Nome;COR" em Player ou InvalidPlayersFileException
    private static Player parseLinha(String linha)
            throws InvalidPlayersFileException {

        String[] tok = linha.split(";");
        if (tok.length != 2)
            throw new InvalidPlayersFileException("Linha inválida: " + linha);

        String nome = tok[0].trim();
        String cor  = tok[1].trim().toUpperCase();

        try {
            return new Player(nome, Color.valueOf(cor));
        } catch (IllegalArgumentException e) {
            throw new InvalidPlayersFileException("Cor inválida na linha: " + linha);
        }
    }
}
