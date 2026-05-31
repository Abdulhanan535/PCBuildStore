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

        brandFilter.addActionListener(e -> filterByBrand());

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

    private void filterByBrand() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            String brand = (String) brandFilter.getSelectedItem();
            List<GPUOption> gpus;
            if (brand.equals("All Brands")) {
                gpus = gpuDAO.getAllGPUs();
            } else {
                gpus = gpuDAO.getGPUsByBrand(brand);
            }
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

        addLabel(panel, gbc, 1, "Brand");
        JTextField brandField = createField("e.g. Nvidia");
        gbc.gridy = 2;
        panel.add(brandField, gbc);

        addLabel(panel, gbc, 3, "Model");
        JTextField nameField = createField("e.g. RTX 4070 Ti");
        gbc.gridy = 4;
        panel.add(nameField, gbc);

        addLabel(panel, gbc, 5, "For Budget");
        JTextField budgetField = createField("e.g. 100000");
        gbc.gridy = 6;
        panel.add(budgetField, gbc);

        addLabel(panel, gbc, 7, "Price Increase");
        JTextField priceField = createField("e.g. 25000");
        gbc.gridy = 8;
        panel.add(priceField, gbc);

        addLabel(panel, gbc, 9, "Performance Increase");
        JTextField scoreField = createField("e.g. 10");
        gbc.gridy = 10;
        panel.add(scoreField, gbc);

        JButton addBtn = new JButton("Add GPU");
        addBtn.setBackground(VIOLET);
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        gbc.gridy = 11;
        gbc.insets = new Insets(16, 0, 6, 0);
        panel.add(addBtn, gbc);

        addBtn.addActionListener(e -> addGPU(brandField, nameField, budgetField, priceField, scoreField));

        return panel;
    }

    private void addLabel(JPanel panel, GridBagConstraints gbc, int row, String text) {
        gbc.gridy = row;
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        label.setForeground(MUTED);
        panel.add(label, gbc);
    }

    private void addGPU(JTextField brandField, JTextField nameField, JTextField budgetField, 
                        JTextField priceField, JTextField scoreField) {
        try {
            String brand = brandField.getText().trim();
            String name = nameField.getText().trim();
            String budgetStr = budgetField.getText().trim();
            String priceStr = priceField.getText().trim();
            String scoreStr = scoreField.getText().trim();

            if (brand.isEmpty() || name.isEmpty() || budgetStr.isEmpty() || priceStr.isEmpty() || scoreStr.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this, "All fields are required");
                return;
            }

            int budget = Integer.parseInt(budgetStr);
            int price = Integer.parseInt(priceStr);
            int score = Integer.parseInt(scoreStr);

            if (price < 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Price cannot be negative");
                return;
            }

            GPUOption gpu = new GPUOption(0, brand, name, budget, price, score);
            if (gpuDAO.addGPU(gpu)) {
                javax.swing.JOptionPane.showMessageDialog(this, "GPU added successfully!");
                brandField.setText("");
                nameField.setText("");
                budgetField.setText("");
                priceField.setText("");
                scoreField.setText("");
                loadGPUData();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Failed to add GPU");
            }
        } catch (NumberFormatException ex) {
            javax.swing.JOptionPane.showMessageDialog(this, "Budget, Price, and Score must be numbers");
        }
    }

    private JTextField createField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                    g2.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2.setColor(MUTED);
                    g2.setFont(getFont());
                    g2.drawString(placeholder, getInsets().left + 2, getHeight() / 2 + getFont().getSize() / 3);
                    g2.dispose();
                }
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBackground(BG);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }
}
