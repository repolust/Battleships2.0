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
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private BattleShipsClient()
    {
    }

    public static BattleShipsClient getTheInstance()
    {
        if (theInstance == null)
        {
            theInstance = new BattleShipsClient();
        }
        return theInstance;
    }

    public void connect()
    {
        try
        {
//            ADDR = InetAddress.getLocalHost();
//            ADDR = InetAddress.getByName("192.168.126.1"); //getByAddress(, null);
            socket = new Socket("192.168.8.100", 1337);

            
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
               
        } catch (UnknownHostException ex)
        {
            Logger.getLogger(BattleShipsClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            Logger.getLogger(BattleShipsClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void disconnect() throws IOException
    {
        in.close();
        out.close();
        socket.close();
    }

    public void sendObject(Object toSend) throws IOException, ClassNotFoundException
    {
        out.writeObject(toSend);
    }

    public Object getObject() throws IOException, ClassNotFoundException
    {

        Object obj = in.readObject();
        return obj;
    }

}
