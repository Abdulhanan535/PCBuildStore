package com.project.ui;

import com.project.dao.BillDAO;
import com.project.dao.BuildDAO;
import com.project.models.Bill;
import com.project.models.Build;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class BillingGUI extends JPanel {

    private static final Color BG = new Color(8, 8, 12);
    private static final Color CARD = new Color(20, 20, 28);
    private static final Color BORDER = new Color(35, 35, 50);
    private static final Color TEXT = new Color(245, 245, 250);
    private static final Color MUTED = new Color(120, 120, 145);
    private static final Color GREEN = new Color(0, 212, 170);
    private static final Color RED = new Color(220, 50, 60);
    private static final Color INDIGO = new Color(50, 130, 255);
    private static final Color INTEL_BLUE = new Color(70, 130, 255);
    private static final Color AMD_RED = new Color(220, 20, 60);
    private static final Color NVIDIA_GREEN = new Color(50, 205, 50);
    private static final Color AMD_ORANGE = new Color(255, 140, 0);

    private DefaultTableModel tableModel;
    private JComboBox<String> budgetSelector;
    private BillDAO billDAO;
    private BuildDAO buildDAO;
    private int selectedBillId = -1;

    private JLabel cpuLabel, gpuLabel, ramLabel, storageLabel, psuLabel, priceLabel, scoreLabel;

    public BillingGUI() {
        billDAO = new BillDAO();
        buildDAO = new BuildDAO();
        setLayout(new BorderLayout(0, 16));
        setBackground(BG);
        setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        add(createHeader(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSidePanel(), BorderLayout.EAST);

        loadBills();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Billing");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("Purchase builds, generate bills, view receipts");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(MUTED);

        titlePanel.add(title);
        titlePanel.add(subtitle);

        header.add(titlePanel, BorderLayout.WEST);
        return header;
    }

    private JPanel createCenterPanel() {
        JPanel center = new JPanel(new BorderLayout(0, 12));
        center.setOpaque(false);

        tableModel = new DefaultTableModel(
            new String[]{"Bill ID", "Build", "CPU", "GPU", "Price", "Score", "Date"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(32);
        table.setBackground(CARD);
        table.setForeground(TEXT);
        table.setGridColor(BORDER);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        table.getTableHeader().setBackground(CARD);
        table.getTableHeader().setForeground(MUTED);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.convertRowIndexToModel(table.getSelectedRow());
                selectedBillId = (int) tableModel.getValueAt(row, 0);
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(CARD);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));

        center.add(createBudgetPanel(), BorderLayout.NORTH);
        center.add(scroll, BorderLayout.CENTER);
        return center;
    }

    private JPanel createBudgetPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);

        JLabel lbl = new JLabel("Budget:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT);

        String[] budgets = new String[92];
        budgets[0] = "All";
        for (int i = 1; i < budgets.length; i++) {
            budgets[i] = String.format("%,d", 50000 + (i - 1) * 5000);
        }
        budgetSelector = new JComboBox<>(budgets);
        budgetSelector.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        budgetSelector.setBackground(CARD);
        budgetSelector.setForeground(TEXT);

        JButton deleteBtn = createButton("Delete", RED);
        deleteBtn.addActionListener(e -> deleteBill());

        panel.add(lbl);
        panel.add(budgetSelector);
        panel.add(javax.swing.Box.createHorizontalStrut(20));
        panel.add(deleteBtn);

        return panel;
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD);
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(20, 16, 20, 16)
        ));

        JLabel purchaseTitle = new JLabel("Purchase Build");
        purchaseTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        purchaseTitle.setForeground(TEXT);
        purchaseTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        cpuLabel = createPreviewLabel("CPU: -");
        gpuLabel = createPreviewLabel("GPU: -");
        ramLabel = createPreviewLabel("RAM: -");
        storageLabel = createPreviewLabel("Storage: -");
        psuLabel = createPreviewLabel("PSU: -");
        priceLabel = createPreviewLabel("Price: -");
        scoreLabel = createPreviewLabel("Score: -");

        JButton purchaseBtn = createButton("Purchase Build", GREEN);
        purchaseBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        purchaseBtn.addActionListener(e -> purchaseBuild());

        JButton viewBillBtn = createButton("View Bill Receipt", INDIGO);
        viewBillBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        viewBillBtn.addActionListener(e -> viewBillReceipt());

        panel.add(purchaseTitle);
        panel.add(javax.swing.Box.createVerticalStrut(16));
        panel.add(cpuLabel);
        panel.add(gpuLabel);
        panel.add(ramLabel);
        panel.add(storageLabel);
        panel.add(psuLabel);
        panel.add(javax.swing.Box.createVerticalStrut(8));
        panel.add(priceLabel);
        panel.add(scoreLabel);
        panel.add(javax.swing.Box.createVerticalStrut(20));
        panel.add(purchaseBtn);
        panel.add(javax.swing.Box.createVerticalStrut(8));
        panel.add(viewBillBtn);

        return panel;
    }

    private JLabel createPreviewLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        return btn;
    }

    private void loadBills() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);
            List<Bill> bills = billDAO.getAllBills();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (Bill b : bills) {
                tableModel.addRow(new Object[]{
                    b.getBillId(),
                    "Build #" + b.getBuildId(),
                    b.getFinalCpu(),
                    b.getFinalGpu(),
                    "Rs." + String.format("%,d", b.getFinalPrice()),
                    b.getFinalScore(),
                    b.getPurchaseDate() != null ? b.getPurchaseDate().format(fmt) : ""
                });
            }
        });
    }

    private void purchaseBuild() {
        int selectedIndex = budgetSelector.getSelectedIndex();
        if (selectedIndex == 0) {
            JOptionPane.showMessageDialog(this, "Please select a specific budget (not 'All')");
            return;
        }
        
        int budget = 50000 + (selectedIndex - 1) * 5000;
        List<Build> builds = buildDAO.getBuildsByBudget(budget);

        if (builds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No builds available at Rs." + String.format("%,d", budget));
            return;
        }

        String[] buildOptions = new String[builds.size()];
        for (int i = 0; i < builds.size(); i++) {
            Build b = builds.get(i);
            buildOptions[i] = b.getCpuModel() + " / " + b.getGpuModel() + " (Rs." + b.getTotalPrice() + ")";
        }

        String selected = (String) JOptionPane.showInputDialog(this, "Select a build:", "Purchase Build",
            JOptionPane.QUESTION_MESSAGE, null, buildOptions, buildOptions[0]);
        if (selected == null) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Confirm purchase of:\n" + selected,
            "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        int buildIndex = 0;
        for (int i = 0; i < buildOptions.length; i++) {
            if (buildOptions[i].equals(selected)) {
                buildIndex = i;
                break;
            }
        }

        Build build = builds.get(buildIndex);
        Bill bill = new Bill(0, build.getBuildId(), build.getCpuModel(), build.getGpuModel(),
                            build.getTotalPrice(), build.getPerformanceScore(), null);

        if (billDAO.saveBill(bill)) {
            buildDAO.deleteBuild(build.getBuildId());
            JOptionPane.showMessageDialog(this, "Bill generated successfully!");
            loadBills();
        }
    }

    private void deleteBill() {
        if (selectedBillId == -1) {
            JOptionPane.showMessageDialog(this, "Select a bill from the table first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this bill?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            billDAO.deleteBill(selectedBillId);
            selectedBillId = -1;
            loadBills();
        }
    }

    private void viewBillReceipt() {
        if (selectedBillId == -1) {
            JOptionPane.showMessageDialog(this, "Select a bill from the table first.");
            return;
        }

        List<Bill> bills = billDAO.getAllBills();
        Bill selected = null;
        for (Bill b : bills) {
            if (b.getBillId() == selectedBillId) {
                selected = b;
                break;
            }
        }
        if (selected == null) return;

        String receipt = "+-------------------------------+\n"
            + "|       Purchased Build Bill     |\n"
            + "+-------------------------------+\n"
            + "Build Details:\n"
            + "CPU: " + selected.getFinalCpu() + "\n"
            + "GPU: " + selected.getFinalGpu() + "\n"
            + "Total Price: Rs." + String.format("%,d", selected.getFinalPrice()) + "\n"
            + "Performance Score: " + selected.getFinalScore() + "\n"
            + "+-------------------------------+\n"
            + "Thank you for your purchase!\n";

        JTextArea area = new JTextArea(receipt);
        area.setFont(new Font("Consolas", Font.PLAIN, 13));
        area.setEditable(false);
        area.setBackground(CARD);
        area.setForeground(TEXT);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JOptionPane.showMessageDialog(this, area, "Bill Receipt", JOptionPane.PLAIN_MESSAGE);
    }
}
