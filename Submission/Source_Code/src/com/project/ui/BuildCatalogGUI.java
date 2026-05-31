package com.project.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.project.dao.BuildDAO;
import com.project.models.Build;

public class BuildCatalogGUI extends JPanel {

    private static final Color BG = new Color(8, 8, 12);
    private static final Color CARD = new Color(20, 20, 28);
    private static final Color BORDER = new Color(35, 35, 50);
    private static final Color TEXT = new Color(245, 245, 250);
    private static final Color MUTED = new Color(120, 120, 145);
    private static final Color EMBER = new Color(255, 80, 40);
    private static final Color MINT = new Color(0, 255, 170);
    private static final Color FIELD_BG = new Color(14, 14, 20);
    private static final Color FIELD_BORDER = new Color(40, 40, 55);

    private static final Color INTEL_BLUE = new Color(70, 130, 255);
    private static final Color AMD_RED = new Color(220, 20, 60);
    private static final Color NVIDIA_GREEN = new Color(50, 205, 50);
    private static final Color AMD_ORANGE = new Color(255, 140, 0);

    private final BuildDAO buildDAO = new BuildDAO();
    private DefaultTableModel tableModel;
    private JTable table;
    private JComboBox<String> cpuTypeCombo, gpuTypeCombo;
    private JTextField cpuModelField, gpuModelField;
    private JTextField ramField, storageField, psuField, priceField, scoreField;
    private JTextField searchField;
    private JButton addBtn, updateBtn, deleteBtn, clearBtn;
    private JLabel countLabel;
    private int selectedBuildId = -1;

    public BuildCatalogGUI() {
        setLayout(new BorderLayout());
        setBackground(BG);
        buildUI();
        loadBuilds();
    }

    private void buildUI() {
        JPanel header = buildHeader();
        JPanel main = new JPanel(new BorderLayout(12, 0));
        main.setBackground(BG);
        main.setBorder(BorderFactory.createEmptyBorder(0, 24, 22, 24));
        main.add(buildTablePanel(), BorderLayout.CENTER);
        main.add(buildFormPanel(), BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
        add(main, BorderLayout.CENTER);
    }

    private JPanel buildHeader() {
        JPanel h = new JPanel();
        h.setLayout(new BoxLayout(h, BoxLayout.Y_AXIS));
        h.setBackground(BG);
        h.setBorder(BorderFactory.createEmptyBorder(22, 24, 16, 24));

        JPanel row = new JPanel(new BorderLayout());
        row.setBackground(BG);
        row.setMaximumSize(new Dimension(9999, 40));

        JLabel title = new JLabel("BUILD CATALOG");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT);

        searchField = createField("Search by budget...");
        searchField.setPreferredSize(new Dimension(220, 36));
        searchField.setMaximumSize(new Dimension(220, 36));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filterByBudget(); }
            public void removeUpdate(DocumentEvent e) { filterByBudget(); }
            public void changedUpdate(DocumentEvent e) { filterByBudget(); }
        });

        row.add(title, BorderLayout.WEST);
        row.add(searchField, BorderLayout.EAST);

        countLabel = new JLabel("0 builds");
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLabel.setForeground(MUTED);

        h.add(row);
        h.add(countLabel);
        return h;
    }

    private JPanel buildTablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        String[] cols = {"ID", "CPU", "Type", "GPU", "GPU Type", "RAM", "Storage", "PSU", "Price", "Score"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(38);
        table.setBackground(CARD);
        table.setForeground(TEXT);
        table.setGridColor(BORDER);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setSelectionBackground(new Color(255, 80, 40, 60));
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(14, 14, 20));
        header.setForeground(MUTED);
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setPreferredSize(new Dimension(0, 36));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER));

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        table.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                if (!sel) {
                    String type = val != null ? val.toString() : "";
                    comp.setForeground(type.equalsIgnoreCase("Intel") ? INTEL_BLUE : AMD_RED);
                }
                return comp;
            }
        });

        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                if (!sel) {
                    String type = val != null ? val.toString() : "";
                    comp.setForeground(type.equalsIgnoreCase("Nvidia") ? NVIDIA_GREEN : AMD_ORANGE);
                }
                return comp;
            }
        });

        table.getColumnModel().getColumn(9).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, val, sel, foc, r, c);
                if (!sel && val != null) {
                    try {
                        int score = Integer.parseInt(val.toString());
                        int g = (score * 255) / 120;
                        int red = ((120 - score) * 255) / 120;
                        comp.setForeground(new Color(red, g, 0));
                    } catch (NumberFormatException e) {
                        comp.setForeground(TEXT);
                    }
                }
                return comp;
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) fillForm(row);
            }
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(CARD);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildFormPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG);
        wrapper.setPreferredSize(new Dimension(280, 0));

        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(BG);
        titleBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        JLabel formTitle = new JLabel("BUILD DETAILS");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        formTitle.setForeground(EMBER);
        titleBar.add(formTitle, BorderLayout.WEST);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(CARD);
        form.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(18, 16, 18, 16)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 10, 0);
        gbc.weightx = 1;

        cpuTypeCombo = new JComboBox<>(new String[]{"Intel", "AMD"});
        cpuTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cpuTypeCombo.setBackground(FIELD_BG);
        cpuTypeCombo.setForeground(TEXT);

        gpuTypeCombo = new JComboBox<>(new String[]{"Nvidia", "AMD"});
        gpuTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gpuTypeCombo.setBackground(FIELD_BG);
        gpuTypeCombo.setForeground(TEXT);

        cpuModelField = createField("e.g. Ryzen 7 7800X3D");
        gpuModelField = createField("e.g. RTX 4070 Ti");
        ramField = createField("e.g. 32GB DDR5");
        storageField = createField("e.g. 1TB NVMe SSD");
        psuField = createField("e.g. 750W Gold");
        priceField = createField("e.g. 185000 (50k-500k, multiples of 5k)");
        scoreField = createField("e.g. 92 (0-120)");

        int row = 0;
        addFormRow(form, gbc, row, "CPU Type", cpuTypeCombo); row += 2;
        addFormRow(form, gbc, row, "CPU Model", cpuModelField); row += 2;
        addFormRow(form, gbc, row, "GPU Type", gpuTypeCombo); row += 2;
        addFormRow(form, gbc, row, "GPU Model", gpuModelField); row += 2;
        addFormRow(form, gbc, row, "RAM", ramField); row += 2;
        addFormRow(form, gbc, row, "Storage", storageField); row += 2;
        addFormRow(form, gbc, row, "PSU", psuField); row += 2;
        addFormRow(form, gbc, row, "Total Price", priceField); row += 2;
        addFormRow(form, gbc, row, "Score", scoreField); row += 2;

        gbc.gridy = row++;
        gbc.weighty = 1;
        form.add(Box.createVerticalGlue(), gbc);

        gbc.weighty = 0;
        gbc.gridy = row++;
        JPanel btns = new JPanel();
        btns.setLayout(new BoxLayout(btns, BoxLayout.Y_AXIS));
        btns.setBackground(CARD);

        addBtn = createButton("Add Build", MINT);
        updateBtn = createButton("Update Build", new Color(50, 130, 255));
        deleteBtn = createButton("Delete Build", new Color(255, 50, 90));
        clearBtn = createButton("Clear Form", MUTED);

        addBtn.addActionListener(e -> addBuild());
        updateBtn.addActionListener(e -> updateBuild());
        deleteBtn.addActionListener(e -> deleteBuild());
        clearBtn.addActionListener(e -> clearForm());

        btns.add(addBtn);
        btns.add(updateBtn);
        btns.add(deleteBtn);
        btns.add(clearBtn);
        form.add(btns, gbc);

        wrapper.add(titleBar, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(form);
        sp.setBorder(BorderFactory.createEmptyBorder());
        sp.getViewport().setBackground(BG);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        wrapper.add(sp, BorderLayout.CENTER);
        return wrapper;
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, int row, String label, Component field) {
        gbc.gridy = row;
        gbc.weighty = 0;
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        l.setForeground(MUTED);
        p.add(l, gbc);
        gbc.gridy = row + 1;
        p.add(field, gbc);
    }

    private JTextField createField(String placeholder) {
        JTextField f = new JTextField() {
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
        f.setBackground(FIELD_BG);
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(FIELD_BORDER),
            BorderFactory.createEmptyBorder(7, 10, 7, 10)
        ));
        f.setPreferredSize(new Dimension(0, 34));
        f.setMaximumSize(new Dimension(9999, 34));
        return f;
    }

    private JButton createButton(String text, Color accent) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setForeground(BG);
        b.setBackground(accent);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(9999, 36));
        b.setPreferredSize(new Dimension(0, 36));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(accent.brighter());
            }
            public void mouseExited(MouseEvent e) {
                b.setBackground(accent);
            }
        });
        return b;
    }

    private void loadBuilds() {
        List<Build> builds = buildDAO.getAllBuilds();
        tableModel.setRowCount(0);
        for (Build b : builds) {
            tableModel.addRow(new Object[]{
                b.getBuildId(), b.getCpuModel(), b.getCpuType(),
                b.getGpuModel(), b.getGpuType(), b.getRam(),
                b.getStorage(), b.getPsu(), b.getTotalPrice(),
                b.getPerformanceScore()
            });
        }
        countLabel.setText(builds.size() + " builds");
    }

    private void filterByBudget() {
        String text = searchField.getText().trim();
        if (text.isEmpty()) {
            loadBuilds();
            return;
        }
        try {
            int budget = Integer.parseInt(text);
            List<Build> builds = buildDAO.getBuildsByBudget(budget);
            tableModel.setRowCount(0);
            for (Build b : builds) {
                tableModel.addRow(new Object[]{
                    b.getBuildId(), b.getCpuModel(), b.getCpuType(),
                    b.getGpuModel(), b.getGpuType(), b.getRam(),
                    b.getStorage(), b.getPsu(), b.getTotalPrice(),
                    b.getPerformanceScore()
                });
            }
            countLabel.setText(builds.size() + " builds (max Rs." + budget + ")");
        } catch (NumberFormatException ex) {
            loadBuilds();
        }
    }

    private void fillForm(int row) {
        selectedBuildId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        cpuTypeCombo.setSelectedItem(tableModel.getValueAt(row, 2).toString());
        cpuModelField.setText(tableModel.getValueAt(row, 1).toString());
        gpuTypeCombo.setSelectedItem(tableModel.getValueAt(row, 4).toString());
        gpuModelField.setText(tableModel.getValueAt(row, 3).toString());
        ramField.setText(tableModel.getValueAt(row, 5).toString());
        storageField.setText(tableModel.getValueAt(row, 6).toString());
        psuField.setText(tableModel.getValueAt(row, 7).toString());
        priceField.setText(tableModel.getValueAt(row, 8).toString());
        scoreField.setText(tableModel.getValueAt(row, 9).toString());
    }

    private void addBuild() {
        Build b = buildFromForm();
        if (b == null) return;
        if (buildDAO.addBuild(b)) {
            JOptionPane.showMessageDialog(this, "Build added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadBuilds();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add build.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateBuild() {
        if (selectedBuildId == -1) {
            JOptionPane.showMessageDialog(this, "Select a build from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Build b = buildFromForm();
        if (b == null) return;
        b = new Build(selectedBuildId, b.getCpuModel(), b.getCpuType(), b.getGpuModel(),
            b.getGpuType(), b.getRam(), b.getStorage(), b.getPsu(),
            b.getTotalPrice(), b.getPerformanceScore());
        if (buildDAO.updateBuild(b)) {
            JOptionPane.showMessageDialog(this, "Build updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadBuilds();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update build.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteBuild() {
        if (selectedBuildId == -1) {
            JOptionPane.showMessageDialog(this, "Select a build from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this build?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (buildDAO.deleteBuild(selectedBuildId)) {
                JOptionPane.showMessageDialog(this, "Build deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                loadBuilds();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete build.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        cpuTypeCombo.setSelectedIndex(0);
        cpuModelField.setText("");
        gpuTypeCombo.setSelectedIndex(0);
        gpuModelField.setText("");
        ramField.setText("");
        storageField.setText("");
        psuField.setText("");
        priceField.setText("");
        scoreField.setText("");
        selectedBuildId = -1;
        table.clearSelection();
    }

    private Build buildFromForm() {
        String cpuModel = cpuModelField.getText().trim();
        String cpuType = (String) cpuTypeCombo.getSelectedItem();
        String gpuModel = gpuModelField.getText().trim();
        String gpuType = (String) gpuTypeCombo.getSelectedItem();
        String ram = ramField.getText().trim();
        String storage = storageField.getText().trim();
        String psu = psuField.getText().trim();
        String priceText = priceField.getText().trim();
        String scoreText = scoreField.getText().trim();

        if (cpuModel.isEmpty() || gpuModel.isEmpty() || ram.isEmpty()
                || storage.isEmpty() || psu.isEmpty() || priceText.isEmpty() || scoreText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            int price = Integer.parseInt(priceText);
            if (price < 50000 || price > 500000 || price % 5000 != 0) {
                JOptionPane.showMessageDialog(this, "Price must be between 50,000-500,000 in multiples of 5,000.", "Validation", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            int score = Integer.parseInt(scoreText);
            if (score < 0 || score > 120) {
                JOptionPane.showMessageDialog(this, "Score must be between 0-120.", "Validation", JOptionPane.WARNING_MESSAGE);
                return null;
            }

            return new Build(0, cpuModel, cpuType, gpuModel, gpuType, ram, storage, psu, price, score);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price and Score must be numbers.", "Validation", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }
}
