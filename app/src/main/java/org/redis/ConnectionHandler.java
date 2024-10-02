package org.redis;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.redis.protocol.ProtocolDeserializer;


public class ConnectionHandler extends Thread{ 
    private final Socket socket;


    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }


    public void run(){
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            OutputStream outputStream = socket.getOutputStream();
            ProtocolDeserializer des = new ProtocolDeserializer();
            while(true){
                Pair pair = des.parseInput(inputStream);
                System.out.println(pair.left + " " + pair.right);
                System.out.println("Connection established");
            }
            

        }
        catch(IOException ex) { 
            System.out.println("IOException: " + ex.getMessage());
        }
        finally{ 
            try {
                socket.close();
                System.out.println("socket closed");
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        }
    }



}