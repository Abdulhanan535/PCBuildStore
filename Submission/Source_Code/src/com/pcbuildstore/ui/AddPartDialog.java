package com.pcbuildstore.ui;

import com.pcbuildstore.dao.PartDAO;
import com.pcbuildstore.models.Part;
import com.pcbuildstore.ui.theme.Components;
import com.pcbuildstore.ui.theme.Theme;

import javax.swing.*;
import java.awt.*;

public class AddPartDialog extends JDialog {

    private boolean saved = false;
    private JComboBox<String> categoryBox;
    private JTextField brandField, nameField, priceField, scoreField;
    private JTextField socketField, ddrField, vramField, capacityField;
    private JTextField wattField, efficiencyField;

    private static final String[] CAT_NAMES = {"CPU", "GPU", "RAM", "Storage", "PSU", "Motherboard"};
    private static final int[] CAT_IDS = {1, 2, 3, 4, 5, 6};

    public AddPartDialog(Window owner) {
        super(owner, "Add New Part", ModalityType.APPLICATION_MODAL);
        setSize(420, 520);
        setLocationRelativeTo(owner);
        setResizable(false);
        getContentPane().setBackground(Theme.BG);
        setLayout(new BorderLayout());
        buildUI();
    }

    public boolean isSaved() { return saved; }

    private void buildUI() {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Theme.BG);
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Add new part");
        title.setFont(Theme.light(18));
        title.setForeground(Theme.TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        form.add(title);
        form.add(Components.vSpacer(16));

        categoryBox = new JComboBox<>(CAT_NAMES);
        brandField = makeField("Brand (e.g. NVIDIA, AMD, Corsair)");
        nameField = makeField("Name (e.g. RTX 4070 Super)");
        priceField = makeField("Price in PKR (e.g. 125000)");
        scoreField = makeField("Performance score (e.g. 88)");
        socketField = makeField("Socket type (e.g. LGA1700, AM5, AM4)");
        ddrField = makeField("DDR generation (e.g. DDR4, DDR5)");
        vramField = makeField("VRAM (e.g. 12GB) - GPUs only");
        capacityField = makeField("Capacity (e.g. 1TB, 16GB)");
        wattField = makeField("Wattage (e.g. 750) - PSUs only");
        efficiencyField = makeField("Efficiency (e.g. 80+ Gold) - PSUs only");

        form.add(makeLabeled("Category", categoryBox));
        form.add(Components.vSpacer(8));
        form.add(makeLabeled("Brand", brandField));
        form.add(Components.vSpacer(8));
        form.add(makeLabeled("Name", nameField));
        form.add(Components.vSpacer(8));
        form.add(makeLabeled("Price (PKR)", priceField));
        form.add(Components.vSpacer(8));
        form.add(makeLabeled("Performance Score", scoreField));
        form.add(Components.vSpacer(8));
        form.add(makeLabeled("Socket Type", socketField));
        form.add(Components.vSpacer(8));
        form.add(makeLabeled("DDR Generation", ddrField));
        form.add(Components.vSpacer(8));
        form.add(makeLabeled("VRAM", vramField));
        form.add(Components.vSpacer(8));
        form.add(makeLabeled("Capacity", capacityField));
        form.add(Components.vSpacer(8));
        form.add(makeLabeled("Wattage", wattField));
        form.add(Components.vSpacer(8));
        form.add(makeLabeled("Efficiency", efficiencyField));
        form.add(Components.vSpacer(16));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnRow.setOpaque(false);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton cancel = Components.secondaryButton("Cancel");
        cancel.addActionListener(e -> dispose());
        btnRow.add(cancel);

        JButton save = Components.primaryButton("Add part");
        save.addActionListener(e -> savePart());
        btnRow.add(save);

        form.add(btnRow);
        form.add(Box.createVerticalGlue());

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(Theme.BG);
        add(scroll, BorderLayout.CENTER);
    }

    private JTextField makeField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(Theme.regular(11));
        f.setForeground(Theme.TEXT);
        f.setCaretColor(Theme.ACCENT);
        f.setBackground(Theme.SURFACE_2);
        f.setBorder(BorderFactory.createCompoundBorder(
            new Components.RoundedBorder(Theme.BORDER, 1, 4),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    private JPanel makeLabeled(String label, JComponent field) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        JLabel l = new JLabel(label);
        l.setFont(Theme.medium(9));
        l.setForeground(Theme.TEXT_3);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(l);
        p.add(Components.vSpacer(2));
        p.add(field);
        return p;
    }

    private void savePart() {
        String brand = brandField.getText().trim();
        String name = nameField.getText().trim();
        String priceStr = priceField.getText().trim();
        String scoreStr = scoreField.getText().trim();

        if (brand.isEmpty() || name.isEmpty() || priceStr.isEmpty() || scoreStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Brand, name, price and score are required.");
            return;
        }

        int price, score;
        try {
            price = Integer.parseInt(priceStr);
            score = Integer.parseInt(scoreStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Price and score must be numbers.");
            return;
        }

        int catIndex = categoryBox.getSelectedIndex();
        int catId = CAT_IDS[catIndex];

        String socket = socketField.getText().trim();
        String ddr = ddrField.getText().trim();
        String vram = vramField.getText().trim();
        String capacity = capacityField.getText().trim();
        String wattStr = wattField.getText().trim();
        String efficiency = efficiencyField.getText().trim();

        Integer wattage = null;
        if (!wattStr.isEmpty()) {
            try { wattage = Integer.parseInt(wattStr); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Wattage must be a number."); return; }
        }

        Part p = new Part(0, catId, brand, name, price, score,
            socket.isEmpty() ? null : socket,
            ddr.isEmpty() ? null : ddr,
            null, null,
            vram.isEmpty() ? null : vram,
            null,
            capacity.isEmpty() ? null : capacity,
            null,
            wattage,
            efficiency.isEmpty() ? null : efficiency,
            null);

        PartDAO dao = new PartDAO();
        if (dao.addPart(p)) {
            saved = true;
            JOptionPane.showMessageDialog(this, name + " added successfully!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add part.");
        }
    }
}
