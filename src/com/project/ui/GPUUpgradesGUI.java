package com.project.ui;

import com.project.dao.GPUOptionDAO;
import com.project.models.GPUOption;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class GPUUpgradesGUI extends JPanel {

    private static final Color BG = new Color(8, 8, 12);
    private static final Color CARD = new Color(20, 20, 28);
    private static final Color BORDER = new Color(35, 35, 50);
    private static final Color TEXT = new Color(245, 245, 250);
    private static final Color MUTED = new Color(120, 120, 145);
    private static final Color VIOLET = new Color(140, 60, 255);

    private DefaultTableModel tableModel;
    private JComboBox<String> brandFilter;
    private GPUOptionDAO gpuDAO;

    public GPUUpgradesGUI() {
        gpuDAO = new GPUOptionDAO();
        setLayout(new BorderLayout(16, 0));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel title = new JLabel("GPU Upgrades");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT);

        brandFilter = new JComboBox<>(new String[]{"All Brands", "Nvidia", "AMD"});
        brandFilter.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        brandFilter.setBackground(CARD);
        brandFilter.setForeground(TEXT);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(title, BorderLayout.WEST);
        header.add(brandFilter, BorderLayout.EAST);

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
        scroll.getViewport().setBackground(CARD);

        add(header, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(createFormPanel(), BorderLayout.EAST);

        loadGPUData();
    }

    private void loadGPUData() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            List<GPUOption> gpus = gpuDAO.getAllGPUs();
            for (GPUOption g : gpus) {
                tableModel.addRow(new Object[]{
                    g.getGpuId(), g.getBrand(), g.getName(),
                    g.getForBudget(), g.getPriceIncrease(), g.getPerformanceIncrease()
                });
            }
        });
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CARD);
        panel.setPreferredSize(new Dimension(240, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(20, 16, 20, 16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel formTitle = new JLabel("Add GPU");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(TEXT);
        gbc.gridy = 0;
        panel.add(formTitle, gbc);

        JTextField brandField = createField();
        gbc.gridy = 1;
        panel.add(brandField, gbc);

        JTextField nameField = createField();
        gbc.gridy = 2;
        panel.add(nameField, gbc);

        JButton addBtn = new JButton("Add GPU");
        addBtn.setBackground(VIOLET);
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 3;
        gbc.insets = new Insets(16, 0, 6, 0);
        panel.add(addBtn, gbc);

        return panel;
    }

    private JTextField createField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(BG);
        field.setForeground(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }
}
