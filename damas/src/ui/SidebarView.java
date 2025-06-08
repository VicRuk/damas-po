package ui;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import model.*;

class SidebarView extends JPanel {
    private final ProfileBox blackBox;
    private final ProfileBox whiteBox;
    private final DefaultListModel<String> logModel = new DefaultListModel<>();

    SidebarView(GameState gs) {
        setBackground(new Color(0x31,0x2E,0x2B));
        setLayout(new BorderLayout());

        blackBox = new ProfileBox(gs.getBlackPlayer());
        whiteBox = new ProfileBox(gs.getWhitePlayer());

        JList<String> log = new JList<>(logModel);
        log.setBackground(new Color(40,37,34));
        log.setForeground(Color.WHITE);
        log.setFont(new Font(Font.MONOSPACED,Font.PLAIN,12));

        add(blackBox, BorderLayout.NORTH);
        add(new JScrollPane(log), BorderLayout.CENTER);
        add(whiteBox, BorderLayout.SOUTH);
    }
    void highlight(enums.Color turno) {
        blackBox.setPlaying(turno == enums.Color.PRETA);
        whiteBox.setPlaying(turno == enums.Color.BRANCA);
    }
    void addLog(Position f, Position t) {
        char cf = (char)('a'+f.col()), ct = (char)('a'+t.col());
        int rf = 8 - f.row(), rt = 8 - t.row();
        String jogada = cf+""+rf+"-"+ct+rt;
        logModel.addElement(jogada);
        appendCSV(jogada);
    }
    private void appendCSV(String line) {
        try {
            Path arq = Paths.get("data/movimentos.csv");
            Files.createDirectories(arq.getParent());
            Files.writeString(arq, line+System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (Exception e) { e.printStackTrace(); }
    }

    // DESENHAR
    private static class ProfileBox extends JPanel {
        private static final int PIC_SIZE = 60;
        ProfileBox(Player p){
            setLayout(new FlowLayout(FlowLayout.LEFT,10,10));
            setPreferredSize(new Dimension(260,90));
            setBackground(new Color(0x31,0x2E,0x2B));

            add(loadAvatar());
            JLabel name = new JLabel(p.getName());
            name.setForeground(Color.WHITE);
            name.setFont(name.getFont().deriveFont(Font.BOLD,16f));
            add(name);
        }
        private JLabel loadAvatar(){
            try {
                var url = ProfileBox.class.getResource("/padrao.png");
                Image img = (url != null)
                        ? new ImageIcon(url).getImage()
                        : ImageIO.read(Paths.get("data/padrao.png").toFile());
                return new JLabel(new ImageIcon(img.getScaledInstance(PIC_SIZE,PIC_SIZE,Image.SCALE_SMOOTH)));
            } catch (Exception e) { return new JLabel("[img]"); }
        }
        void setPlaying(boolean on) {
            setBackground(on ? new Color(0x7FB54A) : new Color(0x31,0x2E,0x2B));
        }
    }
}
