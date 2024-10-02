package org.redis;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


public class ConnectionHandler{ 
    private final Socket socket;


    public ConnectionHandler(Socket socket) {
        this.socket = socket;
    }


    public void run(){
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            OutputStream outputStream = socket.getOutputStream();

            System.out.println("Connection established");

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