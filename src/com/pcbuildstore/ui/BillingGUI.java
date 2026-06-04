package com.pcbuildstore.ui;

import com.pcbuildstore.dao.BillDAO;
import com.pcbuildstore.dao.BuildDAO;
import com.pcbuildstore.dao.PartDAO;
import com.pcbuildstore.models.Bill;
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
import java.awt.print.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BillingGUI extends JPanel {

    private final DashboardGUI dashboard;
    private final BillDAO billDAO = new BillDAO();
    private final BuildDAO buildDAO = new BuildDAO();
    private final PartDAO partDAO = new PartDAO();

    private JComboBox<String> buildSelector;
    private DefaultTableModel billsModel;
    private JTable billsTable;
    private int selectedBuildId = -1;
    private int selectedBillId = -1;

    private JLabel previewName, previewCpu, previewGpu, previewRam, previewStorage, previewPsu;
    private JLabel previewPrice, previewScore;

    public BillingGUI(DashboardGUI dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);
        buildUI();
        loadBills();
        loadBuilds();
    }

    public void onShow() { loadBills(); loadBuilds(); }

    private void buildUI() {
        add(createTopBar(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createTopBar() {
        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(Theme.BG);
        wrap.setBorder(BorderFactory.createEmptyBorder(16, 36, 0, 36));

        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        JLabel ey = Components.eyebrow("CHECKOUT");
        ey.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel t = new JLabel("Billing");
        t.setFont(Theme.light(24));
        t.setForeground(Theme.TEXT);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(ey);
        left.add(Box.createVerticalStrut(2));
        left.add(t);
        row.add(left, BorderLayout.WEST);
        return wrap;
    }

    private JPanel createMainPanel() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BG);
        main.setBorder(BorderFactory.createEmptyBorder(14, 36, 18, 36));

        JPanel left = new JPanel(new BorderLayout(0, 12));
        left.setOpaque(false);
        left.add(createPurchaseRow(), BorderLayout.NORTH);
        left.add(createHistoryPanel(), BorderLayout.CENTER);

        JPanel right = createSidePanel();
        main.add(left, BorderLayout.CENTER);
        main.add(right, BorderLayout.EAST);
        return main;
    }

    private JPanel createPurchaseRow() {
        JPanel card = new JPanel(new BorderLayout(12, 0));
        card.setBackground(Theme.SURFACE);
        card.setBorder(new Components.RoundedBorder(Theme.BORDER, 1, Theme.R_LARGE));
        card.setOpaque(true);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
        JLabel ey = Components.eyebrow("STEP  01");
        ey.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel t = new JLabel("Pick a build to purchase");
        t.setFont(Theme.bold(13));
        t.setForeground(Theme.TEXT);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(ey);
        left.add(Box.createVerticalStrut(2));
        left.add(t);
        card.add(left, BorderLayout.WEST);

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.X_AXIS));
        right.setOpaque(false);
        right.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 14));
        right.add(Box.createHorizontalGlue());

        JLabel sl = new JLabel("Build  ");
        sl.setFont(Theme.regular(10));
        sl.setForeground(Theme.TEXT_2);
        right.add(sl);

        JPanel cbWrap = new JPanel(new BorderLayout());
        cbWrap.setBackground(Theme.SURFACE_2);
        cbWrap.setBorder(new Components.RoundedBorder(Theme.BORDER, 1, Theme.R_MEDIUM));
        cbWrap.setPreferredSize(new Dimension(300, 32));
        cbWrap.setMaximumSize(new Dimension(300, 32));

        buildSelector = new JComboBox<>();
        buildSelector.setFont(Theme.regular(11));
        buildSelector.setBackground(Theme.SURFACE_2);
        buildSelector.setForeground(Theme.TEXT);
        buildSelector.setBorder(null);
        buildSelector.setFocusable(false);
        buildSelector.addActionListener(e -> onBuildSelected());
        cbWrap.add(buildSelector, BorderLayout.CENTER);
        right.add(cbWrap);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JLabel title = new JLabel("Purchase history");
        title.setFont(Theme.bold(13));
        title.setForeground(Theme.TEXT);
        header.add(title, BorderLayout.WEST);

        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.X_AXIS));
        right.setOpaque(false);
        JButton viewBtn = Components.ghostButton("View receipt");
        viewBtn.setFont(Theme.medium(10));
        viewBtn.addActionListener(e -> viewReceipt());
        JButton delBtn  = Components.ghostButton("Delete");
        delBtn.setFont(Theme.medium(10));
        delBtn.setForeground(Theme.SALE);
        delBtn.addActionListener(e -> deleteBill());
        right.add(viewBtn);
        right.add(Box.createHorizontalStrut(6));
        right.add(delBtn);
        header.add(right, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Theme.SURFACE);
        card.setBorder(new Components.RoundedBorder(Theme.BORDER, 1, Theme.R_LARGE));

        String[] cols = {"Bill", "Build", "Price (PKR)", "Score", "Date"};
        billsModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        billsTable = new JTable(billsModel);
        styleBillsTable(billsTable);
        billsTable.setRowHeight(36);

        billsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = billsTable.getSelectedRow();
                selectedBillId = row >= 0 ? (int) billsModel.getValueAt(row, 0) : -1;
            }
        });

        JScrollPane scroll = new JScrollPane(billsTable);
        Components.applyDarkScrollbar(scroll);
        scroll.getViewport().setBackground(Theme.SURFACE);
        card.add(scroll, BorderLayout.CENTER);
        panel.add(card, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSidePanel() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(Theme.BG);
        side.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));
        side.setPreferredSize(new Dimension(260, 0));

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel ey = Components.eyebrow("STEP  02");
        ey.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel t = new JLabel("Build preview");
        t.setFont(Theme.bold(13));
        t.setForeground(Theme.TEXT);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        header.add(ey);
        header.add(Box.createVerticalStrut(2));
        header.add(t);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        side.add(header);
        side.add(Components.vSpacer(10));

        JPanel preview = new JPanel(new BorderLayout(0, 6));
        preview.setBackground(Theme.SURFACE);
        preview.setBorder(BorderFactory.createCompoundBorder(
            new Components.RoundedBorder(Theme.BORDER, 1, Theme.R_MEDIUM),
            BorderFactory.createEmptyBorder(14, 14, 14, 14)
        ));
        preview.setOpaque(true);
        preview.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel pn = new JLabel("Untitled build");
        pn.setFont(Theme.bold(15));
        pn.setForeground(Theme.TEXT);
        preview.add(pn, BorderLayout.NORTH);
        previewName = pn;

        JPanel rows = new JPanel();
        rows.setLayout(new BoxLayout(rows, BoxLayout.Y_AXIS));
        rows.setOpaque(false);
        previewCpu     = addSpecRow(rows, "CPU",     Theme.INTEL_BLUE);
        previewGpu     = addSpecRow(rows, "GPU",     Theme.NVIDIA_GRN);
        previewRam     = addSpecRow(rows, "RAM",     Theme.INFO);
        previewStorage = addSpecRow(rows, "Storage", Theme.HOT);
        previewPsu     = addSpecRow(rows, "Power",   Theme.AMD_RED);
        preview.add(rows, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setOpaque(false);
        JPanel pp = new JPanel(new BorderLayout());
        pp.setOpaque(false);
        JLabel pl = new JLabel("TOTAL  PRICE");
        pl.setFont(Theme.medium(8));
        pl.setForeground(Theme.TEXT_3);
        previewPrice = new JLabel("PKR 0");
        previewPrice.setFont(Theme.bold(18));
        previewPrice.setForeground(Theme.TEXT);
        pp.add(pl, BorderLayout.NORTH);
        pp.add(previewPrice, BorderLayout.CENTER);

        JPanel sp = new JPanel(new BorderLayout());
        sp.setOpaque(false);
        JLabel sl = new JLabel("PERFORMANCE  SCORE");
        sl.setFont(Theme.medium(8));
        sl.setForeground(Theme.TEXT_3);
        previewScore = new JLabel("0");
        previewScore.setFont(Theme.bold(15));
        previewScore.setForeground(Theme.HOT);
        sp.add(sl, BorderLayout.NORTH);
        sp.add(previewScore, BorderLayout.CENTER);
        bottom.add(pp);
        bottom.add(Box.createVerticalStrut(6));
        bottom.add(sp);
        bottom.setAlignmentX(Component.LEFT_ALIGNMENT);
        preview.add(bottom, BorderLayout.SOUTH);
        preview.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));

        side.add(preview);
        side.add(Components.vSpacer(10));

        JButton buy = Components.saleButton("Purchase build");
        buy.setAlignmentX(Component.LEFT_ALIGNMENT);
        buy.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        buy.setFont(Theme.medium(10));
        buy.addActionListener(e -> purchaseBuild());
        side.add(buy);

        side.add(Box.createVerticalGlue());
        return side;
    }

    private JLabel addSpecRow(JPanel parent, String name, Color accent) {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setOpaque(false);
        row.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        left.setOpaque(false);
        JLabel dot = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(accent);
                g2.fillOval(0, 0, 5, 5);
                g2.dispose();
            }
        };
        dot.setPreferredSize(new Dimension(5, 5));
        dot.setOpaque(false);
        left.add(dot);
        JLabel nLbl = new JLabel(name);
        nLbl.setFont(Theme.medium(8));
        nLbl.setForeground(Theme.TEXT_3);
        left.add(nLbl);
        row.add(left, BorderLayout.WEST);

        JLabel v = new JLabel("--");
        v.setFont(Theme.regular(10));
        v.setForeground(Theme.TEXT);
        v.setHorizontalAlignment(SwingConstants.RIGHT);
        row.add(v, BorderLayout.EAST);

        parent.add(row);
        return v;
    }

    private void styleBillsTable(JTable table) {
        table.setBackground(Theme.SURFACE);
        table.setForeground(Theme.TEXT);
        table.setGridColor(Theme.BORDER_SOFT);
        table.setSelectionBackground(Theme.SURFACE_2);
        table.setSelectionForeground(Theme.TEXT);
        table.setFont(Theme.regular(12));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBorder(null);
        table.setFocusable(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setBackground(Theme.SURFACE);
        header.setForeground(Theme.TEXT_3);
        header.setFont(Theme.medium(9));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_SOFT));
        header.setPreferredSize(new Dimension(0, 36));

        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean sel, boolean foc, int row, int col) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                l.setBackground(sel ? Theme.SURFACE_2 : Theme.SURFACE);
                l.setForeground(Theme.TEXT);
                l.setFont(Theme.regular(12));
                l.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
                if (col == 2) {
                    l.setHorizontalAlignment(SwingConstants.RIGHT);
                    l.setFont(Theme.bold(12));
                } else if (col == 3) {
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                    l.setFont(Theme.bold(12));
                    l.setForeground(Theme.HOT);
                } else if (col == 0 || col == 4) {
                    l.setForeground(Theme.TEXT_3);
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
        List<Build> builds = buildDAO.getAllBuilds();
        for (Build b : builds) {
            buildSelector.addItem(b.getBuildId() + "  -  " + b.getName() + "  -  PKR " + String.format("%,d", b.getTotalPrice()));
        }
    }

    private void onBuildSelected() {
        int idx = buildSelector.getSelectedIndex();
        if (idx <= 0) {
            selectedBuildId = -1;
            clearPreview();
            return;
        }
        Build build = buildDAO.getAllBuilds().get(idx - 1);
        selectedBuildId = build.getBuildId();

        previewName.setText(build.getName());
        previewPrice.setText("PKR " + String.format("%,d", build.getTotalPrice()));
        previewScore.setText(String.valueOf(build.getTotalScore()));

        List<BuildPart> parts = buildDAO.getBuildParts(selectedBuildId);
        for (BuildPart bp : parts) {
            Part p = partDAO.getPartById(bp.getPartId());
            if (p == null) continue;
            String text = p.getBrand() + " " + p.getName();
            switch (bp.getCategoryId()) {
                case 1: previewCpu.setText(text); break;
                case 2: previewGpu.setText(text); break;
                case 3: previewRam.setText(text); break;
                case 4: previewStorage.setText(text); break;
                case 5: previewPsu.setText(text); break;
            }
        }
    }

    private void clearPreview() {
        previewName.setText("Untitled build");
        previewCpu.setText("--");
        previewGpu.setText("--");
        previewRam.setText("--");
        previewStorage.setText("--");
        previewPsu.setText("--");
        previewPrice.setText("PKR 0");
        previewScore.setText("0");
    }

    private void purchaseBuild() {
        if (selectedBuildId == -1) {
            JOptionPane.showMessageDialog(this, "Select a build to purchase.");
            return;
        }
        Build build = buildDAO.getBuildById(selectedBuildId);
        List<BuildPart> parts = buildDAO.getBuildParts(selectedBuildId);
        if (parts.size() < 5) {
            JOptionPane.showMessageDialog(this, "This build is incomplete (missing parts).");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Purchase this build?\n\n");
        sb.append("Name: ").append(build.getName()).append("\n");
        sb.append("Price: PKR ").append(String.format("%,d", build.getTotalPrice())).append("\n");
        sb.append("Score: ").append(build.getTotalScore()).append("\n\n");
        sb.append("Parts:\n");
        for (BuildPart bp : parts) {
            Part p = partDAO.getPartById(bp.getPartId());
            if (p != null) {
                sb.append("  ").append(bp.getCategoryName()).append(": ")
                  .append(p.getBrand()).append(" ").append(p.getName())
                  .append(" (PKR ").append(String.format("%,d", bp.getPriceAtAdd())).append(")\n");
            }
        }
        int confirm = JOptionPane.showConfirmDialog(this, sb.toString(), "Confirm purchase", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Bill bill = new Bill(0, selectedBuildId, build.getTotalPrice(), build.getTotalScore(), null);
            boolean saved = billDAO.saveBill(bill);
            if (saved) {
                loadBills();
                loadBuilds();
                clearPreview();
                selectedBuildId = -1;
                buildSelector.setSelectedIndex(0);
                dashboard.refreshStats();
                List<Bill> bills = billDAO.getAllBills();
                if (!bills.isEmpty()) {
                    selectedBillId = bills.get(0).getBillId();
                    viewReceipt();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save bill.");
            }
        }
    }

    private void loadBills() {
        billsModel.setRowCount(0);
        List<Bill> bills = billDAO.getAllBills();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' HH:mm");
        for (Bill b : bills) {
            billsModel.addRow(new Object[]{
                b.getBillId(),
                b.getBuildName(),
                String.format("%,d", b.getFinalPrice()),
                b.getFinalScore(),
                b.getPurchaseDate() != null ? b.getPurchaseDate().format(fmt) : ""
            });
        }
    }

    private void viewReceipt() {
        if (selectedBillId == -1) {
            JOptionPane.showMessageDialog(this, "Select a bill first.");
            return;
        }
        Bill bill = null;
        for (Bill b : billDAO.getAllBills()) {
            if (b.getBillId() == selectedBillId) { bill = b; break; }
        }
        if (bill == null) return;
        Build build = buildDAO.getBuildById(bill.getBuildId());
        List<BuildPart> parts = buildDAO.getBuildParts(bill.getBuildId());

        JPanel receiptPanel = new JPanel();
        receiptPanel.setLayout(new BoxLayout(receiptPanel, BoxLayout.Y_AXIS));
        receiptPanel.setBackground(Theme.SURFACE);
        receiptPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel shop = new JLabel("PC BUILD STORE");
        shop.setFont(Theme.medium(10));
        shop.setForeground(Theme.TEXT_3);
        shop.setAlignmentX(Component.LEFT_ALIGNMENT);
        receiptPanel.add(shop);
        receiptPanel.add(Components.vSpacer(4));

        JLabel title = new JLabel("Receipt #" + bill.getBillId());
        title.setFont(Theme.bold(22));
        title.setForeground(Theme.TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        receiptPanel.add(title);
        receiptPanel.add(Components.vSpacer(2));

        JLabel dt = new JLabel(bill.getPurchaseDate() != null
            ? bill.getPurchaseDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' HH:mm"))
            : "N/A");
        dt.setFont(Theme.regular(11));
        dt.setForeground(Theme.TEXT_2);
        dt.setAlignmentX(Component.LEFT_ALIGNMENT);
        receiptPanel.add(dt);
        receiptPanel.add(Components.vSpacer(16));

        addSeparator(receiptPanel);
        receiptPanel.add(Components.vSpacer(14));

        JLabel buildLbl = new JLabel(build.getName());
        buildLbl.setFont(Theme.bold(14));
        buildLbl.setForeground(Theme.TEXT);
        buildLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        receiptPanel.add(buildLbl);
        receiptPanel.add(Components.vSpacer(10));

        for (BuildPart bp : parts) {
            Part p = partDAO.getPartById(bp.getPartId());
            if (p == null) continue;
            JPanel row = new JPanel(new BorderLayout(8, 0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
            row.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel left = new JPanel();
            left.setLayout(new BoxLayout(left, BoxLayout.X_AXIS));
            left.setOpaque(false);
            JLabel cat = new JLabel(bp.getCategoryName().toUpperCase());
            cat.setFont(Theme.medium(9));
            cat.setForeground(Theme.TEXT_3);
            cat.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
            left.add(cat);
            JLabel n = new JLabel(p.getBrand() + " " + p.getName());
            n.setFont(Theme.regular(12));
            n.setForeground(Theme.TEXT);
            left.add(n);
            row.add(left, BorderLayout.CENTER);

            JLabel pr = new JLabel("PKR " + String.format("%,d", bp.getPriceAtAdd()));
            pr.setFont(Theme.medium(12));
            pr.setForeground(Theme.TEXT);
            row.add(pr, BorderLayout.EAST);
            receiptPanel.add(row);
            receiptPanel.add(Components.vSpacer(6));
        }

        receiptPanel.add(Components.vSpacer(8));
        addSeparator(receiptPanel);
        receiptPanel.add(Components.vSpacer(10));

        JPanel totalRow = new JPanel(new BorderLayout());
        totalRow.setOpaque(false);
        totalRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        totalRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel tlbl = new JLabel("TOTAL");
        tlbl.setFont(Theme.medium(11));
        tlbl.setForeground(Theme.TEXT_2);
        totalRow.add(tlbl, BorderLayout.WEST);
        JLabel tval = new JLabel("PKR " + String.format("%,d", bill.getFinalPrice()));
        tval.setFont(Theme.bold(20));
        tval.setForeground(Theme.TEXT);
        totalRow.add(tval, BorderLayout.EAST);
        receiptPanel.add(totalRow);
        receiptPanel.add(Components.vSpacer(4));

        JPanel scoreRow = new JPanel(new BorderLayout());
        scoreRow.setOpaque(false);
        scoreRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 22));
        scoreRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel sl = new JLabel("PERFORMANCE  SCORE");
        sl.setFont(Theme.medium(9));
        sl.setForeground(Theme.TEXT_3);
        scoreRow.add(sl, BorderLayout.WEST);
        JLabel sv = new JLabel(String.valueOf(bill.getFinalScore()));
        sv.setFont(Theme.bold(14));
        sv.setForeground(Theme.HOT);
        scoreRow.add(sv, BorderLayout.EAST);
        receiptPanel.add(scoreRow);
        receiptPanel.add(Components.vSpacer(20));

        JLabel thank = new JLabel("Thank you for your purchase.");
        thank.setFont(Theme.regular(11));
        thank.setForeground(Theme.TEXT_3);
        thank.setAlignmentX(Component.LEFT_ALIGNMENT);
        receiptPanel.add(thank);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton printBtn = Components.primaryButton("Print");
        Bill fb = bill;
        Build bd = build;
        List<BuildPart> bp = parts;
        printBtn.addActionListener(e -> printReceipt(fb, bd, bp));
        btnRow.add(printBtn);

        JButton saveBtn = Components.primaryButton("Save");
        saveBtn.addActionListener(e -> saveReceipt(fb, bd, bp));
        btnRow.add(saveBtn);

        receiptPanel.add(Components.vSpacer(12));
        receiptPanel.add(btnRow);

        JOptionPane.showMessageDialog(this, receiptPanel, "Receipt #" + bill.getBillId(), JOptionPane.PLAIN_MESSAGE);
    }

    private void addSeparator(JPanel panel) {
        JPanel sep = new JPanel();
        sep.setOpaque(true);
        sep.setBackground(Theme.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setPreferredSize(new Dimension(0, 1));
        panel.add(sep);
    }

    private void printReceipt(Bill bill, Build build, List<BuildPart> parts) {
        Printable printable = (graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2 = (Graphics2D) graphics;
            g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            int x = 10;
            int y = 20;
            int w = (int) pageFormat.getImageableWidth() - 20;

            g2.setFont(new Font("Monospaced", Font.BOLD, 16));
            g2.drawString("PC BUILD STORE", x, y);
            y += 16;
            g2.setFont(new Font("Monospaced", Font.PLAIN, 9));
            g2.drawString("Receipt #" + bill.getBillId(), x, y);
            y += 12;
            if (bill.getPurchaseDate() != null) {
                g2.drawString(bill.getPurchaseDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm")), x, y);
            }
            y += 6;
            g2.drawLine(x, y, x + w, y);
            y += 14;

            g2.setFont(new Font("Monospaced", Font.BOLD, 12));
            g2.drawString(build.getName(), x, y);
            y += 16;

            g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
            for (BuildPart bp : parts) {
                Part p = partDAO.getPartById(bp.getPartId());
                if (p == null) continue;
                String cat = bp.getCategoryName().toUpperCase();
                String name = p.getBrand() + " " + p.getName();
                String price = "PKR " + String.format("%,d", bp.getPriceAtAdd());
                g2.drawString(cat + ": " + name, x, y);
                g2.drawString(price, x + w - g2.getFontMetrics().stringWidth(price), y);
                y += 13;
            }

            y += 4;
            g2.drawLine(x, y, x + w, y);
            y += 16;
            g2.setFont(new Font("Monospaced", Font.BOLD, 14));
            String total = "PKR " + String.format("%,d", bill.getFinalPrice());
            g2.drawString("TOTAL", x, y);
            g2.drawString(total, x + w - g2.getFontMetrics().stringWidth(total), y);
            y += 16;
            g2.setFont(new Font("Monospaced", Font.PLAIN, 10));
            g2.drawString("Performance Score: " + bill.getFinalScore(), x, y);
            y += 16;
            g2.setFont(new Font("Monospaced", Font.ITALIC, 10));
            g2.drawString("Thank you for your purchase.", x, y);

            return Printable.PAGE_EXISTS;
        };
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setJobName("PCBuildStore Receipt #" + bill.getBillId());
        job.setPrintable(printable);
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Print failed: " + ex.getMessage());
            }
        }
    }

    private void saveReceipt(Bill bill, Build build, List<BuildPart> parts) {
        String html = buildReceiptHtml(bill, build, parts);
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new java.io.File("Receipt_" + bill.getBillId() + ".html"));
        int opt = fc.showSaveDialog(null);
        if (opt != JFileChooser.APPROVE_OPTION) return;
        java.io.File file = fc.getSelectedFile();
        try {
            java.io.FileWriter fw = new java.io.FileWriter(file);
            fw.write(html);
            fw.close();
            JOptionPane.showMessageDialog(null, "Receipt saved to:\n" + file.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to save: " + ex.getMessage());
        }
    }

    private String buildReceiptHtml(Bill bill, Build build, List<BuildPart> parts) {
        StringBuilder s = new StringBuilder();
        s.append("<!DOCTYPE html><html><head><meta charset=\"utf-8\"><title>Receipt #");
        s.append(bill.getBillId());
        s.append("</title><style>");
        s.append("body{font-family:monospace;max-width:500px;margin:40px auto;padding:20px;");
        s.append("border:2px solid #333;color:#111}");
        s.append("h1{text-align:center;margin:0;font-size:20px}");
        s.append("h2{text-align:center;margin:4px 0 16px;font-size:14px;font-weight:normal;color:#666}");
        s.append("hr{border:none;border-top:1px dashed #999;margin:16px 0}");
        s.append("table{width:100%;border-collapse:collapse}");
        s.append("td{padding:4px 0;font-size:13px}");
        s.append(".total td{font-weight:bold;font-size:16px;border-top:2px solid #333;padding-top:8px}");
        s.append(".score td{color:#e33;font-weight:bold}");
        s.append(".footer{text-align:center;margin-top:20px;font-size:12px;color:#888}");
        s.append("</style></head><body>");
        s.append("<h1>PC BUILD STORE</h1>");
        s.append("<h2>Receipt #").append(bill.getBillId()).append("</h2>");
        s.append("<p style=\"font-size:12px;color:#666\">");
        s.append(bill.getPurchaseDate() != null
            ? bill.getPurchaseDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy 'at' HH:mm"))
            : "N/A");
        s.append("</p>");
        s.append("<hr>");
        s.append("<p><b>Build:</b> ").append(build.getName()).append("</p>");
        s.append("<table>");
        for (BuildPart bp : parts) {
            Part p = partDAO.getPartById(bp.getPartId());
            if (p == null) continue;
            s.append("<tr><td><b>").append(bp.getCategoryName().toUpperCase()).append("</b><br>");
            s.append(p.getBrand()).append(" ").append(p.getName()).append("</td>");
            s.append("<td style=\"text-align:right;white-space:nowrap\">PKR ");
            s.append(String.format("%,d", bp.getPriceAtAdd())).append("</td></tr>");
        }
        s.append("<tr class=\"total\"><td>TOTAL</td><td style=\"text-align:right\">PKR ");
        s.append(String.format("%,d", bill.getFinalPrice())).append("</td></tr>");
        s.append("<tr class=\"score\"><td>PERFORMANCE SCORE</td><td style=\"text-align:right\">");
        s.append(bill.getFinalScore()).append("</td></tr>");
        s.append("</table><hr>");
        s.append("<p class=\"footer\">Thank you for your purchase!</p>");
        s.append("</body></html>");
        return s.toString();
    }

    private void deleteBill() {
        if (selectedBillId == -1) {
            JOptionPane.showMessageDialog(this, "Select a bill first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this bill?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            billDAO.deleteBill(selectedBillId);
            selectedBillId = -1;
            loadBills();
            dashboard.refreshStats();
        }
    }
}
