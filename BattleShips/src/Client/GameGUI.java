/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import Beans.EinheitsVektor;
import Beans.Kugel;
import Beans.Player;
import Beans.Position;
import Beans.Treffer;
import Client.BL.Controlls;
import Client.BL.GameBL;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import sound.MusikThread;

/**
 *
 * @author michi
 */
public class GameGUI extends javax.swing.JFrame {

    /**
     * Creates new form GameGUI
     */
    private GameBL bl;
    private Controlls controlls = new Controlls();
    
     private final String cannonPath = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "sound"
            + File.separator + "cannon.mp3";
    
    private final String hitPath = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "sound"
            + File.separator + "hitmarker.mp3";
    
    private final String crashPath = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "sound"
            + File.separator + "crash.mp3";
    
    private final String winSoundPath = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "sound"
            + File.separator + "winSound.mp3";
    
    private final String airhornPath = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "sound"
            + File.separator + "airhorn.mp3";
    
    private KeyListener jpGameListener = new KeyAdapterImpl();
    
    private int maxX = (int) this.jpGame.getSize().getWidth();
    private int maxY = (int) this.jpGame.getSize().getHeight();
    
    private Player p;
    
    @Override
    public void paint(Graphics grphcs)
    {
        super.paint(grphcs); //To change body of generated methods, choose Tools | Templates.

        if (bl != null)
        {
            bl.drawPlayers();

        }
    }
    
    public GameGUI(Player p) {
        
        this.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        initComponents();
        this.p = p;
        
        jpGame.addKeyListener(jpGameListener);
        jpGame.setFocusable(true);
        
        bl = new GameBL(this.jpGame);
//        bl = new GameBL(this.jpGame, schiffListe);
        
        playSound(airhornPath);
    }

     public void playSound(String path)
        {
            MusikThread sound = new MusikThread(path);
            sound.start();
        }
    
    public class zeichenThread extends Thread
    {

        private JFrame gui;
        
        private LinkedList<Player> schiffListe = new LinkedList<Player>();
        private LinkedList<Kugel> kugelListe = new LinkedList<Kugel>();
        private Player p;
        private JLabel lbName,lbHealth,lbMunition;
        
        private BattleShipsClient connection;
        
        public zeichenThread(JFrame gui,Player p,JLabel lbName, JLabel lbHealth,JLabel lbMunition)
        {
            this.gui = gui;
            this.p = p;
            this.lbName = lbName;
            this.lbHealth = lbHealth;
            this.lbMunition = lbMunition;
            connection = BattleShipsClient.getTheInstance();

        }

        @Override
        public void run()
        {
            while (!isInterrupted())
            {
//                try
//                {

                    lbName.setText(p.getName());
                    lbHealth.setText(""+p.getLeben());
                    lbMunition.setText(""+p.getMunition());

// Der Thread überprüft ob die jeweiligen Tasten in der Liste der Klasse Controlls enthalten sind und führt demensprechen die Aktionen aus     

//-----------------------------------Controlls---------------------------------
//-----------------------------------Spieler 1 ---------------------------------

                    if (p.getCurrentAngle() >= 360 || p.getCurrentAngle() <= -360) // Winkel zurücksetzen
                    {
                        p.setCurrentAngle(0);
                    }

                    if (controlls.containsKey(KeyEvent.VK_W) && !controlls.containsKey(KeyEvent.VK_A) && !controlls.containsKey(KeyEvent.VK_D))// W Gerade aus
                    {
                        checkAndIncrease();

                    }
                    if (controlls.containsKey(KeyEvent.VK_W) && controlls.containsKey(KeyEvent.VK_A) && controlls.containsKey(KeyEvent.VK_D))// W A D Gerade aus
                    {
                        checkAndIncrease();

                    }
                    if (controlls.containsKey(KeyEvent.VK_W) && controlls.containsKey(KeyEvent.VK_A) && !controlls.containsKey(KeyEvent.VK_D))// W A Links Kurve
                    {
                        checkAndIncrease();
                        EinheitsVektor k = p.getDirection();
                        k.rotateEinheitsVektor(-p.getRotation());
                        p.setDirection(k);
                        p.setCurrentAngle(p.getCurrentAngle() - p.getRotation());

                    }
                    if (controlls.containsKey(KeyEvent.VK_W) && controlls.containsKey(KeyEvent.VK_D) && !controlls.containsKey(KeyEvent.VK_A))// W D Rechts Kurve
                    {
                        checkAndIncrease();
                        EinheitsVektor k = p.getDirection();
                        k.rotateEinheitsVektor(p.getRotation());
                        p.setDirection(k);
                        p.setCurrentAngle(p.getCurrentAngle() + p.getRotation());

                    }
                    if (controlls.containsKey(KeyEvent.VK_SPACE)) // Schuss
                    {

                        EinheitsVektor einVLinks = new EinheitsVektor(schiffListe.get(0).getDirection().getX(), schiffListe.get(0).getDirection().getY()); 
                        EinheitsVektor einVRechts = new EinheitsVektor(schiffListe.get(0).getDirection().getX(), schiffListe.get(0).getDirection().getY());

                        einVLinks.rotateEinheitsVektor(-90); 
                        einVRechts.rotateEinheitsVektor(90);

                        Rectangle hitbox = new Rectangle(p.getHitbox().x, p.getHitbox().y, p.getHitbox().width, p.getHitbox().height);           

                        for (int i = 0; i <= 14; i += 7)
                        {
                            Position posSL = new Position(hitbox.getCenterX() - 3, hitbox.getCenterY() - 3);
                            Position posSR = new Position(hitbox.getCenterX() + 3, hitbox.getCenterY() + 3);

                            if ((p.getCurrentAngle() > 70 && p.getCurrentAngle() < 110 || p.getCurrentAngle() > 250 && p.getCurrentAngle() < 290) || (p.getCurrentAngle() < -70 && p.getCurrentAngle() > -110 || p.getCurrentAngle() < -250 && p.getCurrentAngle() > -290))
                            {
                                posSL.increaseX(i);
                                posSR.increaseX(i);
                            } else if ((p.getCurrentAngle() > 340 && p.getCurrentAngle() <= 360 || p.getCurrentAngle() > 160 && p.getCurrentAngle() <= 200 || p.getCurrentAngle() >= 0 && p.getCurrentAngle() < 20) || (p.getCurrentAngle() < -340 && p.getCurrentAngle() >= -360 || p.getCurrentAngle() < -160 && p.getCurrentAngle() >= -200 || p.getCurrentAngle() <= -0 && p.getCurrentAngle() > -20))
                            {
                                posSL.increaseY(i);
                                posSR.increaseY(i);
                            } else if ((p.getCurrentAngle() > 20 && p.getCurrentAngle() < 70 || p.getCurrentAngle() > 200 && p.getCurrentAngle() < 250) || (p.getCurrentAngle() < -290 && p.getCurrentAngle() > -340 || p.getCurrentAngle() < -110 && p.getCurrentAngle() > -160))
                            {
                                posSL.increaseX(i * (-1));
                                posSR.increaseX(i * (-1));
                                posSL.increaseY(i);
                                posSR.increaseY(i);
                            } else
                            {
                                posSL.increaseX(i);
                                posSR.increaseX(i);
                                posSL.increaseY(i);
                                posSR.increaseY(i);
                            }
                            if (!(p.getMunition() <= 0))
                            {

                                try {
                                    connection.sendObject(new Kugel(einVLinks, posSL, 5, 1));
                                    connection.sendObject(new Kugel(einVRechts, posSR, 5, 1));
                                } catch (IOException ex) {
                                    Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (ClassNotFoundException ex) {
                                    Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                                p.setMunition(p.getMunition() - 2);

                            }
                        }
                        
                        controlls.removeKey(KeyEvent.VK_SPACE);
                        
                        if(p.getMunition() != 0)
                        {
                             playSound(cannonPath);
                             
                        }
                        
                        
                    }


//-----------------------------------/Controlls---------------------------------

                    //Falls beide SChiffe keine Munition haben, bekommt das Schiff das mehr Leben hat einen leichten Geschwindigkeitsboost
//                    if (!isSpeedIncreased) {
//                        
//                    if(p.getMunition() == 0 && p2.getMunition() == 0)
//                    {
//                        if(p.getLeben() > p2.getLeben())
//                        {
//                            p.setSpeed(p.getSpeed()+2);
//                            isSpeedIncreased = true;
//                        }
//                        else
//                        {
//                            p2.setSpeed(p2.getSpeed()+2);
//                            isSpeedIncreased = true;
//                        }
//                    }
//                    }


//-----------------------------------Kugel bewegen und entfernen---------------------------------
                    int removeIndex = -1;

                    for (Kugel k : kugelListe)
                    {

                        k.getPos().increaseX(k.getEinheintsVektor().getX() * 20);
                        k.getPos().increaseY(k.getEinheintsVektor().getY() * 20);

                        if (k.getPos().getX() > maxX || k.getPos().getX() < 0)
                        {
                            removeIndex = kugelListe.indexOf(k);

                        }
                        if (k.getPos().getY() > maxY || k.getPos().getY() < 0)
                        {
                            removeIndex = kugelListe.indexOf(k);

                        }

                    }

                    if (removeIndex != -1)
                    {
                        kugelListe.remove(removeIndex);
                    }
//-----------------------------------//Kugel bewegen---------------------------------

//-----------------------------------Collision Detection---------------------------------
//                    CheckIfHit check = new CheckIfHit(kugelListe, schiffListe);
//
//                    if (check.checkCollision()) // Schiffe fahren zusammen
//                    {
//                        playSound(crashPath);
//                        
//                        p.setLeben(p.getLeben() - 20);
//                        p2.setLeben(p2.getLeben() - 20);
// 
////                   ------------------Schiffe zurücksetzen----------------                     
//                        pos1 = new Position(300, (maxY / 2 - 35));
//                        pos2 = new Position((maxX - 390), (maxY / 2 - 35));
//                        
//                        p.setP(pos1);
//                        p.setCurrentAngle(90);
//                        p.setDirection(new EinheitsVektor(1, 0));
//
//                        p2.setP(pos2);
//                        p2.setCurrentAngle(270);
//                        p2.setDirection(new EinheitsVektor(-1, 0));
//
//                    }
//
//                    if (check.checkIfHit() != null)//Kanonenkugel hat getroffen
//                    {
//                        playSound(hitPath);
//                        
//                        Treffer t = check.checkIfHit();
//                        kugelListe.remove((t.getKugelIndex()));
//
//                    }
                    
//                    if(p.getLeben() <= 0 && p2.getLeben() <= 0)// Unentschieden
//                    {
//                        
//                        gui.dispose();
//                         
//                        WinnerDlg wdlg = new WinnerDlg(new javax.swing.JFrame(),true,"Unentschieden","");
//                        wdlg.setVisible(true);
//
//                        this.interrupt();
//                        break;
//                    }
//                    
//                    else if (p.getLeben() <= 0)//Spieler 2 gewinnt
//                    {
//                        
//                        gui.dispose();
//                         
//                        playSound(winSoundPath);
//                        
//                        WinnerDlg wdlg = new WinnerDlg(new javax.swing.JFrame(),true,"Du siegts !", p2.getName());
//                        wdlg.setVisible(true);
//
//                        this.interrupt();
//                        break;
//                        
//                    } else if (p2.getLeben() <= 0)//Spieler 1 gewinnt
//                    {
//                        
//                        gui.dispose();
//                        
//                        playSound(winSoundPath);
//                        
//                        WinnerDlg wdlg = new WinnerDlg(new javax.swing.JFrame(),true,"Du siegts!",p.getName());
//                        wdlg.setVisible(true);
//                        
//                        this.interrupt();
//                        break;
//                    }



                    try {
                        //----------------------------BL zeichnet------------------------------------
                        connection.sendObject(p);
                        
                    } catch (IOException ex) {
                        Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, ex);
                    }
//                    
//                    Thread.sleep(10);
               } 
//                    catch (InterruptedException ex)
//                {
//                    Logger.getLogger(GameGUI.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }

        }

        public void checkAndIncrease()//Bewegt Spieler1 und sorgt dafür das man über den Rand fahren kann
        {

            if (p.getP().getX() <= 0)
            {
                p.getP().setX(maxX - 1);
            } else if (p.getP().getX() >= maxX)
            {
                p.getP().setX(1);
            } else if (p.getP().getY() <= 0)
            {
                p.getP().setY(maxY - 1);
            } else if (p.getP().getY() >= maxY)
            {
                p.getP().setY(1);
            } else
            {
                p.getP().increaseY(p.getDirection().getY() * p.getSpeed());
                p.getP().increaseX(p.getDirection().getX() * p.getSpeed());
            }
        }

        
        public void playSound(String path)
        {
            MusikThread sound = new MusikThread(path);
            sound.start();
        }
    }
     
     
     private class KeyAdapterImpl extends KeyAdapter //KeyListener
    {

        //Wenn eine Taste gedrückt wird, dann wird sie in der Klasse Controlls gespeichtert
        //Wenn man die Taste wieder loslässt wird diese wieder aus Controlls entfernt
        
        public KeyAdapterImpl()
        {
            
        }

        @Override
        public void keyPressed(KeyEvent evt)
        {
            switch (evt.getKeyCode())
            {
                case KeyEvent.VK_A:
                    System.out.println("Pressed: a");
                    controlls.addKey(KeyEvent.VK_A);
                    break;
                case KeyEvent.VK_W:
                    System.out.println("Pressed: w");
                    controlls.addKey(KeyEvent.VK_W);
                    break;
                case KeyEvent.VK_S:
                    System.out.println("Pressed: s");
                    controlls.addKey(KeyEvent.VK_S);
                    break;
                case KeyEvent.VK_D:
                    System.out.println("Pressed: d");
                    controlls.addKey(KeyEvent.VK_D);
                    break;
                case KeyEvent.VK_LEFT:
                    System.out.println("Pressed: left");
                    controlls.addKey(KeyEvent.VK_LEFT);
                    break;
                case KeyEvent.VK_UP:
                    System.out.println("Pressed: up");
                    controlls.addKey(KeyEvent.VK_UP);
                    break;
                case KeyEvent.VK_DOWN:
                    System.out.println("Pressed: down");
                    controlls.addKey(KeyEvent.VK_DOWN);
                    break;
                case KeyEvent.VK_RIGHT:
                    System.out.println("Pressed: right");
                    controlls.addKey(KeyEvent.VK_RIGHT);
                    break;
                case KeyEvent.VK_SPACE:
                    System.out.println("# space #");
                    controlls.addKey(KeyEvent.VK_SPACE);// adden
                    break;
                case KeyEvent.VK_ENTER:
                    System.out.println("**enter**");
                    controlls.addKey(KeyEvent.VK_ENTER);// adden
                    break;
            }

        }

        @Override
        public void keyReleased(KeyEvent evt)
        {
            switch (evt.getKeyCode())
            {
                case KeyEvent.VK_A:
                    System.out.println("Released: a");
                    controlls.removeKey(KeyEvent.VK_A);
                    break;
                case KeyEvent.VK_W:
                    System.out.println("Released: w");
                    controlls.removeKey(KeyEvent.VK_W);
                    break;
                case KeyEvent.VK_S:
                    System.out.println("Released: s");
                    controlls.removeKey(KeyEvent.VK_S);
                    break;
                case KeyEvent.VK_D:
                    System.out.println("Released: d");
                    controlls.removeKey(KeyEvent.VK_D);
                    break;
                
                case KeyEvent.VK_LEFT:
                    System.out.println("Released: left");
                    controlls.removeKey(KeyEvent.VK_LEFT);
                    break;
                case KeyEvent.VK_UP:
                    System.out.println("Released: up");
                    controlls.removeKey(KeyEvent.VK_UP);
                    break;
                case KeyEvent.VK_DOWN:
                    System.out.println("Released: down");
                    controlls.removeKey(KeyEvent.VK_DOWN);
                    break;
                case KeyEvent.VK_RIGHT:
                    System.out.println("Released: right");
                    controlls.removeKey(KeyEvent.VK_RIGHT);
                    break;
                case KeyEvent.VK_ESCAPE:
                    System.exit(0);
                    controlls.addKey(KeyEvent.VK_ESCAPE);
                    break;
            }

        }
     }
     
     
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbName = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lbLeben = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lbMunition = new javax.swing.JLabel();
        jpGame = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(102, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(32767, 100));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 39));
        jPanel1.setPreferredSize(new java.awt.Dimension(659, 80));
        jPanel1.setLayout(new java.awt.GridLayout(1, 6));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Name:");
        jPanel1.add(jLabel1);

        lbName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel1.add(lbName);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Leben:");
        jPanel1.add(jLabel4);

        lbLeben.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel1.add(lbLeben);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Munition:");
        jPanel1.add(jLabel3);

        lbMunition.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jPanel1.add(lbMunition);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        javax.swing.GroupLayout jpGameLayout = new javax.swing.GroupLayout(jpGame);
        jpGame.setLayout(jpGameLayout);
        jpGameLayout.setHorizontalGroup(
            jpGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 659, Short.MAX_VALUE)
        );
        jpGameLayout.setVerticalGroup(
            jpGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 315, Short.MAX_VALUE)
        );

        getContentPane().add(jpGame, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(GameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GameGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameGUI(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jpGame;
    private javax.swing.JLabel lbLeben;
    private javax.swing.JLabel lbMunition;
    private javax.swing.JLabel lbName;
    // End of variables declaration//GEN-END:variables
}
