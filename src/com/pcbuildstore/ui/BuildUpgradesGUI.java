package com.pcbuildstore.ui;

import com.pcbuildstore.dao.BuildDAO;
import com.pcbuildstore.dao.PartDAO;
import com.pcbuildstore.models.Build;
import com.pcbuildstore.models.BuildPart;
import com.pcbuildstore.models.Part;
import com.pcbuildstore.ui.theme.Components;
import com.pcbuildstore.ui.theme.Theme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BuildUpgradesGUI extends JPanel {

    private final DashboardGUI dashboard;
    private final BuildDAO buildDAO = new BuildDAO();
    private final PartDAO partDAO = new PartDAO();

    private JComboBox<String> buildSelector;
    private int selectedBuildId = -1;
    private int selectedSlotCatId = -1;

    private DefaultTableModel upgradeModel;
    private JTable upgradeTable;
    private int selectedUpgradeRow = -1;

    private static final String[] SLOT_NAMES = {"CPU", "GPU", "RAM", "Storage", "PSU", "Motherboard"};
    private static final int[] SLOT_IDS = {1, 2, 3, 4, 5, 6};
    private static final Color[] SLOT_COLORS = {Theme.INFO, Theme.NVIDIA_GRN, Theme.VIOLET, Theme.GOLD, Theme.EMBER, Theme.ACCENT};

    private final JLabel[] slotNameLabels = new JLabel[6];
    private final JLabel[] slotDetailLabels = new JLabel[6];
    private final JLabel[] slotScoreLabels = new JLabel[6];
    private final JPanel[] slotPanels = new JPanel[6];

    public BuildUpgradesGUI(DashboardGUI dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        buildUI();
        loadBuilds();
    }

    public void onShow() {
        loadBuilds();
    }

    private void buildUI() {
        add(createTopBar(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
    }

    private JPanel createTopBar() {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(Theme.BG);
        wrap.setBorder(BorderFactory.createEmptyBorder(16, 36, 0, 36));

        JLabel eyebrow = Components.eyebrow("UPGRADES");
        eyebrow.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.add(eyebrow);
        wrap.add(Components.vSpacer(2));

        JLabel title = new JLabel("Build upgrades");
        title.setFont(Theme.light(24));
        title.setForeground(Theme.TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrap.add(title);
        wrap.add(Components.vSpacer(12));
        wrap.add(createBuildSelector());
        wrap.add(Components.vSpacer(12));
        return wrap;
    }

    private JPanel createBuildSelector() {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(Theme.SURFACE);
        card.setBorder(new Components.RoundedBorder(Theme.BORDER, 1, Theme.R_LARGE));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JLabel label = new JLabel("Pick a build to upgrade");
        label.setFont(Theme.bold(13));
        label.setForeground(Theme.TEXT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        card.add(label, BorderLayout.WEST);

        buildSelector = new JComboBox<>();
        buildSelector.setFont(Theme.regular(11));
        buildSelector.setBackground(Theme.SURFACE_2);
        buildSelector.setForeground(Theme.TEXT);
        buildSelector.setBorder(null);
        buildSelector.setFocusable(false);
        buildSelector.addActionListener(e -> onBuildSelected());

        JPanel cbWrap = new JPanel(new BorderLayout());
        cbWrap.setBackground(Theme.SURFACE_2);
        cbWrap.setBorder(new Components.RoundedBorder(Theme.BORDER, 1, Theme.R_MEDIUM));
        cbWrap.setPreferredSize(new Dimension(300, 32));
        cbWrap.add(buildSelector, BorderLayout.CENTER);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setOpaque(false);
        right.add(cbWrap);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new BorderLayout(14, 0));
        body.setBackground(Theme.BG);
        body.setBorder(BorderFactory.createEmptyBorder(0, 36, 18, 36));
        body.add(createSlotsPanel(), BorderLayout.WEST);
        body.add(createTablePanel(), BorderLayout.CENTER);
        return body;
    }

    private JPanel createSlotsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(280, 0));

        JLabel header = new JLabel("Select a slot to upgrade");
        header.setFont(Theme.bold(13));
        header.setForeground(Theme.TEXT);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(header);
        panel.add(Components.vSpacer(12));

        for (int i = 0; i < 6; i++) {
            slotPanels[i] = createSlotCard(i);
            panel.add(slotPanels[i]);
            panel.add(Components.vSpacer(4));
        }

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private JPanel createSlotCard(int index) {
        int catId = SLOT_IDS[index];
        Color color = SLOT_COLORS[index];

        JPanel card = new JPanel(new BorderLayout(8, 0)) {
            boolean hover = false;
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hover = true;
                        repaint();
                    }
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        selectSlot(catId);
                    }
                });
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                boolean selected = (selectedSlotCatId == catId);
                if (selected) {
                    g2.setColor(Theme.SURFACE_3);
                } else if (hover) {
                    g2.setColor(Theme.SURFACE_2);
                } else {
                    g2.setColor(Theme.SURFACE);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                if (selected) {
                    g2.setColor(color);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8);
                }
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 72));

        JLabel icon = new JLabel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(Theme.TEXT_INV);
                g2.setFont(Theme.bold(11));
                FontMetrics fm = g2.getFontMetrics();
                String text = SLOT_NAMES[index].substring(0, 2);
                int tx = (getWidth() - fm.stringWidth(text)) / 2;
                int ty = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, tx, ty);
                g2.dispose();
            }
        };
        icon.setPreferredSize(new Dimension(32, 32));
        icon.setOpaque(false);
        card.add(icon, BorderLayout.WEST);

        JPanel texts = new JPanel();
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.setOpaque(false);

        slotNameLabels[index] = new JLabel(SLOT_NAMES[index]);
        slotNameLabels[index].setFont(Theme.bold(11));
        slotNameLabels[index].setForeground(Theme.TEXT_3);
        slotNameLabels[index].setAlignmentX(Component.LEFT_ALIGNMENT);

        slotDetailLabels[index] = new JLabel("Empty slot");
        slotDetailLabels[index].setFont(Theme.regular(10));
        slotDetailLabels[index].setForeground(Theme.TEXT_3);
        slotDetailLabels[index].setAlignmentX(Component.LEFT_ALIGNMENT);

        texts.add(slotNameLabels[index]);
        texts.add(slotDetailLabels[index]);
        card.add(texts, BorderLayout.CENTER);

        slotScoreLabels[index] = new JLabel("0 pts");
        slotScoreLabels[index].setFont(Theme.medium(9));
        slotScoreLabels[index].setForeground(Theme.VIOLET);
        slotScoreLabels[index].setHorizontalAlignment(SwingConstants.RIGHT);
        card.add(slotScoreLabels[index], BorderLayout.EAST);

        return card;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JLabel title = new JLabel("Available upgrades");
        title.setFont(Theme.bold(13));
        title.setForeground(Theme.TEXT);

        JButton applyBtn = Components.primaryButton("Apply upgrade");
        applyBtn.setFont(Theme.medium(10));
        applyBtn.addActionListener(e -> applyUpgrade());

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.add(title, BorderLayout.WEST);
        header.add(applyBtn, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        String[] cols = {"ID", "Part", "Brand", "Price", "Score", "Detail"};
        upgradeModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        upgradeTable = new JTable(upgradeModel);
        styleTable(upgradeTable);
        upgradeTable.setRowHeight(36);
        upgradeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectedUpgradeRow = upgradeTable.getSelectedRow();
            }
        });

        JScrollPane scroll = new JScrollPane(upgradeTable);
        Components.applyDarkScrollbar(scroll);
        scroll.getViewport().setBackground(Theme.SURFACE);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.SURFACE);
        card.setBorder(new Components.RoundedBorder(Theme.BORDER, 1, Theme.R_LARGE));
        card.add(scroll, BorderLayout.CENTER);
        panel.add(card, BorderLayout.CENTER);

        return panel;
    }

    private void styleTable(JTable table) {
        table.setBackground(Theme.SURFACE);
        table.setForeground(Theme.TEXT);
        table.setGridColor(Theme.BORDER_SOFT);
        table.setSelectionBackground(Theme.SURFACE_2);
        table.setSelectionForeground(Theme.TEXT);
        table.setFont(Theme.regular(12));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader th = table.getTableHeader();
        th.setBackground(Theme.SURFACE);
        th.setForeground(Theme.TEXT_3);
        th.setFont(Theme.medium(9));
        th.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SOFT));
        th.setPreferredSize(new Dimension(0, 36));

        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                l.setBackground(sel ? Theme.SURFACE_2 : Theme.SURFACE);
                l.setForeground(Theme.TEXT);
                l.setFont(Theme.regular(12));
                l.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
                if (col == 3 || col == 4) {
                    l.setHorizontalAlignment(SwingConstants.RIGHT);
                    l.setFont(Theme.bold(12));
                } else if (col == 0) {
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                    l.setForeground(Theme.TEXT_3);
                } else if (col == 5) {
                    l.setForeground(Theme.TEXT_2);
                    l.setFont(Theme.regular(10));
                }
                return l;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(r);
        }
    }

    private void loadBuilds() {
        buildSelector.removeAllItems();
        buildSelector.addItem("-- Select a build --");
        for (Build b : buildDAO.getAllBuilds()) {
            buildSelector.addItem(b.getBuildId() + "  -  " + b.getName() + "  -  PKR " + String.format("%,d", b.getTotalPrice()));
        }
    }

    private void onBuildSelected() {
        int idx = buildSelector.getSelectedIndex();
        if (idx <= 0) {
            selectedBuildId = -1;
            selectedSlotCatId = -1;
            resetSlots();
            upgradeModel.setRowCount(0);
            return;
        }
        Build build = buildDAO.getAllBuilds().get(idx - 1);
        selectedBuildId = build.getBuildId();
        selectedSlotCatId = -1;
        loadBuildSlots();
        upgradeModel.setRowCount(0);
    }

    private void resetSlots() {
        for (int i = 0; i < 6; i++) {
            slotNameLabels[i].setText(SLOT_NAMES[i]);
            slotDetailLabels[i].setText("Empty slot");
            slotScoreLabels[i].setText("0 pts");
            slotPanels[i].repaint();
        }
    }

    private void loadBuildSlots() {
        boolean[] filled = new boolean[6];
        for (BuildPart bp : buildDAO.getBuildParts(selectedBuildId)) {
            for (int i = 0; i < 6; i++) {
                if (SLOT_IDS[i] == bp.getCategoryId()) {
                    Part p = partDAO.getPartById(bp.getPartId());
                    if (p != null) {
                        slotNameLabels[i].setText(p.getBrand() + " " + p.getName());
                        slotDetailLabels[i].setText("PKR " + String.format("%,d", p.getPrice()));
                        slotScoreLabels[i].setText(p.getPerformanceScore() + " pts");
                    }
                    filled[i] = true;
                    break;
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            if (!filled[i]) {
                slotNameLabels[i].setText(SLOT_NAMES[i]);
                slotDetailLabels[i].setText("Empty slot");
                slotScoreLabels[i].setText("0 pts");
            }
            slotPanels[i].repaint();
        }
    }

    private void selectSlot(int catId) {
        selectedSlotCatId = catId;
        for (JPanel p : slotPanels) {
            p.repaint();
        }
        loadUpgrades(catId);
    }

    private void loadUpgrades(int catId) {
        upgradeModel.setRowCount(0);
        selectedUpgradeRow = -1;
        if (selectedBuildId == -1) return;

        List<BuildPart> buildParts = buildDAO.getBuildParts(selectedBuildId);
        String socket = null;
        String ddr = null;
        int currentId = -1;

        for (BuildPart bp : buildParts) {
            Part p = partDAO.getPartById(bp.getPartId());
            if (p == null) continue;
            if (bp.getCategoryId() == 1) socket = p.getSocketType();
            if (bp.getCategoryId() == 6 && socket == null) socket = p.getSocketType();
            if (bp.getCategoryId() == 6 && ddr == null) ddr = p.getDdrGeneration();
            if (bp.getCategoryId() == 3 && ddr == null) ddr = p.getDdrGeneration();
            if (bp.getCategoryId() == catId) currentId = bp.getPartId();
        }

        List<Part> candidates = getCompatible(catId, socket, ddr, currentId);

        for (Part p : candidates) {
            String detail = getDetail(catId, p);
            upgradeModel.addRow(new Object[]{
                p.getPartId(),
                p.getName(),
                p.getBrand(),
                "PKR " + String.format("%,d", p.getPrice()),
                p.getPerformanceScore() + " pts",
                detail
            });
        }
    }

    private String getDetail(int catId, Part p) {
        return switch (catId) {
            case 1 -> p.getSocketType() != null ? p.getSocketType() : "";
            case 2 -> p.getVram() != null ? p.getVram() + " VRAM" : "";
            case 3 -> (p.getDdrGeneration() != null ? p.getDdrGeneration() : "")
                    + (p.getCapacity() != null ? " " + p.getCapacity() : "");
            case 4 -> p.getCapacity() != null ? p.getCapacity() : "";
            case 5 -> (p.getWattage() != null ? p.getWattage() + "W" : "")
                    + (p.getEfficiency() != null ? " " + p.getEfficiency() : "");
            case 6 -> (p.getSocketType() != null ? p.getSocketType() : "")
                    + (p.getDdrGeneration() != null ? " " + p.getDdrGeneration() : "");
            default -> "";
        };
    }

    private List<Part> getCompatible(int catId, String socket, String ddr, int currentId) {
        List<Part> list = new ArrayList<>();
        switch (catId) {
            case 1 -> {
                if (socket != null && !socket.isEmpty()) {
                    list.addAll(partDAO.getCompatibleCPUs(socket));
                } else {
                    list.addAll(partDAO.getPartsByCategory(1));
                }
            }
            case 2 -> list.addAll(partDAO.getPartsByCategory(2));
            case 3 -> {
                if (ddr != null && !ddr.isEmpty()) {
                    list.addAll(partDAO.getCompatibleParts(null, ddr));
                } else {
                    list.addAll(partDAO.getPartsByCategory(3));
                }
            }
            case 4 -> list.addAll(partDAO.getPartsByCategory(4));
            case 5 -> list.addAll(partDAO.getAdequatePSUs(400));
            case 6 -> {
                for (Part p : partDAO.getPartsByCategory(6)) {
                    boolean socketOk = socket == null || socket.isEmpty()
                            || p.getSocketType() == null || p.getSocketType().equalsIgnoreCase(socket);
                    boolean ddrOk = ddr == null || ddr.isEmpty()
                            || p.getDdrGeneration() == null || p.getDdrGeneration().equalsIgnoreCase(ddr);
                    if (socketOk && ddrOk) list.add(p);
                }
            }
        }
        list.removeIf(p -> p.getPartId() == currentId);
        return list;
    }

    private void applyUpgrade() {
        if (selectedBuildId == -1) {
            JOptionPane.showMessageDialog(this, "Select a build first.");
            return;
        }
        if (selectedSlotCatId == -1) {
            JOptionPane.showMessageDialog(this, "Select a slot to upgrade.");
            return;
        }
        if (selectedUpgradeRow < 0 || selectedUpgradeRow >= upgradeModel.getRowCount()) {
            JOptionPane.showMessageDialog(this, "Select an upgrade from the table.");
            return;
        }

        int partId = (int) upgradeModel.getValueAt(selectedUpgradeRow, 0);
        String partName = (String) upgradeModel.getValueAt(selectedUpgradeRow, 1);
        Part newPart = partDAO.getPartById(partId);
        if (newPart == null) return;

        String slotName = SLOT_NAMES[getSlotIndex(selectedSlotCatId)];

        int confirm = JOptionPane.showConfirmDialog(this,
                "Replace " + slotName + " with:\n"
                        + newPart.getBrand() + " " + newPart.getName() + "\n"
                        + "Price: PKR " + String.format("%,d", newPart.getPrice()) + "\n"
                        + "Score: " + newPart.getPerformanceScore() + " pts",
                "Confirm upgrade", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            buildDAO.removePartFromBuild(selectedBuildId, selectedSlotCatId);
            buildDAO.addPartToBuild(selectedBuildId, selectedSlotCatId, partId, newPart.getPrice());

            List<BuildPart> parts = buildDAO.getBuildParts(selectedBuildId);
            int newPrice = 0;
            int newScore = 0;
            for (BuildPart bp : parts) {
                Part p = partDAO.getPartById(bp.getPartId());
                if (p != null) {
                    newPrice += p.getPrice();
                    newScore += p.getPerformanceScore();
                }
            }
            buildDAO.updateBuildTotals(selectedBuildId, newPrice, newScore);

            JOptionPane.showMessageDialog(this, slotName + " upgraded!");
            loadBuildSlots();
            loadUpgrades(selectedSlotCatId);
            dashboard.refreshStats();
        }
    }

    private int getSlotIndex(int catId) {
        for (int i = 0; i < 6; i++) {
            if (SLOT_IDS[i] == catId) return i;
        }
        return 0;
    }
}
