package it.polimi.ingsw;

import it.polimi.ingsw.distributed.rmi.ClientImpl;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;

import java.rmi.RemoteException;

public class AppClientSocket {
    public static void main(String[] args) throws RemoteException {
        ServerStub serverStub = new ServerStub("localhost", 1234);
        ClientImpl client = new ClientImpl(serverStub, "null");
        new Thread(){
            @Override
            public void run(){
                try{
                    serverStub.receive(client);
                } catch(RemoteException e) {
                    System.err.println("Error while receiving message from server");
                    try{
                        serverStub.close();
                    } catch (RemoteException ex) {
                        System.err.println("Cannot close connection with server. Halting...");
                    }
                    System.exit(1);
                }
            }
        }.start();
        //clint.run();
    }
}
