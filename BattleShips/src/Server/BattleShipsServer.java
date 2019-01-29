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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michi
 */
public class BattleShipsServer {

    private final int PORTNR = 1337;
    private ServerThread st;
    private ServerGUI gui;

    private LinkedList<Kugel> kugelList = new LinkedList<Kugel>();
    private static Map<ObjectInputStream, Player> clients = new HashMap();
    private LinkedList<ObjectOutputStream> connections = new LinkedList();

    private int maxX = 1920;
    private int maxY = 910; 

    public void initClientPosition() {
        
        LinkedList<Player> players = new LinkedList();
        for (ObjectInputStream oin : clients.keySet()) {
            players.add(clients.get(oin));
        }

        int anzahl = players.size();

        switch (anzahl) {
            case 2: {
                //getPlayer
                Player player1 = players.get(0);
                Player player2 = players.get(1);
                //setPosition
                Position p1 = new Position(300, (maxY / 2) - 35);
                Position p2 = new Position(maxX - 300, (maxY / 2) - 35);
                player1.setP(p1);
                player2.setP(p2);
                //setRotation
                player1.setRotation(40);
                player2.setRotation(270);
                //setEinheitsvektor
                player1.setDirection(new EinheitsVektor(1, 0));
                player2.setDirection(new EinheitsVektor(-1, 0));
                //setWinkel
                player1.setCurrentAngle(90);
                player2.setCurrentAngle(270);
                //updatePlayerliste
                players.set(0, player1);
                players.set(1, player2);
            }
            case 3: {
                //getPlayer
                Player player1 = players.get(0);
                Player player2 = players.get(1);
                Player player3 = players.get(2);
                //setPosition
                Position p1 = new Position(300, 200);
                Position p2 = new Position(maxX - 300, 200);
                Position p3 = new Position(300, maxY - 200);
                player1.setP(p1);
                player2.setP(p2);
                player3.setP(p3);
                //setRotation
                player1.setRotation(40);
                player2.setRotation(270);
                player3.setRotation(40);
                //setEinheitsvektor
                player1.setDirection(new EinheitsVektor(1, 0));
                player2.setDirection(new EinheitsVektor(-1, 0));
                player3.setDirection(new EinheitsVektor(1, 0));
                //setWinkel
                player1.setCurrentAngle(90);
                player2.setCurrentAngle(270);
                player3.setCurrentAngle(90);
                //updatePlayerliste
                players.set(0, player1);
                players.set(1, player2);
                players.set(2, player3);
            }
            case 4: {
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
                player1.setP(p1);
                player2.setP(p2);
                player3.setP(p3);
                player4.setP(p3);
                //setRotation
                player1.setRotation(40);
                player2.setRotation(270);
                player3.setRotation(40);
                player4.setRotation(270);
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
                //updatePlayerliste
                players.set(0, player1);
                players.set(1, player2);
                players.set(2, player3);
                players.set(3, player4);
            }
        }
    }

    public BattleShipsServer(ServerGUI gui) {
        this.gui = gui;
        startServer();
    }

    public void startServer() {
        if (st == null || !st.isAlive()) {
            try {
                st = new ServerThread();
                st.start();
                gui.log("Server started on Port: " + PORTNR);
            } catch (IOException ex) {

                st = null;
                gui.log("Starting server failed! " + ex.toString());
            }
        }
    }

    public void stopServer() {
        if (st != null || st.isAlive()) {
            st.interrupt();
            gui.log("Server stopped.");
        }
    }

    //------------------------------------------------------------------------------    
    class ServerThread extends Thread {

        private ServerSocket serverSocket;

        public ServerThread() throws IOException {
            serverSocket = new ServerSocket(PORTNR);
            serverSocket.setSoTimeout(250); // damit er bei serverSocket.accept nicht hängen bleibt
        }

        @Override
        public void run() {
            while (!st.isInterrupted()) {
                try {

                    Socket socket = serverSocket.accept();
                    gui.log("Neuer client");
                    ClientCommunicationThread cct = new ClientCommunicationThread(socket);

                } catch (SocketTimeoutException ex) {
                    //gui.log("timeout");
                } catch (IOException ex) {
                    gui.log("Connection failed");
                }
            }
            try {
                serverSocket.close();
                gui.log("Server shut down");
            } catch (IOException ex) {
                gui.log("Closing Server failed");
            }

        }

    }

    class ClientCommunicationThread extends Thread {

        private Socket socket;

        public ClientCommunicationThread(Socket socket) throws SocketException {
            this.socket = socket;
            //this.socket.setSoTimeout(1000);//Falls Client nix sendet
            start();
        }

        @Override
        public void run() {

            gui.log("Communication Started!");
        try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                
                Object obj = in.readObject();
                
            synchronized (clients) 
            {
                if (obj instanceof Player) 
                {
                    Player p = (Player) obj;

                    clients.put(in, p);
                    connections.add(out);
                    gui.log(p.getName() + " joined the Battle!");

                }
         
                    
                    while (!Thread.interrupted()) {
                        Object gameObj = in.readObject();

                        if (gameObj instanceof Player) 
                        {
                            Player p = (Player) gameObj;
                            clients.replace(in, p);

                            //Player aus der Clientliste
                            LinkedList<Player> players = new LinkedList();
                            for (ObjectInputStream oin : clients.keySet()) {
                                players.add(clients.get(oin));
                            }
                            //----------------------------
                            for (ObjectOutputStream con : connections) {
                                con.writeObject(players);
                            }
                            
                        } 
                        else if (gameObj instanceof Kugel) 
                        {
                            Kugel k = (Kugel) gameObj;
                            kugelList.add(k);
                            for (ObjectOutputStream con : connections) {
                                con.writeObject(kugelList);
                            }
                            
                        }
                        else if(gameObj instanceof String)
                        {
                            String command = (String) gameObj;
                            if(command.equals("currentPlayers"))
                            {
                                LinkedList<Player> players = new LinkedList();
                                for (ObjectInputStream oin : clients.keySet()) {
                                    players.add(clients.get(oin));
                                }
                                out.writeObject(players);
                            }
                            else if(command.equals("imReady"))
                            {
                                Player p = clients.get(in);
                                p.setBereit(true);
                                clients.put(in, p);
                            }
                        }

                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(BattleShipsServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BattleShipsServer.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    class LogicThread extends Thread {

        public LogicThread() {
            
        }

        @Override
        public void run() {
            
            if(!kugelList.isEmpty())
            {
                moveKugeln();
                checkIfHit();
            }
            checkCollision();
        }
        
        public void moveKugeln()
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
                    }
        }

        public void checkCollision() 
        {
            if(!clients.isEmpty())
            {    
                LinkedList<Player> players = new LinkedList();
                
                for (ObjectInputStream oin : clients.keySet()) {
                    players.add(clients.get(oin));
                }

                for (Player p : players) {
                    for (Player p2 : players) {
                        if (p != p2) {
                            if (p.getHitbox().intersects(p2.getHitbox())) {
                                //Jedem client sagen dass 2 SChiffe ineinander gefahren sind
                                //schiffs koord. reseten
                                p.setLeben(p.getLeben() - 20);
                                p2.setLeben(p2.getLeben() - 20);
                                //den 2en jeweils leben mitteilen
                                //allen die pos mitteilen
                                gui.log(p.getName() + " und " + p2.getName() + " sind zusammengefahren!");
                            }
                        }

                    }
                }
            }
        }

        public void checkIfHit() {

            //Treffer mit KanonenKugel
            for (Kugel k : kugelList) {

                Rectangle rectK = new Rectangle(k.getPos().getXInt(), k.getPos().getYInt(), k.getGroesse(), k.getGroesse());

                LinkedList<Player> players = new LinkedList();
                for (ObjectInputStream oin : clients.keySet()) {
                    players.add(clients.get(oin));
                }

                for (Player p : players) {
                    Rectangle hitbox = p.getHitbox();

                    if (rectK.intersects(hitbox)) {
                        Treffer t = new Treffer(p.getIndex(), kugelList.indexOf(k));
                        //leben abziehen
                        //kugel entfernen
                        //dem client leben mitteilen
                        gui.log(p.getName() + " wurde getroffen!");
                    }
                }
            }

        }

    }
    
    

}
