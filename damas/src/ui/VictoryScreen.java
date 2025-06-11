package ui;

import enums.Color;
import javax.swing.*;
import java.awt.*;

public class VictoryScreen extends JFrame {
    private static final int SIZE = 640;
    public VictoryScreen(Color winner) {
        setTitle("Vitória!");
        setSize(SIZE, SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Determinar cores com base no vencedor
        java.awt.Color bgColor, textColor;
        String winnerText;

        if (winner == Color.BRANCA) {
            bgColor = new java.awt.Color(240, 217, 181);
            textColor = new java.awt.Color(101, 67, 33);
            winnerText = "BRANCAS";
        } else {
            bgColor = new java.awt.Color(50, 50, 50);     // Cinza escuro
            textColor = new java.awt.Color(220, 220, 220); // Cinza claro
            winnerText = "PRETAS";
        }

        getContentPane().setBackground(bgColor);

        JLabel victoryLabel = new JLabel("VITÓRIA DAS PEÇAS " + winnerText + "!");
        victoryLabel.setFont(new Font("Arial", Font.BOLD, 24));
        victoryLabel.setForeground(textColor);
        victoryLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton closeButton = new JButton("Fechar Jogo");
        closeButton.addActionListener(e -> System.exit(0));
        closeButton.setBackground(new java.awt.Color(120, 120, 120));
        closeButton.setForeground(java.awt.Color.WHITE);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);

        add(victoryLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}