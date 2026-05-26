package com.project.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GPUUpgradesGUI extends JPanel {

    private static final Color BG = new Color(8, 8, 12);
    private static final Color CARD = new Color(20, 20, 28);
    private static final Color BORDER = new Color(35, 35, 50);
    private static final Color TEXT = new Color(245, 245, 250);
    private static final Color MUTED = new Color(120, 120, 145);

    private DefaultTableModel tableModel;

    public GPUUpgradesGUI() {
        setLayout(new BorderLayout(0, 16));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("GPU Upgrades");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT);

        tableModel = new DefaultTableModel(
            new String[]{"ID", "Brand", "Name", "For Budget", "Price Increase", "Score Increase"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(28);
        table.setBackground(CARD);
        table.setForeground(TEXT);
        table.setGridColor(BORDER);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.getTableHeader().setBackground(CARD);
        table.getTableHeader().setForeground(MUTED);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));

        add(title, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }
}
