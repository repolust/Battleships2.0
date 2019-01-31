/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Beans.EinheitsVektor;
import Beans.Option;
import Beans.Position;
import Beans.Player;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import models.LobbyTableModel;
import models.SpielerTableModel;

/**
 *
 * @author Leonardo und Michael Erstellt am 18.4.2018
 */
public class LobbyGUI extends javax.swing.JFrame
{

    /**
     * Creates new form NewPlayerDlg
     */
    private Player p;

    private LobbyTableModel slm;
    private BattleShipsClient connection;
    private UpdateThread searchForUpdates;

    public LobbyGUI(Player p)
    {

        initComponents();
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.p = p;
        slm = new LobbyTableModel();
        this.jTablePlayers.setModel(slm);

        try
        {
            //Beim Server anmelden
            connection = BattleShipsClient.getTheInstance();
            connection.connect();

            searchForUpdates = new UpdateThread();
            searchForUpdates.start();

            connection.sendObject(p);
            
        } 
        catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(LobbyGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTablePlayers = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        btBereit = new javax.swing.JButton();
        btSpielStarten = new javax.swing.JButton();

        jScrollPane1.setViewportView(jTextPane1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lobby");
        setPreferredSize(new java.awt.Dimension(688, 387));
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Spieler", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Old English Text MT", 1, 14))); // NOI18N
        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jTablePlayers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String []
            {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTablePlayers);

        jPanel1.add(jScrollPane2);

        jPanel2.setLayout(new java.awt.GridLayout(1, 2));

        btBereit.setFont(new java.awt.Font("Old English Text MT", 1, 24)); // NOI18N
        btBereit.setText("Bereit machen");
        btBereit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btBereit.setContentAreaFilled(false);
        btBereit.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                onBereitMachen(evt);
            }
        });
        jPanel2.add(btBereit);

        btSpielStarten.setFont(new java.awt.Font("Old English Text MT", 1, 24)); // NOI18N
        btSpielStarten.setText("Beenden");
        btSpielStarten.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btSpielStarten.setContentAreaFilled(false);
        btSpielStarten.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btSpielBeenden(evt);
            }
        });
        jPanel2.add(btSpielStarten);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel7);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btSpielBeenden(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSpielBeenden
        try
        {
            connection.sendObject("imOut");
            connection.disconnect();
            this.dispose();
            System.exit(0);
        } catch (IOException ex)
        {
            Logger.getLogger(LobbyGUI.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(LobbyGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btSpielBeenden

    private void onBereitMachen(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onBereitMachen
        try
        {
            //Anmelden, vom Server restlichen Daten bekommen
            connection.sendObject("imReady");

            // Spieler muss vom Server noch Folgende Eigenschaften bekommen: position, pos, startPos, angle, Einheitsvektor
        } catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(LobbyGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_onBereitMachen

    public class UpdateThread extends Thread
    {

        public UpdateThread()
        {

        }

        @Override
        public void run()
        {
            while (!isInterrupted())
            {
                try
                {

                    //REquesrUpdate
                    Object obj = connection.getObject();
                    System.out.println("Neue Liste bekommen boy");
                    if (obj instanceof List)
                    {
                        List<Player> players = (List<Player>) obj;
                        slm.setPlayerList(players);
                     
//                        JOptionPane.showMessageDialog(null, "Table updated!");
                    }
                    else if(obj instanceof String)
                    {
                        String command = (String) obj;
                        if(command.equals("StartGame"))
                        {
                            //Startinformationen von Player anfordern
                            connection.sendObject("requestStartInformation");
                            Object objStart = connection.getObject();
                            
                            if(objStart instanceof Player)
                            {
                                Player p = (Player) objStart;
                                GameGUI gui = new GameGUI(p);
                                this.interrupt();
                            }
                        }
                    }

                } catch (IOException | ClassNotFoundException ex)
                {
//                    JOptionPane.showMessageDialog(null, "An error eccourd!","Oh nein",JOptionPane.ERROR_MESSAGE);
                    Logger.getLogger(LobbyGUI.class.getName()).log(Level.SEVERE, null, ex);
                }

                try
                {
                    Thread.sleep(1000);
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(LobbyGUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(LobbyGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(LobbyGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(LobbyGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(LobbyGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                LobbyGUI dialog = new LobbyGUI(null);
                dialog.addWindowListener(new java.awt.event.WindowAdapter()
                {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e)
                    {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btBereit;
    private javax.swing.JButton btSpielStarten;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTablePlayers;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
