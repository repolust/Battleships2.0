/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import javax.swing.text.JTextComponent;

/**
 *
 * @author michi
 */
public abstract class BasicServer
{
    
    private final int PORTNR = 1337;
    public ServerGUI gui;
    private ServerThread st;
    

 
    
    public void startServer()
    {
        if(st == null || !st.isAlive())
        {
            try {
                st = new ServerThread();
                st.start();
                log("Server started on Port: "+PORTNR);
            } catch (IOException ex) {
                
                st = null;
                log("Starting server failed!");
            }
        }
    }
    
    protected void log(String message)
    {   
         gui.log(message);
    }
    
    protected void logError(String e)
    {
        gui.error(e);
    }
    
    public void stopServer()
    {
        if( st != null ||st.isAlive())
        {
            st.interrupt();
            
        }
    }
    
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
//                    log("timeout");
                }
                catch (IOException ex)
                {
                   log("Connection failed");
                }
            }
            try
            {
                serverSocket.close();
                 log("Server shut down");
            } catch (IOException ex)
            {
               log("Closing Server failed");
            }
           
        }
     
    }
    
    class ClientCommunicationThread extends Thread
    {
        private Socket socket;

        public ClientCommunicationThread(Socket socket) throws SocketException
        {
            this.socket = socket;
//            this.socket.setSoTimeout(1000);//Falls Client nix sendet
            start();
        }
        
        

        @Override
        public void run()
        {
            
            
            try(
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());)
            {
               Object obj = in.readObject();
//                Transaction t = (Transaction) obj;
//               gui.log("request on: "+ t.getIban());
               
//               out.writeObject(performRequest(t));
                
            } catch (IOException  | ClassNotFoundException ex)
            {
                log("Error in ClientCommunication Thread! "+ ex.toString());
            } 
        }
        
    }
    
//    protected abstract Object performRequest(Transaction t);

   
}
