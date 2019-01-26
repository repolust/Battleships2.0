/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author michi
 */
public class BattleShipsClient 
{
    private InetAddress ADDR;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    private static BattleShipsClient theInstance;

    private BattleShipsClient() {
    }
    
    public BattleShipsClient gettheInstance()
    {
        if(theInstance == null)
        {
            theInstance = new BattleShipsClient();         
        }
        return theInstance;
    }
    
    public void connect() throws UnknownHostException, IOException
    {
        ADDR = InetAddress.getLocalHost();
        socket = new Socket(ADDR, 1337);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
    }
    
    public void sendObject(Object toSend) throws IOException, ClassNotFoundException
    {
        out.writeObject(toSend);
        Object obj = in.readObject();
    }
}
