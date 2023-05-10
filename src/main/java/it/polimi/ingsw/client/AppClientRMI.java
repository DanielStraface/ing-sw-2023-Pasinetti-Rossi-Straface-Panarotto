package it.polimi.ingsw.client;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.exceptions.NoMatchException;
import it.polimi.ingsw.exceptions.NotSupportedMatchesException;
import it.polimi.ingsw.exceptions.TooManyMatchesException;
import it.polimi.ingsw.server.AppServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

public class AppClientRMI extends AppClient{
    private static final String APPSERVER_REGISTRY_NAME = "it.polimi.ingsw.server.AppServer";

    public static void main(String[] args) throws RemoteException, NotBoundException {
        Server matchServerRef = null;
        Registry registry = LocateRegistry.getRegistry();
        AppServer serverApp = (AppServer) registry.lookup(APPSERVER_REGISTRY_NAME);
        System.out.print("\nConnection successfully created!\nPlease log in with your nickname before play:");
        logginToAppServer(serverApp, null);
        List<Integer> decisions = mainMenu();
        /* -- create or join a match -- */
        ClientImpl userClient = null;
        switch (decisions.get(TYPE_OF_MATCH_POSITION)) {
            case CREATE_A_NEW_MATCH -> {
                System.out.println("Creation of a new match in progress...");
                try{
                    AppServer.typeOfMatch tom = AppServer.typeOfMatch.newTwoPlayersGame;
                    for(AppServer.typeOfMatch t : AppServer.typeOfMatch.values()){
                        if(t.ordinal() + 1 == decisions.get(NUMBER_OF_PLAYER_POSITION))
                            tom = t;
                    }
                    matchServerRef = serverApp.connect(tom);
                    userClient = new ClientImpl(matchServerRef, nickname);
                } catch (NotSupportedMatchesException e) {
                    if (e instanceof TooManyMatchesException) {
                        serverApp.removeLoggedUser(nickname);
                        System.out.print(e.getMessage() + "\nClient termination...");
                        System.exit(QUIT_IN_APPCLIENTRMI_ERROR);
                    }
                }
            }
            case JOIN_EXISTING_MATCH -> {
                System.out.println("Joining an existing match in progress...");
                try {
                     matchServerRef = serverApp.connect(AppServer.typeOfMatch.existingGame);
                } catch (NotSupportedMatchesException e) {
                    if(e instanceof NoMatchException) System.err.println("Something went wrong, not reachable section");
                }
                if(matchServerRef == null){
                    System.out.println("There are no match at this moment for you..\nPlease, reboot application and" +
                            " choose 'to Start a new game'.");
                    serverApp.removeLoggedUser(nickname);
                    System.exit(NO_MATCH_IN_WAITING_NOW_ERROR);
                }
                userClient = new ClientImpl(matchServerRef, nickname);
            }
            default -> {
                System.exit(QUIT_IN_APPCLIENTRMI_ERROR);
            }
        }
        System.out.println("QUI");
        ClientImpl finalUserClient = userClient;
        new Thread(() -> {
            while(true){
                if(finalUserClient.getClientState() == ClientImpl.ClientState.GAMEOVER) break;
            }
        }).start();
        if(matchServerRef != null) matchServerRef.startGame();
    }
}