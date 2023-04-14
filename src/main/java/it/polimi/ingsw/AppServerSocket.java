package it.polimi.ingsw;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.rmi.ServerImpl;
import it.polimi.ingsw.distributed.socket.middleware.ClientSkeleton;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AppServerSocket {
    public static void main(String[] args){
        try(ServerSocket serverSocket = new ServerSocket(1234)){
            while(true){
                try(Socket socket = serverSocket.accept()) {
                    ClientSkeleton clientSkeleton = new ClientSkeleton(socket);
                    Server server = new ServerImpl();
                    while (true) {
                        clientSkeleton.receive(server);
                    }
                }
            }
        } catch (IOException e){
            throw new RuntimeException("Error while creating server socket", e);
        }
    }
}
