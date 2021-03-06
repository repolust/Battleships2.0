/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Beans.EinheitsVektor;
import Beans.Kugel;
import Beans.Player;
import Beans.Position;
import Beans.Treffer;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michi
 */
public class BattleShipsServer
{

    private final int PORTNR = 1337;
    private ServerThread st;
    private ServerGUI gui;

    private List<Kugel> kugelList = new LinkedList();
    private Map<ObjectInputStream, Player> clients = new HashMap();
    private List<ObjectOutputStream> connections = new LinkedList();
    private StartGameThread startGame = new StartGameThread();
    private LogicThread logicThread = new LogicThread();
    private int maxX = 1920;
    private int maxY = 910;

    public BattleShipsServer(ServerGUI gui)
    {
        this.gui = gui;
        startServer();
    }

    public List<Player> getPlayerList()
    {
        List<Player> players = new LinkedList();
        synchronized (clients)
        {
            for (ObjectInputStream oin : clients.keySet())
            {
                players.add(clients.get(oin));
            }
        }
        return players;
    }

    public void startServer()
    {
        if (st == null || !st.isAlive())
        {
            try
            {
                st = new ServerThread();
                st.start();

                InetAddress inetAddress = InetAddress.getLocalHost();
                gui.log("Server started on " + inetAddress.getHostAddress() + " | Port: " + PORTNR);
                gui.log("Waiting for players..");
                startGame.start();
            } catch (IOException ex)
            {

                st = null;
                gui.log("Starting server failed! " + ex.toString());
            }
        }
    }

    public void stopServer()
    {
        if (st != null || st.isAlive())
        {
            st.interrupt();
            gui.log("Server stopped.");
        }
    }

    //------------------------------------------------------------------------------    
    class ServerThread extends Thread
    {

        private ServerSocket serverSocket;

        public ServerThread() throws IOException
        {
            serverSocket = new ServerSocket(PORTNR);
            serverSocket.setSoTimeout(250); // damit er bei serverSocket.accept nicht hängen bleibt
        }

        @Override
        public void run()
        {
            while (!st.isInterrupted())
            {
                try
                {

                    if (clients.size() < 4)
                    {
                        Socket socket = serverSocket.accept();
                        gui.log("Neuer client!");
                        ClientCommunicationThread cct = new ClientCommunicationThread(socket);
                    }
                } catch (SocketTimeoutException ex)
                {
                    //gui.log("timeout");
                } catch (IOException ex)
                {
                    gui.log("Connection failed");
                }
            }
            try
            {
                serverSocket.close();
                gui.log("Server shut down");
            } catch (IOException ex)
            {
                gui.log("Closing Server failed");
            }

        }

    }

    class ClientCommunicationThread extends Thread
    {

        private Socket socket;

        public ClientCommunicationThread(Socket socket) throws SocketException
        {
            this.socket = socket;
            //this.socket.setSoTimeout(1000);//Falls Client nix sendet
            start();
        }

        @Override
        public void run()
        {

            gui.log("Communication Started!");
            try
            {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

                Object obj = in.readObject();
//-------------------------Anmelden--------------------
                if (obj instanceof Player)
                {
                    Player p = (Player) obj;
                    synchronized (clients)
                    {
                        clients.put(in, p);
                    }
                    connections.add(out);
                    gui.log(p.getName() + " joined the Battle!");

                    for (ObjectOutputStream con : connections)
                    {
                        con.writeObject(getPlayerList());
                        con.reset();
                    }
                    gui.updatePlayertable(getPlayerList());

                }
//------------------------- Spielverlauf--------------------
                while (!Thread.interrupted())
                {

                    Object gameObj = in.readObject();

                    if (gameObj instanceof Player)
                    {
                        synchronized (clients)
                        {
                            Player p = (Player) gameObj;
                            clients.replace(in, p);

                            for (ObjectOutputStream con : connections)
                            {
                                con.writeObject(getPlayerList());
                                con.reset();
                            }
                        }

                    } //                    else if (gameObj instanceof Kugel)
                    //                    {
                    //                        Kugel k = (Kugel) gameObj;
                    //                        kugelList.add(k);
                    //
                    //                    } //-------------------------------Lobby---------------------------------------
                    else if (gameObj instanceof String)
                    {
                        String command = (String) gameObj;

                        if (command.equals("imReady"))
                        {
                            synchronized (clients)
                            {
                                Player p = clients.get(in);
                                p.setBereit(true);

                                clients.replace(in, p);

                                gui.log(p.getName() + " is ready!");

                                for (ObjectOutputStream con : connections)
                                {
                                    con.writeObject(getPlayerList());
                                    con.reset();

                                }

                                gui.updatePlayertable(getPlayerList());
                            }
                        } else if (command.equals("imOut"))
                        {
                            synchronized (clients)
                            {
                                gui.log(clients.get(in).getName() + " disconnected");

                                clients.remove(in);
                                gui.updatePlayertable(getPlayerList());

                                for (ObjectOutputStream con : connections)
                                {
                                    con.writeObject(getPlayerList());
                                    con.reset();

                                }
                            }
                        } else if (command.equals("requestStartInformation"))
                        {
                            synchronized (clients)
                            {
                                gui.log("Start information sent to: " + clients.get(in).getName());
                                out.writeObject(clients.get(in));
                                out.writeObject(getPlayerList());
                                out.reset();
                            }

                        } //-------------------------------------Bewegung------------------------------------
                        else if (command.equals("moveForward"))
                        {
                            synchronized (clients)
                            {
                                Player p = moveShip(clients.get(in));
                                clients.replace(in, p);

                                for (ObjectOutputStream con : connections)
                                {
                                    con.writeObject(getPlayerList());
                                    con.reset();
                                }
                            }
                        } else if (command.equals("turnLeft"))
                        {
                            synchronized (clients)
                            {
                                Player p = moveShip(clients.get(in));

                                EinheitsVektor k = p.getDirection();
                                k.rotateEinheitsVektor(-p.getRotation());
                                p.setDirection(k);
                                p.setCurrentAngle(p.getCurrentAngle() - p.getRotation());

                                clients.replace(in, p);

                                for (ObjectOutputStream con : connections)
                                {
                                    con.writeObject(getPlayerList());
                                    con.reset();
                                }
                            }
                        } else if (command.equals("turnRight"))
                        {
                            synchronized (clients)
                            {
                                Player p = moveShip(clients.get(in));

                                EinheitsVektor k = p.getDirection();
                                k.rotateEinheitsVektor(p.getRotation());
                                p.setDirection(k);
                                p.setCurrentAngle(p.getCurrentAngle() + p.getRotation());
                                clients.replace(in, p);

                                for (ObjectOutputStream con : connections)
                                {
                                    con.writeObject(getPlayerList());
                                    con.reset();
                                }
                            }
                        } else if (command.equals("schuss"))
                        {

                            synchronized (clients)
                            {
                                Player p = clients.get(in);
                                gui.log(p.getName() + " schießt!");
                                schuss(p);
                                for (ObjectOutputStream con : connections)
                                {
                                    con.writeObject(kugelList);
                                    con.reset();
                                }
                            }

                        }
                    }

                }

            } catch (IOException ex)
            {
                Logger.getLogger(BattleShipsServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex)
            {
                Logger.getLogger(BattleShipsServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        public Player moveShip(Player p)
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

            return p;
        }

        public void schuss(Player p)
        {
            synchronized (kugelList)
            {
                EinheitsVektor einVLinks = new EinheitsVektor(p.getDirection().getX(), p.getDirection().getY());
                EinheitsVektor einVRechts = new EinheitsVektor(p.getDirection().getX(), p.getDirection().getY());

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
                        kugelList.add(new Kugel(einVLinks, posSL, 5, p.getIndex()));

                        kugelList.add(new Kugel(einVRechts, posSR, 5, p.getIndex()));
                        p.setMunition(p.getMunition() - 2);
                    }

                }
            }
        }
    }

    class StartGameThread extends Thread
    {

        public StartGameThread()
        {

        }

        public void initClientPosition()
        {

            List<Player> players = getPlayerList();

            int anzahl = players.size();

            switch (anzahl)
            {
                case 1:
                {
                    //getPlayer
                    Player player1 = players.get(0);
                    //setPosition
                    Position p1 = new Position(300, (maxY / 2) - 35);
                    player1.setStartPos(p1);
                    //setEinheitsvektor
                    player1.setDirection(new EinheitsVektor(1, 0));
                    //setWinkel
                    player1.setCurrentAngle(90);
                    //SET index
                    player1.setIndex(1);

                    //updatePlayerliste           
                    int c = 0;
                    for (ObjectInputStream stream : clients.keySet())
                    {
                        switch (c)
                        {
                            case 0:
                                clients.replace(stream, player1);
                                break;
                            default:
                                break;
                        }
                        c++;
                    }
                }
                break;
                case 2:
                {
                    //getPlayer
                    Player player1 = players.get(0);
                    Player player2 = players.get(1);
                    //setPosition
                    Position p1 = new Position(300, (maxY / 2) - 35);
                    Position p2 = new Position(maxX - 300, (maxY / 2) - 35);
                    player1.setStartPos(p1);
                    player2.setStartPos(p2);
                    //setEinheitsvektor
                    player1.setDirection(new EinheitsVektor(1, 0));
                    player2.setDirection(new EinheitsVektor(-1, 0));
                    //setWinkel
                    player1.setCurrentAngle(90);
                    player2.setCurrentAngle(270);
                    //SET index
                    player1.setIndex(1);
                    player2.setIndex(2);

                    //updatePlayerliste           
                    int c = 0;
                    for (ObjectInputStream stream : clients.keySet())
                    {
                        switch (c)
                        {
                            case 0:
                                clients.replace(stream, player1);
                                break;
                            case 1:
                                clients.replace(stream, player2);
                                break;
                            default:
                                break;
                        }
                        c++;
                    }

                }
                break;
                case 3:
                {
                    //getPlayer
                    Player player1 = players.get(0);
                    Player player2 = players.get(1);
                    Player player3 = players.get(2);
                    //setPosition
                    Position p1 = new Position(300, 200);
                    Position p2 = new Position(maxX - 300, 200);
                    Position p3 = new Position(300, maxY - 200);
                    player1.setStartPos(p1);
                    player2.setStartPos(p2);
                    player3.setStartPos(p3);
                    //setEinheitsvektor
                    player1.setDirection(new EinheitsVektor(1, 0));
                    player2.setDirection(new EinheitsVektor(-1, 0));
                    player3.setDirection(new EinheitsVektor(1, 0));
                    //setWinkel
                    player1.setCurrentAngle(90);
                    player2.setCurrentAngle(270);
                    player3.setCurrentAngle(90);
                    //SET index
                    player1.setIndex(1);
                    player2.setIndex(2);
                    player3.setIndex(3);

                    //updatePlayerliste           
                    int c = 0;
                    for (ObjectInputStream stream : clients.keySet())
                    {
                        switch (c)
                        {
                            case 0:
                                clients.replace(stream, player1);
                                break;
                            case 1:
                                clients.replace(stream, player2);
                                break;
                            case 2:
                                clients.replace(stream, player3);
                                break;
                            default:
                                break;
                        }
                        c++;
                    }
                }
                break;
                case 4:
                {
                    //getPlayer
                    Player player1 = players.get(0);
                    Player player2 = players.get(1);
                    Player player3 = players.get(2);
                    Player player4 = players.get(3);
                    //setPosition
                    Position p1 = new Position(300, 200);
                    Position p2 = new Position(maxX - 300, 200);
                    Position p3 = new Position(300, maxY - 200);
                    Position p4 = new Position(maxX - 300, maxY - 200);
                    player1.setStartPos(p1);
                    player2.setStartPos(p2);
                    player3.setStartPos(p3);
                    player4.setStartPos(p3);
                    //setEinheitsvektor
                    player1.setDirection(new EinheitsVektor(1, 0));
                    player2.setDirection(new EinheitsVektor(-1, 0));
                    player3.setDirection(new EinheitsVektor(1, 0));
                    player4.setDirection(new EinheitsVektor(-1, 0));
                    //setWinkel
                    player1.setCurrentAngle(90);
                    player2.setCurrentAngle(270);
                    player3.setCurrentAngle(90);
                    player4.setCurrentAngle(270);
                    //SET index
                    player1.setIndex(1);
                    player2.setIndex(2);
                    player3.setIndex(3);
                    player4.setIndex(4);
                    //updatePlayerliste           
                    int c = 0;
                    for (ObjectInputStream stream : clients.keySet())
                    {
                        switch (c)
                        {
                            case 0:
                                clients.replace(stream, player1);
                                break;
                            case 1:
                                clients.replace(stream, player2);
                                break;
                            case 2:
                                clients.replace(stream, player3);
                                break;
                            case 3:
                                clients.replace(stream, player4);
                                break;
                            default:
                                break;
                        }
                        c++;
                    }
                }
                break;

            }
        }

        public List<Player> getPlayerList()
        {
            List<Player> players = new LinkedList();
            synchronized (clients)
            {
                for (ObjectInputStream oin : clients.keySet())
                {
                    players.add(clients.get(oin));
                }
            }
            return players;

        }

        @Override
        public void run()
        {
            gui.log("StartGameThread started!");

            while (!isInterrupted())
            {
                boolean startGame = false;
                if (getPlayerList().size() > 1)
                {

                    for (Player p : getPlayerList())
                    {
                        if (p.isBereit())
                        {
                            startGame = true;
                        } else
                        {
                            startGame = false;
                            break;
                        }
                    }

                    if (startGame)
                    {
                        gui.log("JETZT GEHTS LOOS!");
                        initClientPosition();
                        for (ObjectOutputStream con : connections)
                        {
                            try
                            {
                                gui.log("Game Started!");
                                con.writeObject("StartGame");
                                con.reset();
                            } catch (IOException ex)
                            {
                                Logger.getLogger(BattleShipsServer.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        logicThread.start();
                        this.interrupt();
                    }
                }
            }
        }

    }

    class LogicThread extends Thread
    {

        public LogicThread()
        {

        }

        @Override
        public void run()
        {
            gui.log("LogicThread started!");
            while (!isInterrupted())
            {
                if (!kugelList.isEmpty())
                {
                    moveKugeln();
                    checkIfHit();
                }
                checkCollision();
                try
                {
                    Thread.sleep(10);
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(BattleShipsServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        public void moveKugeln()
        {
            synchronized (kugelList)
            {
                int removeIndex = -1;

                for (Kugel k : kugelList)
                {

                    k.getPos().increaseX(k.getEinheintsVektor().getX() * 20);
                    k.getPos().increaseY(k.getEinheintsVektor().getY() * 20);

                    if (k.getPos().getX() > maxX || k.getPos().getX() < 0)
                    {
                        removeIndex = kugelList.indexOf(k);

                    }
                    if (k.getPos().getY() > maxY || k.getPos().getY() < 0)
                    {
                        removeIndex = kugelList.indexOf(k);

                    }

                }

                if (removeIndex != -1)
                {
                    kugelList.remove(removeIndex);
                    gui.log("Kugel entfernt!");
                }

                if (!kugelList.isEmpty())
                {
                    for (ObjectOutputStream con : connections)
                    {
                        try
                        {

                            con.writeObject(kugelList);
                           con.reset();

                        } catch (IOException ex)
                        {
                            Logger.getLogger(BattleShipsServer.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }

        public void checkCollision()
        {
            if (!clients.isEmpty())
            {

                for (Player p : getPlayerList())
                {
                    for (Player p2 : getPlayerList())
                    {
                        if (p != p2)
                        {
                            if (p.getHitbox().intersects(p2.getHitbox()))
                            {
                                //Jedem client sagen dass 2 SChiffe ineinander gefahren sind
                                //schiffs koord. reseten
                                p.setLeben(p.getLeben() - 20);
                                p2.setLeben(p2.getLeben() - 20);
                                p.setToStartPos();
                                p2.setToStartPos();
                                //allen die pos mitteilen
                                gui.log(p.getName() + " und " + p2.getName() + " sind zusammengefahren!");
                            }
                        }

                    }
                }
            }
        }

        public void checkIfHit()
        {
            synchronized (kugelList)
            {
                try
                {
                    
                    //Treffer mit KanonenKugel
                    for (Kugel k : kugelList)
                    {
                        
                        Rectangle rectK = new Rectangle(k.getPos().getXInt(), k.getPos().getYInt(), k.getGroesse(), k.getGroesse());
                        
                        for (Player p : getPlayerList())
                        {
                            Rectangle hitbox = p.getHitbox();
                            
                            if (rectK.intersects(hitbox) && k.getPlayernumber() != p.getIndex())
                            {
                                kugelList.wait();
                                Treffer t = new Treffer(p.getIndex(), kugelList.indexOf(k));
                                kugelList.remove(k);
                                //leben abziehen
                                
                                //dem client leben mitteilen
                                gui.log(p.getName() + " wurde getroffen!");
                                kugelList.notifyAll();
                            }
                        }
                    }
                    
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(BattleShipsServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}
