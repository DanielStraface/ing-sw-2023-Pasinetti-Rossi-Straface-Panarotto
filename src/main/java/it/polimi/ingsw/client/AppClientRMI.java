package it.polimi.ingsw.client;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.exceptions.TooManyMatchesException;
import it.polimi.ingsw.server.AppServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class AppClientRMI {
    private static final int TYPE_OF_MATCH_POSITION = 0;
    private static final int NUMBER_OF_PLAYER_POSITION = 1;
    private static final int NICKNAME_POSITION = 2;
    private static final String TO_START_NEW_GAME = "NEW GAME";
    private static final Integer JOINING_EXISTING_GAME = 0;
    private static final String APPSERVER_REGISTRY_NAME = "it.polimi.ingsw.server.AppServer";
    private static final int NO_MATCH_IN_WAITING_NOW_ERROR = 3;
    private static final int QUIT_IN_APPLCLIENTRMI_ERROR = 4;

    public static void main(String[] args) throws RemoteException, NotBoundException {
        String nickname = args[NICKNAME_POSITION];
        Registry registry = LocateRegistry.getRegistry();
        AppServer serverApp = (AppServer) registry.lookup(APPSERVER_REGISTRY_NAME);
        switch (args[TYPE_OF_MATCH_POSITION]) {
            case "1" -> {
                try{
                    new ClientImpl(serverApp.connect(TO_START_NEW_GAME),
                            Integer.parseInt(args[NUMBER_OF_PLAYER_POSITION]), nickname);
                } catch (TooManyMatchesException e) {
                    System.out.print(e.getMessage() + "\nClient termination...");
                    System.exit(QUIT_IN_APPLCLIENTRMI_ERROR);
                }
                /*new ClientImpl(serverApp.connect(TO_START_NEW_GAME),
                        Integer.parseInt(args[NUMBER_OF_PLAYER_POSITION]), nickname);*/
            }
            case "2" -> {
                Server ref = serverApp.connect();
                if(ref == null){
                    System.out.println("There are no match at this moment for you..\nPlease, reboot application and" +
                            " choose 'to Start a new game'.");
                    System.exit(NO_MATCH_IN_WAITING_NOW_ERROR);
                }
                new ClientImpl(ref, JOINING_EXISTING_GAME, nickname);
            }
            //case "3" -> server.loadFromFile();
            default -> {
                System.exit(QUIT_IN_APPLCLIENTRMI_ERROR);
            }
        }
    }
}