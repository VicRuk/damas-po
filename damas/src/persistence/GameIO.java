package persistence;

import model.GameState;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public final class GameIO {

    private GameIO(){}

    // BINÁRIO
    public static void saveBinary(GameState gs, Path path) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(gs);
        }
    }
    public static GameState loadBinary(Path path) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            return (GameState) in.readObject();
        }
    }

    // CSV LOG
    public static void appendLog(Path csv, String linha) {
        try (BufferedWriter bw = Files.newBufferedWriter(csv,
                StandardCharsets.UTF_8, Files.exists(csv) ?
                        java.nio.file.StandardOpenOption.APPEND :
                        java.nio.file.StandardOpenOption.CREATE)) {
            bw.write(LocalDateTime.now() + ";" + linha + "\n");
        } catch (IOException e) { e.printStackTrace(); }
    }
}
