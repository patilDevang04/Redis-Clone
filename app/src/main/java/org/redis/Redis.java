package org.redis;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Redis {
    public static void main(String[] args) throws IOException {
        int port = 6379;
        try(ServerSocket socket = new ServerSocket(port)) {
            
            socket.setReuseAddress(true);

            while(true) { 
                Socket sock = socket.accept(); 
                ConnectionHandler handler = new ConnectionHandler(sock);
                handler.run();
            }

        }

        
    }
}
