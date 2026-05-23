package com.project.ui;

import javax.swing.*;
import java.awt.*;

public class DashboardGUI extends JFrame {

    public DashboardGUI() {
        setTitle("PC Build Store - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("PC Build Store Management System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        mainPanel.add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnBuildCatalog = new JButton("Build Catalog");
        JButton btnGpuUpgrades = new JButton("GPU Upgrades");
        JButton btnBilling = new JButton("Billing");
        JButton btnReports = new JButton("Reports");

        Dimension btnSize = new Dimension(200, 50);
        for (JButton btn : new JButton[]{btnBuildCatalog, btnGpuUpgrades, btnBilling, btnReports}) {
            btn.setPreferredSize(btnSize);
            btn.setFont(new Font("Arial", Font.PLAIN, 16));
        }

        gbc.gridx = 0; gbc.gridy = 0;
        centerPanel.add(btnBuildCatalog, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        centerPanel.add(btnGpuUpgrades, gbc);
        gbc.gridx = 0; gbc.gridy = 2;
        centerPanel.add(btnBilling, gbc);
        gbc.gridx = 0; gbc.gridy = 3;
        centerPanel.add(btnReports, gbc);

        btnBuildCatalog.addActionListener(e -> JOptionPane.showMessageDialog(this, "Build Catalog coming soon!"));
        btnGpuUpgrades.addActionListener(e -> JOptionPane.showMessageDialog(this, "GPU Upgrades coming soon!"));
        btnBilling.addActionListener(e -> JOptionPane.showMessageDialog(this, "Billing coming soon!"));
        btnReports.addActionListener(e -> JOptionPane.showMessageDialog(this, "Reports coming soon!"));

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        JLabel footer = new JLabel("Developed by Abdul Hanan & Team", SwingConstants.CENTER);
        footer.setFont(new Font("Arial", Font.ITALIC, 12));
        mainPanel.add(footer, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }
}
