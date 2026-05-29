package com.project.ui;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
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
    private static final Color CARD_HOVER = new Color(28, 28, 38);
    private static final Color BORDER = new Color(35, 35, 50);
    private static final Color TEXT = new Color(245, 245, 250);
    private static final Color MUTED = new Color(120, 120, 145);
    private static final Color EMBER = new Color(255, 80, 40);
    private static final Color MINT = new Color(0, 255, 170);
    private static final Color FIELD_BG = new Color(14, 14, 20);
    private static final Color FIELD_BORDER = new Color(40, 40, 55);

    private final BuildDAO buildDAO = new BuildDAO();
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField cpuModelField, cpuTypeField, gpuModelField, gpuTypeField;
    private JTextField ramField, storageField, psuField, priceField, scoreField;
    private JTextField searchField;
    private JButton addBtn, updateBtn, deleteBtn, clearBtn;
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

        h.add(row);
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
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setBackground(BG);
        wrapper.setPreferredSize(new Dimension(280, 0));

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

        cpuModelField = createField("e.g. Ryzen 7 7800X3D");
        cpuTypeField = createField("e.g. AMD");
        gpuModelField = createField("e.g. RTX 4070 Ti");
        gpuTypeField = createField("e.g. NVIDIA");
        ramField = createField("e.g. 32GB DDR5");
        storageField = createField("e.g. 1TB NVMe SSD");
        psuField = createField("e.g. 750W Gold");
        priceField = createField("e.g. 185000");
        scoreField = createField("e.g. 92");

        int row = 0;
        addFormRow(form, gbc, row++, "CPU Model", cpuModelField);
        addFormRow(form, gbc, row++, "CPU Type", cpuTypeField);
        addFormRow(form, gbc, row++, "GPU Model", gpuModelField);
        addFormRow(form, gbc, row++, "GPU Type", gpuTypeField);
        addFormRow(form, gbc, row++, "RAM", ramField);
        addFormRow(form, gbc, row++, "Storage", storageField);
        addFormRow(form, gbc, row++, "PSU", psuField);
        addFormRow(form, gbc, row++, "Total Price", priceField);
        addFormRow(form, gbc, row++, "Score", scoreField);

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

        wrapper.add(form);
        return wrapper;
    }

    private void addFormRow(JPanel p, GridBagConstraints gbc, int row, String label, JTextField field) {
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
        JTextField f = new JTextField();
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
        } catch (NumberFormatException ex) {
            loadBuilds();
        }
    }

    private void fillForm(int row) {
        selectedBuildId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        cpuModelField.setText(tableModel.getValueAt(row, 1).toString());
        cpuTypeField.setText(tableModel.getValueAt(row, 2).toString());
        gpuModelField.setText(tableModel.getValueAt(row, 3).toString());
        gpuTypeField.setText(tableModel.getValueAt(row, 4).toString());
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
        cpuModelField.setText("");
        cpuTypeField.setText("");
        gpuModelField.setText("");
        gpuTypeField.setText("");
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
        String cpuType = cpuTypeField.getText().trim();
        String gpuModel = gpuModelField.getText().trim();
        String gpuType = gpuTypeField.getText().trim();
        String ram = ramField.getText().trim();
        String storage = storageField.getText().trim();
        String psu = psuField.getText().trim();
        String priceText = priceField.getText().trim();
        String scoreText = scoreField.getText().trim();

        if (cpuModel.isEmpty() || cpuType.isEmpty() || gpuModel.isEmpty() || gpuType.isEmpty()
                || ram.isEmpty() || storage.isEmpty() || psu.isEmpty()
                || priceText.isEmpty() || scoreText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Validation", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        try {
            int price = Integer.parseInt(priceText);
            int score = Integer.parseInt(scoreText);
            return new Build(0, cpuModel, cpuType, gpuModel, gpuType, ram, storage, psu, price, score);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Price and Score must be numbers.", "Validation", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }
}
