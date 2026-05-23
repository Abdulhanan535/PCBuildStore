package com.project.ui;

import javax.swing.JOptionPane;

public class DashboardGUI extends javax.swing.JFrame {

    public DashboardGUI() {
        initComponents();
        applyCustomStyles();
        setVisible(true);
    }

    private void applyCustomStyles() {
        // Add padding around the main content so it doesn't stick to the edges
        pnlContent.setBorder(javax.swing.BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Make the buttons look modern and sleek!
        javax.swing.JButton[] buttons = {btnBuildCatalog, btnGpuUpgrades, btnBilling, btnReports};
        java.awt.Color btnColor = new java.awt.Color(41, 128, 185); // Professional blue
        java.awt.Color hoverColor = new java.awt.Color(52, 152, 219); // Lighter blue for hover
        
        for (javax.swing.JButton btn : buttons) {
            btn.setBackground(btnColor);
            btn.setForeground(java.awt.Color.WHITE);
            btn.setFocusPainted(false);
            // Using a compound border to give it some internal padding
            btn.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 97, 141), 2),
                javax.swing.BorderFactory.createEmptyBorder(15, 10, 15, 10)
            ));
            btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
            
            // Add hover effects
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    btn.setBackground(hoverColor);
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    btn.setBackground(btnColor);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlHeader = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        pnlContent = new javax.swing.JPanel();
        pnlModules = new javax.swing.JPanel();
        btnBuildCatalog = new javax.swing.JButton();
        btnGpuUpgrades = new javax.swing.JButton();
        btnBilling = new javax.swing.JButton();
        btnReports = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PC Build Store Management System");
        setBackground(new java.awt.Color(18, 22, 30));
        setMinimumSize(new java.awt.Dimension(1100, 750));

        pnlHeader.setBackground(new java.awt.Color(26, 42, 64));
        pnlHeader.setPreferredSize(new java.awt.Dimension(1100, 100));
        pnlHeader.setLayout(new java.awt.BorderLayout());

        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 26)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("  Dashboard & Reports");
        pnlHeader.add(lblTitle, java.awt.BorderLayout.WEST);

        getContentPane().add(pnlHeader, java.awt.BorderLayout.NORTH);

        pnlContent.setBackground(new java.awt.Color(18, 22, 30));
        pnlContent.setLayout(new java.awt.GridLayout(2, 1, 0, 20));

        pnlModules.setBackground(new java.awt.Color(18, 22, 30));
        pnlModules.setLayout(new java.awt.GridLayout(1, 4, 15, 0));

        btnBuildCatalog.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnBuildCatalog.setText("Build Catalog");
        btnBuildCatalog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuildCatalogActionPerformed(evt);
            }
        });
        pnlModules.add(btnBuildCatalog);

        btnGpuUpgrades.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnGpuUpgrades.setText("GPU Upgrades");
        btnGpuUpgrades.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGpuUpgradesActionPerformed(evt);
            }
        });
        pnlModules.add(btnGpuUpgrades);

        btnBilling.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnBilling.setText("Billing");
        btnBilling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBillingActionPerformed(evt);
            }
        });
        pnlModules.add(btnBilling);

        btnReports.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnReports.setText("Reports");
        btnReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportsActionPerformed(evt);
            }
        });
        pnlModules.add(btnReports);

        pnlContent.add(pnlModules);

        getContentPane().add(pnlContent, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuildCatalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuildCatalogActionPerformed
        JOptionPane.showMessageDialog(this, "Build Catalog opening...");
    }//GEN-LAST:event_btnBuildCatalogActionPerformed

    private void btnGpuUpgradesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGpuUpgradesActionPerformed
        JOptionPane.showMessageDialog(this, "GPU Upgrades opening...");
    }//GEN-LAST:event_btnGpuUpgradesActionPerformed

    private void btnBillingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBillingActionPerformed
        JOptionPane.showMessageDialog(this, "Billing opening...");
    }//GEN-LAST:event_btnBillingActionPerformed

    private void btnReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportsActionPerformed
        JOptionPane.showMessageDialog(this, "Reports opening...");
    }//GEN-LAST:event_btnReportsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBilling;
    private javax.swing.JButton btnBuildCatalog;
    private javax.swing.JButton btnGpuUpgrades;
    private javax.swing.JButton btnReports;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel pnlContent;
    private javax.swing.JPanel pnlHeader;
    private javax.swing.JPanel pnlModules;
    // End of variables declaration//GEN-END:variables
}
