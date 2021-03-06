/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.BL;

import Client.*;
import Beans.EinheitsVektor;
import Beans.Kugel;
import Beans.Player;
import Beans.Position;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Team Erstellt am 11.4.2018
 */
public class GameBL {

    private int maxX, maxY;
    private Graphics g;
    private JPanel jpGame;

    private BufferedImage bufferedImage;

    private List<Player> schiffListe = new LinkedList();
    private List<Kugel> kugelListe = new LinkedList();

    private BattleShipsClient bss;

    private String path = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "bilder"
            + File.separator + "playShip1.png";
    private Image ship;
//    private Position startPos1 = new Position(300, (maxY / 2 - 35));
//    
//    private boolean startToDraw = false;

    public GameBL(JPanel jpGame, int maxX, int maxY) {
        g = jpGame.getGraphics();
        g.clearRect(0, 0, maxX, maxY);
        bufferedImage = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_ARGB);
        this.jpGame = jpGame;
        this.maxX = maxX;
        this.maxY = maxY;
        try {
            ship = ImageIO.read(new File(path));
        } catch (IOException ex) {
            Logger.getLogger(GameBL.class.getName()).log(Level.SEVERE, null, ex);
        }

        bss = BattleShipsClient.getTheInstance();

        ServerCommunicationThread connection = new ServerCommunicationThread();
        DrawThread drawThread = new DrawThread();

        connection.start();
        drawThread.start();

    }

//    public void initMyInits()//Initialisiert die Startwerte
//    {
//        maxX = this.jpGame.getWidth();
//        maxY = this.jpGame.getHeight();
//
//        g = this.jpGame.getGraphics();
//        g.clearRect(0, 0, maxX, maxY);
//
//        bufferedImage = new BufferedImage(maxX, maxY, BufferedImage.TYPE_INT_ARGB);
//    }
    public void draw()//Zeichnet alles
    {
        this.drawShips();
        this.drawKugeln();
        this.drawText();
        g.drawImage(bufferedImage, 0, 0, null);
    }

    public void drawText() {//zeichnet Infos wie Name mit Farbe
        Graphics2D g2d = bufferedImage.createGraphics();
        for(Player p:schiffListe){
            Font test = new Font("Arial",Font.BOLD,14);
            g2d.setFont(test);
            g2d.setColor(p.getC());
            Position position = p.getP();
            g2d.drawString(p.getName(), position.getXInt(), position.getYInt());
        }
        
    }

    public void drawShips()//Zeichnet die SChiffe
    {
        if (schiffListe != null && !schiffListe.isEmpty()) {
            Graphics2D g2d = bufferedImage.createGraphics();
            g2d.clearRect(0, 0, maxX, maxY);
            g2d.setColor(new Color(62, 208, 245));
            g2d.fillRect(0, 0, maxX, maxY);

            for (Player p : schiffListe) {

//                //-----------Position Point-------
//                g.setColor(Color.red);
//                g.fillOval(p.getP().getXInt(), p.getP().getYInt(), 8, 8);
//                g.setColor(Color.black);
//                //-----------/Position Point-------
//     
//                
//                //-----------Hitbox-------
//                Rectangle hitbox = p.getHitbox();
//                g.drawRect((int) Math.round(hitbox.x), (int) Math.round(hitbox.y), hitbox.width, hitbox.height); 
//                //-----------/Hitbox-------
                //-----------Rotate----------------
                AffineTransform origXform1 = g2d.getTransform();
                AffineTransform newXform1 = (AffineTransform) (origXform1.clone());
                int xRot1 = p.getP().getXInt() + (p.getWidth() / 2);
                int yRot1 = p.getP().getYInt() + (p.getHeight() / 2);
                newXform1.rotate(Math.toRadians(p.getCurrentAngle()), xRot1, yRot1);
                g2d.setTransform(newXform1);
                g2d.drawImage(ship, p.getP().getXInt(), p.getP().getYInt(), null);
                g2d.setTransform(origXform1);
                //-----------/Rotate---------------
            }
        }
    }

    public void drawKugeln()//Zeichnet die Kugeln
    {
        if (kugelListe != null && !kugelListe.isEmpty()) {
            Graphics2D g2d = bufferedImage.createGraphics();

            g2d.setColor(Color.BLACK);
            for (Kugel k : kugelListe) {
                g2d.fillOval(k.getPos().getXInt(), k.getPos().getYInt(), k.getGroesse(), k.getGroesse());
            }
        }
    }

    public void startdrawPlayers(List<Player> players)//Zeichnet die Schiffe das erste mal. Wird von der Paint-Methode aus der GUI am start aufgerufen
    {
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.clearRect(0, 0, maxX, maxY);
        g2d.setColor(new Color(62, 208, 245));
        g2d.fillRect(0, 0, maxX, maxY);

        for (Player p : players) {

            AffineTransform origXform1 = g2d.getTransform();
            AffineTransform newXform1 = (AffineTransform) (origXform1.clone());
            int xRot1 = p.getP().getXInt() + (p.getWidth() / 2);
            int yRot1 = p.getP().getYInt() + (p.getHeight() / 2);
            newXform1.rotate(Math.toRadians(p.getCurrentAngle()), xRot1, yRot1);
            g2d.setTransform(newXform1);
            g2d.drawImage(ship, p.getP().getXInt(), p.getP().getYInt(), null);
            g2d.setTransform(origXform1);
            //-----------/Rotate---------------
        }
    }

    public class ServerCommunicationThread extends Thread {

        public ServerCommunicationThread() {

        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    Object obj = bss.getObject();

                    if (obj instanceof List) {
                        List list = (List) obj;

                        if (list.get(0) instanceof Player) {
                            synchronized (schiffListe) {
                                schiffListe = (List<Player>) obj;
                                System.out.println("Schiffsliste bekommen!");
                            }
                        } else if (list.get(0) instanceof Kugel) {
                            synchronized (kugelListe) {
                                kugelListe = (List<Kugel>) obj;
                                System.out.println("KugelListe bekommen!");
                            }
                        }

                    }

                } catch (IOException ex) {
                    Logger.getLogger(GameBL.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GameBL.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public class DrawThread extends Thread {

        public DrawThread() {

        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                draw();
            }
        }

    }

}
