package it.polimi.ingsw;

import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.exceptions.WaitingForEventSocketException;

import java.rmi.RemoteException;

public class AppClientSocket {
    private static final int TYPE_OF_MATCH_POSITION = 0;
    private static final int NUMBER_OF_PLAYER_POSITION = 1;
    private static final int NICKNAME_POSITION = 2;
    private static final Integer JOINING_EXISTING_GAME = 0;
    public static void main(String[] args) throws RemoteException {
        String nickname = args[NICKNAME_POSITION];
        System.out.println("Client ready");
        ServerStub serverStub = new ServerStub("localhost", 1234);
        System.out.println("Connection to the server successfully created!");
        ClientImpl client = null;
        System.err.println(args[TYPE_OF_MATCH_POSITION]);
        switch (args[TYPE_OF_MATCH_POSITION]) {
            case "1" -> {
                 client = new ClientImpl(serverStub,
                        Integer.parseInt(args[NUMBER_OF_PLAYER_POSITION]), nickname);
            }
            case "2" -> {
                client = new ClientImpl(serverStub, JOINING_EXISTING_GAME, nickname);
            }
            default -> {
                System.exit(20);
            }
        }
        System.out.println("New clientImpl created");
        ClientImpl finalClient = client;
        new Thread(){
            public void run(){
                try{
                    System.out.println("Client ready to receive message from server");
                    serverStub.receive(finalClient);
                } catch (WaitingForEventSocketException e) {
                    System.out.println(e.getMessage());
                } catch (RemoteException e) {
                    System.err.println("Error while receiving message from server" + e.getMessage());
                    try{
                        serverStub.close();
                    } catch (RemoteException ex) {
                        System.err.println("Cannot close connection with server. Halting...");
                    }
                    System.exit(4);
                }
            }
        }.start();
        //client.run();
    }
}
