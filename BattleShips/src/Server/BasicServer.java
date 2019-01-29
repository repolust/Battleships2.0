/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.JTextComponent;

/**
 *
 * @author michi
 */
public abstract class BasicServer
{
    
    private final int PORTNR;
    private JTextComponent taLog;
    private ServerThread st;

    
    public BasicServer()
    {
        this.PORTNR = 9999;
    }

    public BasicServer(int PORTNR)
    {
        this.PORTNR = PORTNR;
    }

    public BasicServer(int portnr, JTextComponent taLog)
    {
        this(portnr);
        this.taLog = taLog;
    }
    
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
     
        if(taLog != null)
        {
            taLog.setText(taLog.getText()+"\n"+message);
        }
        {
            System.out.println(message);
        }
    }
   
    protected void removeClient() throws IOException
        {
            st.interrupt();
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
            //serverSocket.setSoTimeout(250); // damit er bei serverSocket.accept nicht hängen bleibt
        }

        
        @Override
        public void run()
        {
            while(!st.isInterrupted())
            {
                try
                {
                    Socket socket = serverSocket.accept();
                    log("Connected with: "+ socket.getRemoteSocketAddress());
                    ClientCommunicationThread cct = new ClientCommunicationThread(socket);
                    
                } 
                catch(SocketTimeoutException ex)
                {
                    //log("timeout");
                }
                catch (IOException ex)
                {
                   log("Connection failed");
                } 
            }
            
           
        }
     
    }
    
    public class ClientCommunicationThread extends Thread
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

               while(!interrupted())
               {
                Object request = in.readObject();
                performCommunication(request,out);
               }
            } catch (IOException  | ClassNotFoundException ex)
            {
                this.interrupt();
//                log("Error in ClientCommunication Thread! "+ ex.toString());
            }
            
            
        }
        
    }
    
    protected abstract Object performCommunication(Object request, ObjectOutputStream out);
    
   
}
