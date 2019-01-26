/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Beans.Kugel;
import Beans.Player;
import Beans.Treffer;
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
public class BattleShipsServer{
    
    private final int PORTNR = 1337;
    private ServerThread st;
    private ServerGUI gui;
    
    private LinkedList<Kugel> kugelList = new LinkedList<Kugel>(); 
    private static Map<Player, OutputStream> clients = new HashMap();

    public BattleShipsServer(ServerGUI gui) {
        this.gui = gui;
        startServer();
    }
    
    public void startServer()
    {
        if(st == null || !st.isAlive())
        {
            try {
                st = new ServerThread();
                st.start();
                gui.log("Server started on Port: "+PORTNR);
            } catch (IOException ex) {
                
                st = null;
                gui.log("Starting server failed! "+ ex.toString());
            }
        }
    }
    
    public void stopServer()
    {
        if( st != null ||st.isAlive())
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
            while(!st.isInterrupted())
            {
                try
                {
                    
                    Socket socket = serverSocket.accept();
                    
                    
                    ClientCommunicationThread cct = new ClientCommunicationThread(socket);
                    
                } 
                catch(SocketTimeoutException ex)
                {
                    //gui.log("timeout");
                }
                catch (IOException ex)
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
                    
            try
            {
               ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
               ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
               Object objplayer = in.readObject();
               
               if(objplayer instanceof Player)
               {
                   Player p = (Player) objplayer;
                   if(!clients.containsKey(p))
                   {
                       clients.put(p, out);
                       gui.log(p.getName()+" joined the Battle!");
                   }              
               }
               
               while(!Thread.interrupted())
               {
                   Object obj = in.readObject();
                   
                   if(obj instanceof Player)
                    {
                        
                    }
                   else if(obj instanceof Kugel){
                       Kugel k = (Kugel)obj;
                       kugelList.add(k); 
                   }
                       
                   
               }
                  
               } catch (IOException ex) {
                Logger.getLogger(BattleShipsServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(BattleShipsServer.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            } 
        }
    
    
    class CheckIfHitThread extends Thread
    {
        public CheckIfHitThread(){
        
        }
        
        public void checkCollision()
        {
            for (Player p : clients.keySet()) 
            {
                for (Player p2 : clients.keySet()) 
                {
                    if(p != p2)
                    {
                        if(p.getHitbox().intersects(p2.getHitbox()))
                        {
                            //Jedem client sagen dass 2 SChiffe ineinander gefahren sind
                            //schiffs koord. reseten
                            //leben abziehen
                            //den 2en jeweils leben mitteilen
                            //allen die pos mitteilen
                            gui.log(p.getName()+" und "+p2.getName()+" sind zusammengefahren!");
                        }
                    }
                    
                }
            }
            
        }        

    
        public void checkIfHit() {

            //Treffer mit KanonenKugel
            for (Kugel k : kugelList) {

                Rectangle rectK = new Rectangle(k.getPos().getXInt(), k.getPos().getYInt(), k.getGroesse(), k.getGroesse());

                for (Player p : clients.keySet()) 
                {
                    Rectangle hitbox = p.getHitbox();
                    
                    if (rectK.intersects(hitbox)) 
                    {
                        Treffer t = new Treffer(p.getIndex(), kugelList.indexOf(k));
                        //leben abziehen
                        //kugel entfernen
                        //dem client leben mitteilen
                        gui.log(p.getName()+" wurde getroffen!");
                    }
                }
            }

        }
    }

    }

