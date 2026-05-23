package com.project.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class DashboardGUI extends JFrame {

    private static final Color BG = new Color(10, 12, 20);
    private static final Color SIDEBAR = new Color(16, 20, 32);
    private static final Color CARD = new Color(22, 28, 44);
    private static final Color CARD_HOVER = new Color(30, 38, 56);
    private static final Color BORDER = new Color(35, 45, 65);
    private static final Color TEXT = new Color(240, 244, 250);
    private static final Color MUTED = new Color(140, 155, 180);
    private static final Color CYAN = new Color(0, 210, 230);
    private static final Color GREEN = new Color(0, 210, 130);
    private static final Color AMBER = new Color(250, 170, 20);
    private static final Color ROSE = new Color(240, 75, 85);
    private static final Color INDIGO = new Color(120, 110, 250);

    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private NavItem activeNav;
    private JLabel s1, s2, s3, s4;

    public DashboardGUI() {
        setTitle("PC Build Store");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1080, 700));

        content();
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(sidebar(), BorderLayout.WEST);
        root.add(content, BorderLayout.CENTER);

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        showView("DASH");
    }

    private JPanel sidebar() {
        JPanel sb = new JPanel();
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));
        sb.setBackground(SIDEBAR);
        sb.setPreferredSize(new Dimension(210, 0));
        sb.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));

        sb.add(logo());
        sb.add(spacer(20));
        sb.add(nav("DASH", "Dashboard", CYAN));
        sb.add(nav("BUILDS", "Build Catalog", GREEN));
        sb.add(nav("GPU", "GPU Upgrades", AMBER));
        sb.add(nav("BILL", "Billing", INDIGO));
        sb.add(nav("REPORTS", "Reports", ROSE));
        sb.add(spacer(0));
        sb.add(footer());

        return sb;
    }

    private JPanel logo() {
        JPanel p = panel(SIDEBAR);
        p.setBorder(BorderFactory.createEmptyBorder(22, 18, 18, 18));
        p.setMaximumSize(dim(210, 72));

        JLabel a = label("PC BUILD", CYAN, 18, Font.BOLD);
        JLabel b = label("STORE", TEXT, 18, Font.BOLD);
        p.add(a);
        p.add(b);
        return p;
    }

    private NavItem nav(String id, String text, Color c) {
        NavItem n = new NavItem(id, text, c);
        n.setMaximumSize(dim(210, 42));
        n.setAlignmentX(0);
        return n;
    }

    private JPanel footer() {
        JPanel p = panel(SIDEBAR);
        p.setBorder(BorderFactory.createEmptyBorder(10, 18, 14, 0));
        p.setMaximumSize(dim(210, 40));
        p.add(label("v1.0  |  Hanan", new Color(45, 55, 72), 10, Font.PLAIN));
        return p;
    }

    private JPanel content() {
        content.setBackground(BG);
        content.add(dashView(), "DASH");
        content.add(placeholder("Build Catalog", GREEN), "BUILDS");
        content.add(placeholder("GPU Upgrades", AMBER), "GPU");
        content.add(placeholder("Billing", INDIGO), "BILL");
        content.add(placeholder("Reports", ROSE), "REPORTS");
        return content;
    }

    private JPanel dashView() {
        JPanel v = panel(BG);
        v.setLayout(new BoxLayout(v, BoxLayout.Y_AXIS));
        v.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));

        JPanel stats = new JPanel(new java.awt.GridLayout(1, 4, 12, 0));
        stats.setBackground(BG);
        stats.setMaximumSize(dim(9999, 95));
        stats.setAlignmentX(0);

        s1 = stat(stats, "BUILDs", "0", CYAN);
        s2 = stat(stats, "REVENUE", "Rs.0", GREEN);
        s3 = stat(stats, "AVG SCORE", "0", AMBER);
        s4 = stat(stats, "BILLS", "0", INDIGO);

        v.add(stats);
        v.add(spacer(20));

        JLabel sec = label("MODULES", MUTED, 10, Font.BOLD);
        sec.setAlignmentX(0);
        v.add(sec);
        v.add(spacer(12));

        JPanel grid = new JPanel(new java.awt.GridLayout(2, 2, 12, 12));
        grid.setBackground(BG);
        grid.setAlignmentX(0);
        grid.add(modCard("Build Catalog", "Configure PC builds", GREEN, "BUILDS"));
        grid.add(modCard("GPU Upgrades", "Upgrade options", AMBER, "GPU"));
        grid.add(modCard("Billing", "Invoices and payments", INDIGO, "BILL"));
        grid.add(modCard("Reports", "Analytics and insights", ROSE, "REPORTS"));
        v.add(grid);

        loadStats();
        return v;
    }

    private JLabel stat(JPanel p, String t, String v, Color c) {
        JPanel card = card(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, c),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
            )
        ));

        JLabel l = label(t, c, 9, Font.BOLD);
        JLabel val = label(v, TEXT, 22, Font.BOLD);
        card.add(l);
        card.add(val);

        p.add(card);
        return val;
    }

    private JPanel modCard(String t, String d, Color c, String target) {
        JPanel card = card(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel title = label(t, TEXT, 15, Font.BOLD);
        JLabel desc = label(d, MUTED, 11, Font.PLAIN);
        JLabel arr = label(">", c, 16, Font.BOLD);
        arr.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel col = panel(CARD);
        col.add(title);
        col.add(desc);

        card.add(col);
        card.add(arr);

        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(c),
                    BorderFactory.createEmptyBorder(17, 17, 17, 17)
                ));
                card.setBackground(CARD_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    BorderFactory.createEmptyBorder(18, 18, 18, 18)
                ));
                card.setBackground(CARD);
            }
            public void mouseClicked(MouseEvent e) {
                showView(target);
            }
        });
        return card;
    }

    private JPanel placeholder(String t, Color c) {
        JPanel v = panel(BG);
        v.setLayout(new BoxLayout(v, BoxLayout.Y_AXIS));
        v.setBorder(BorderFactory.createEmptyBorder(32, 24, 0, 24));

        JLabel title = label(t, TEXT, 22, Font.BOLD);
        JLabel sub = label("Coming soon...", MUTED, 13, Font.PLAIN);
        v.add(title);
        v.add(sub);
        return v;
    }

    private void showView(String id) {
        cards.show(content, id);
        for (Component c : ((JPanel) getContentPane().getComponent(0)).getComponents()) {
            if (c instanceof NavItem) {
                NavItem n = (NavItem) c;
                n.setActive(n.id.equals(id));
            }
        }
    }

    private void loadStats() {
        SwingUtilities.invokeLater(() -> {
            try {
                var conn = java.sql.DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/pc_build_store?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                    "root", ""
                );
                var q = conn.prepareStatement("SELECT COUNT(*) FROM builds");
                var r = q.executeQuery();
                if (r.next()) s1.setText(String.valueOf(r.getInt(1)));

                q = conn.prepareStatement("SELECT COALESCE(SUM(final_price),0) FROM bills");
                r = q.executeQuery();
                if (r.next()) s2.setText("Rs." + String.format("%,d", r.getInt(1)));

                q = conn.prepareStatement("SELECT COALESCE(AVG(performance_score),0) FROM builds");
                r = q.executeQuery();
                if (r.next()) s3.setText(String.format("%.0f", r.getDouble(1)));

                q = conn.prepareStatement("SELECT COUNT(*) FROM bills");
                r = q.executeQuery();
                if (r.next()) s4.setText(String.valueOf(r.getInt(1)));

                conn.close();
            } catch (Exception e) {
                s1.setText("--");
                s2.setText("--");
                s3.setText("--");
                s4.setText("--");
            }
        });
    }

    class NavItem extends JPanel {
        final String id;
        private final Color accent;
        private boolean on = false;

        NavItem(String id, String text, Color accent) {
            this.id = id;
            this.accent = accent;
            setLayout(new BorderLayout());
            setBackground(SIDEBAR);
            setOpaque(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 14));

            JLabel l = label(text, MUTED, 13, Font.PLAIN);
            add(l);

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (!on) setBackground(CARD);
                }
                public void mouseExited(MouseEvent e) {
                    if (!on) setBackground(SIDEBAR);
                }
                public void mouseClicked(MouseEvent e) {
                    showView(id);
                }
            });
        }

        void setActive(boolean v) {
            on = v;
            setBackground(v ? CARD : SIDEBAR);
            setBorder(v
                ? BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 3, 0, 0, accent),
                    BorderFactory.createEmptyBorder(0, 15, 0, 14))
                : BorderFactory.createEmptyBorder(0, 18, 0, 14));
            JLabel l = (JLabel) getComponent(0);
            l.setForeground(v ? TEXT : MUTED);
            l.setFont(font(v ? Font.BOLD : Font.PLAIN, v ? 13 : 13));
        }
    }

    static JPanel panel(Color bg) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(bg);
        p.setOpaque(true);
        return p;
    }

    static JPanel card(Color bg) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setBackground(bg);
        p.setOpaque(true);
        return p;
    }

    static JLabel label(String t, Color fg, int size, int style) {
        JLabel l = new JLabel(t);
        l.setFont(font(style, size));
        l.setForeground(fg);
        l.setOpaque(false);
        return l;
    }

    static Font font(int style, int size) {
        return new Font("Segoe UI", style, size);
    }

    static Dimension dim(int w, int h) {
        return new Dimension(w, h);
    }

    static JPanel spacer(int h) {
        JPanel p = new JPanel();
        p.setBackground(BG);
        p.setOpaque(true);
        p.setPreferredSize(dim(0, h));
        p.setMaximumSize(dim(9999, h));
        return p;
    }
}
