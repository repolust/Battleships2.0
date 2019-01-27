/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dialog;

import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;

/**
 *
 * @author Michael
 * Erstellt am 25.4.2018
 */
public class ShipChooserDlg extends javax.swing.JDialog {

    /**
     * Creates new form ShipChooserDlg
     */
    private Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    private int hoeheSchirm = (int) screensize.getHeight();
    private int breiteSchirm = (int) screensize.getWidth();

    private Image selectedShip = null;
    private String shipName = "";
    private boolean isok = false;

    private String path = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "bilder"
            + File.separator + "playShip1.png";
    
    public ShipChooserDlg(LobbyDlg parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setBounds(breiteSchirm / 3, hoeheSchirm / 3, breiteSchirm / 3, hoeheSchirm / 3);
        this.setResizable(false);
    }

    public Image getSelectedShip() {  
            return selectedShip;
    }
    
    public String getshipName()
    {
        return shipName;
    }
    
      public boolean issok() 
    {
        return isok;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        playShip1 = new javax.swing.JLabel();
        playShip2 = new javax.swing.JLabel();
        playShip3 = new javax.swing.JLabel();
        playShip4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btWaehlen = new javax.swing.JButton();
        btBeenden = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel3.setLayout(new java.awt.GridLayout(2, 2));

        playShip1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        playShip1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/showShip1.png"))); // NOI18N
        playShip1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onShip1Clicked(evt);
            }
        });
        jPanel3.add(playShip1);

        playShip2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/showShip2.png"))); // NOI18N
        playShip2.setEnabled(false);
        playShip2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onShip2Clicked(evt);
            }
        });
        jPanel3.add(playShip2);

        playShip3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/showShip3.png"))); // NOI18N
        playShip3.setEnabled(false);
        playShip3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onShip3Clicked(evt);
            }
        });
        jPanel3.add(playShip3);

        playShip4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Bilder/showShip4.png"))); // NOI18N
        playShip4.setEnabled(false);
        playShip4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onShip4Clicked(evt);
            }
        });
        jPanel3.add(playShip4);

        getContentPane().add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.GridLayout(1, 0));

        btWaehlen.setFont(new java.awt.Font("Old English Text MT", 1, 24)); // NOI18N
        btWaehlen.setText("Auswählen");
        btWaehlen.setToolTipText("");
        btWaehlen.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btWaehlen.setContentAreaFilled(false);
        btWaehlen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAuswaehlen(evt);
            }
        });
        jPanel4.add(btWaehlen);

        btBeenden.setFont(new java.awt.Font("Old English Text MT", 1, 24)); // NOI18N
        btBeenden.setText("Beenden");
        btBeenden.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        btBeenden.setContentAreaFilled(false);
        btBeenden.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBeendenActionPerformed(evt);
            }
        });
        jPanel4.add(btBeenden);

        getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public void clearBorders()
    {
        this.playShip1.setBorder(null);
        this.playShip2.setBorder(null);
        this.playShip3.setBorder(null);
        this.playShip4.setBorder(null);
    }
    private void btBeendenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBeendenActionPerformed
        isok = false;
        this.dispose();
    }//GEN-LAST:event_btBeendenActionPerformed


    private void playShip1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playShip1MouseClicked

    }//GEN-LAST:event_playShip1MouseClicked

    private void lbSchiff1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbSchiff1MouseClicked

    }//GEN-LAST:event_lbSchiff1MouseClicked


    private void onShip1Clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onShip1Clicked
       clearBorders();
        this.playShip1.setBorder(BorderFactory.createEtchedBorder(null,null));  
        path = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "bilder"
            + File.separator + "playShip1.png";
        
        shipName = "Wellenspalter";
    }//GEN-LAST:event_onShip1Clicked

    private void onShip2Clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onShip2Clicked
//        clearBorders();
//        this.playShip2.setBorder(BorderFactory.createEtchedBorder(null,null));    
//        
//        path = System.getProperty("user.dir")
//            + File.separator + "src"
//            + File.separator + "bilder"
//            + File.separator + "playShip2.png";
//        
//        shipName = "Banditenmobil";
    }//GEN-LAST:event_onShip2Clicked

    private void onShip3Clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onShip3Clicked
//        clearBorders();
//        this.playShip3.setBorder(BorderFactory.createEtchedBorder(null,null));    
//        
//        path = System.getProperty("user.dir")
//            + File.separator + "src"
//            + File.separator + "bilder"
//            + File.separator + "playShip3.png";
//        
//        shipName = "Windzerfetzer";
    }//GEN-LAST:event_onShip3Clicked

    private void onShip4Clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onShip4Clicked
//       clearBorders();
//        this.playShip4.setBorder(BorderFactory.createEtchedBorder(null,null));    
//        path = System.getProperty("user.dir")
//            + File.separator + "src"
//            + File.separator + "bilder"
//            + File.separator + "playShip4.png";
//        
//        shipName = "Absolute Zerstörung";
    }//GEN-LAST:event_onShip4Clicked

    private void onAuswaehlen(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAuswaehlen
        try {      
            selectedShip = ImageIO.read(new File(path));
            
            isok = true;
            dispose();
        } catch (IOException ex) {
            isok = false;
        }
        
        
        
    }//GEN-LAST:event_onAuswaehlen


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
            java.util.logging.Logger.getLogger(ShipChooserDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ShipChooserDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ShipChooserDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ShipChooserDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ShipChooserDlg dialog = new ShipChooserDlg((LobbyDlg) new javax.swing.JDialog(), true);
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
    private javax.swing.JButton btBeenden;
    private javax.swing.JButton btWaehlen;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel playShip1;
    private javax.swing.JLabel playShip2;
    private javax.swing.JLabel playShip3;
    private javax.swing.JLabel playShip4;
    // End of variables declaration//GEN-END:variables
}