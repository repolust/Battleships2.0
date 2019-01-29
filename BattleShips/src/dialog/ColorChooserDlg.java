/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialog;

import Client.LobbyGUI;
import java.awt.Color;
import java.awt.Dimension;

/**
 *
 * @author Leonardo
 * Erstellt am 19.4.2018
 */
public class ColorChooserDlg extends javax.swing.JDialog {


    private Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private int hoeheSchirm =  (int) screensize.getHeight();
    private int breiteSchirm = (int) screensize.getWidth();
    private Color c = null;

    public ColorChooserDlg(LobbyGUI parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setResizable(false);
    }
    public ColorChooserDlg(NewPlayerDlg parent, boolean modal) {
        super(parent, modal);
        setUndecorated(true);
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        cChooser = new javax.swing.JColorChooser();
        jPanel2 = new javax.swing.JPanel();
        btAuswaehlen = new javax.swing.JButton();
        btBeenden = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Farbe wählen");
        setSize(new java.awt.Dimension(1000, 1000));

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        cChooser.setBackground(new java.awt.Color(0, 153, 255));
        cChooser.setForeground(new java.awt.Color(255, 255, 255));
        cChooser.setToolTipText("");
        jPanel1.add(cChooser);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setLayout(new java.awt.GridLayout(1, 2));

        btAuswaehlen.setBackground(new java.awt.Color(0, 153, 255));
        btAuswaehlen.setFont(new java.awt.Font("Old English Text MT", 1, 36)); // NOI18N
        btAuswaehlen.setForeground(new java.awt.Color(255, 255, 255));
        btAuswaehlen.setText("Auswählen");
        btAuswaehlen.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));
        btAuswaehlen.setContentAreaFilled(false);
        btAuswaehlen.setOpaque(true);
        btAuswaehlen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAuswaehlenActionPerformed(evt);
            }
        });
        jPanel2.add(btAuswaehlen);

        btBeenden.setBackground(new java.awt.Color(0, 153, 255));
        btBeenden.setFont(new java.awt.Font("Old English Text MT", 1, 36)); // NOI18N
        btBeenden.setForeground(new java.awt.Color(255, 255, 255));
        btBeenden.setText("Beenden");
        btBeenden.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white, java.awt.Color.white));
        btBeenden.setContentAreaFilled(false);
        btBeenden.setOpaque(true);
        btBeenden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBeendenActionPerformed(evt);
            }
        });
        jPanel2.add(btBeenden);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btAuswaehlenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAuswaehlenActionPerformed
        c = this.cChooser.getColor();
        dispose();
    }//GEN-LAST:event_btAuswaehlenActionPerformed

    private void btBeendenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBeendenActionPerformed
        dispose();
    }//GEN-LAST:event_btBeendenActionPerformed
    public Color getC() {
        return c;
    }

    public boolean isC() {
        if (c != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ColorChooserDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ColorChooserDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ColorChooserDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ColorChooserDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ColorChooserDlg dialog = new ColorChooserDlg((LobbyGUI) new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAuswaehlen;
    private javax.swing.JButton btBeenden;
    private javax.swing.JColorChooser cChooser;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
