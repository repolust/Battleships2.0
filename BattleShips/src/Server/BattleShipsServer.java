/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

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

    private int hoeheBildschirm, breiteBildschirm;
    
    public void initClientPosition() {
        LinkedList<Player> players = new LinkedList();
        for (ObjectInputStream oin : clients.keySet()) {
            players.add(clients.get(oin));
        }
        
        int anzahl = players.size();
        
        switch(anzahl){
            case 2:
            {
                //getPlayer
                Player player1 = players.get(0);
                Player player2 = players.get(1);
                //setPosition
                Position p1 = new Position(300, (hoeheBildschirm/2)-35);
                Position p2 = new Position(breiteBildschirm-300, (hoeheBildschirm/2)-35);
                player1.setP(p1);
                player2.setP(p2);
                //setRotation
                player1.setRotation(40);
                player2.setRotation(270);
                //setEinheitsvektor
                player1.setDirection(new EinheitsVektor(1,0));
            }
            case 3:
            {
                //getPlayer
                Player player1 = players.get(0);
                Player player2 = players.get(1);
                Player player3 = players.get(2);
                //setPosition
                Position p1 = new Position(300,200);
                Position p2 = new Position(breiteBildschirm-300, 200);
                Position p3 = new Position(300,hoeheBildschirm-200);
                player1.setP(p1);
                player2.setP(p2);
                player3.setP(p3);
                //setRotation
                player1.setRotation(40);
                player2.setRotation(270);
                player3.setRotation(40);
                
            }
            case 4:
            {
                //getPlayer
                Player player1 = players.get(0);
                Player player2 = players.get(1);
                Player player3 = players.get(2);
                Player player4 = players.get(3);
                //setPosition
                Position p1 = new Position(300,200);
                Position p2 = new Position(breiteBildschirm-300, 200);
                Position p3 = new Position(300,hoeheBildschirm-200);
                Position p4 = new Position(breiteBildschirm-300,hoeheBildschirm-200);
                player1.setP(p1);
                player2.setP(p2);
                player3.setP(p3);
                player4.setP(p3);
                //setRotation
                player1.setRotation(40);
                player2.setRotation(270);
                player3.setRotation(40);
                player4.setRotation(270);
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

            try {
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                Object obj = in.readObject();

                if (obj instanceof Player) {
                    Player p = (Player) obj;

                    clients.put(in, p);
                    connections.add(out);
                    gui.log(p.getName() + " joined the Battle!");

                }

                synchronized (clients) {
                    while (!Thread.interrupted()) {
                        Object gameObj = in.readObject();

                        if (gameObj instanceof Player) {
                            Player p = (Player) gameObj;
                            clients.replace(in, p);

                            //Player aus der Clientliste
                            LinkedList<Player> players = new LinkedList();
                            for (ObjectInputStream oin : clients.keySet()) {
                                players.add(clients.get(oin));
                            }
                            //----------------------------
                            out.writeObject(players);
                        } else if (gameObj instanceof Kugel) {
                            Kugel k = (Kugel) gameObj;
                            kugelList.add(k);
                            out.writeObject(kugelList);
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

    class CheckIfHitThread extends Thread {

        public CheckIfHitThread() {

        }

        public void checkCollision() {
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
                            //leben abziehen
                            //den 2en jeweils leben mitteilen
                            //allen die pos mitteilen
                            gui.log(p.getName() + " und " + p2.getName() + " sind zusammengefahren!");
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
