package org.redis;

import org.redis.config.ObjectFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class Redis {
    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException,
            InstantiationException, IllegalAccessException {
        int port = 6379;
        ObjectFactory objectFactory = new ObjectFactory();
        try(ServerSocket socket = new ServerSocket(port)) {
            
            socket.setReuseAddress(true);

            while(true) { 
                Socket sock = socket.accept(); 
                ConnectionHandler handler = new ConnectionHandler(sock , objectFactory);
                handler.start();
            }

        }

        
    }
}
