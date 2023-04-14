package it.polimi.ingsw;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.rmi.ClientImpl;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AppClientSocket {
    private static final int TYPE_OF_MATCH = 0;
    private static final int PLAYERS_NUMBER = 1;
    private static final int NICKNAME_PLACEHOLDER = 2;
    public static void main(String[] args) throws RemoteException {
        List<String> decisions = new ArrayList<>();
        decisions.add(args[TYPE_OF_MATCH]);
        decisions.add(args[PLAYERS_NUMBER]);
        decisions.add(args[NICKNAME_PLACEHOLDER]);
        ServerStub serverStub = new ServerStub("localhost", 1234);
        final Client client;
        switch (decisions.get(TYPE_OF_MATCH)){
            case "1" -> {
                int numOfPlayers = Integer.parseInt(decisions.get(PLAYERS_NUMBER));
                System.out.println("num: " + numOfPlayers + ", decs: " + decisions.get(NICKNAME_PLACEHOLDER));
                client = new ClientImpl(serverStub, numOfPlayers , decisions.get(NICKNAME_PLACEHOLDER));
                //client.run();
            }
            case "2" -> {
                client = new ClientImpl(serverStub, decisions.get(NICKNAME_PLACEHOLDER));
                //client.run();
            }
            default -> {
                client = null;
                System.exit(20);
            }
        }
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
