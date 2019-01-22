/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import Beans.Kugel;
import Beans.Player;
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
                   
               }
                
            } catch (IOException  | ClassNotFoundException ex)
            {
                gui.error("Error in ClientCommunication Thread! "+ ex.toString());
            } 
        }
        
    }
}
