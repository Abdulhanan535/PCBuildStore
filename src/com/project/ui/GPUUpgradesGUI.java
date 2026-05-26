package com.project.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GPUUpgradesGUI extends JPanel {

    private static final Color BG = new Color(8, 8, 12);
    private static final Color CARD = new Color(20, 20, 28);
    private static final Color BORDER = new Color(35, 35, 50);
    private static final Color TEXT = new Color(245, 245, 250);

    public GPUUpgradesGUI() {
        setLayout(new BorderLayout());
        setBackground(BG);

        JLabel title = new JLabel("GPU Upgrades");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 0));

        add(title, BorderLayout.NORTH);
    }
}
