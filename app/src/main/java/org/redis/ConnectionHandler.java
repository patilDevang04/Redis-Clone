package org.redis;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import org.redis.commands.*;


import org.redis.config.ObjectFactory;
import org.redis.protocol.ProtocolDeserializer;


public class ConnectionHandler extends Thread{ 
    private final Socket socket;
    private final ProtocolDeserializer protocolDeserializer;
    private final CommandFactory commandFactory;

    public ConnectionHandler(Socket socket, ObjectFactory objectFactory) {
        this.socket = socket;
        protocolDeserializer = objectFactory.getProtocolDeserializer();
        commandFactory = objectFactory.getCommandFactory();

    }


    public void run(){
        try {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            OutputStream outputStream = socket.getOutputStream();
            ProtocolDeserializer des = new ProtocolDeserializer();
            while(true){
                Pair<String, Long> stringLongPair = protocolDeserializer.parseInput(inputStream);
                String commandString = stringLongPair.left;

                String[] arguments = commandString.split(" ");
                String command = arguments[0].toUpperCase();
                Handler handler = commandFactory.getCommandHandler(command);
                byte[] response = handler.handle(arguments);

                outputStream.write(response);
                outputStream.flush();

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