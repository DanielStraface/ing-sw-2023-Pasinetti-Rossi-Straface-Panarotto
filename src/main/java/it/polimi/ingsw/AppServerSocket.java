package it.polimi.ingsw;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.distributed.socket.middleware.ClientSkeleton;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;

public class AppServerSocket {
    public static void main(String[] args) throws RemoteException {
        try(ServerSocket serverSocket = new ServerSocket(1234)){
            System.out.println("Server is starting...");
            while(true){
                System.out.println("Server ready!");
                try(Socket socket = serverSocket.accept()){
                    System.out.println("Server accept a new connection from "
                            + socket.getInetAddress() + " on port" + socket.getPort());
                    ClientSkeleton clientSkeleton = new ClientSkeleton(socket);
                    Server server = new ServerImpl();
                    while(true){
                        System.out.println("Server is ready to receive message from " + socket.getInetAddress());
                        clientSkeleton.receive(server);
                    }
                }
            }
        } catch (IOException e) {
            throw new RemoteException("Error while creating server socket", e);
        }
    }
}
