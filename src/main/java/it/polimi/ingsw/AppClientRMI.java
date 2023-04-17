package it.polimi.ingsw;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ClientImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppClientRMI {
    private static final int TYPE_OF_MATCH_POSITION = 0;
    private static final int NUMBER_OF_PLAYER_POSITION = 1;
    private static final int NICKNAME_POSITION = 2;
    private static final Integer JOINING_EXISTING_GAME = 0;

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String nickname = args[NICKNAME_POSITION];
        Registry registry = LocateRegistry.getRegistry();
        AppServer server = (AppServer) registry.lookup("server");
        switch (args[TYPE_OF_MATCH_POSITION]) {
            case "1" -> {
                ClientImpl client = new ClientImpl(server.connect("NEW_GAME"),
                        Integer.parseInt(args[NUMBER_OF_PLAYER_POSITION]), nickname);
            }
            case "2" -> {
                Server ref = server.connect();
                if(ref == null){
                    System.out.println("There are no match at this moment for you..\nPlease, reboot application and" +
                            " choose 'to Start a new game'.");
                    System.exit(3);
                }
                ClientImpl client = new ClientImpl(ref, JOINING_EXISTING_GAME, nickname);
            }
            //case "3" -> server.loadFromFile();
            default -> {
                System.exit(20);
            }
        }
    }
}