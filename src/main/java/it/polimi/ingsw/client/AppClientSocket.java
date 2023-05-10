package it.polimi.ingsw.client;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.exceptions.NoMatchException;
import it.polimi.ingsw.exceptions.NotMessageFromServerYet;
import it.polimi.ingsw.exceptions.NotSupportedMatchesException;
import it.polimi.ingsw.exceptions.TooManyMatchesException;
import it.polimi.ingsw.server.AppServer;

import java.rmi.RemoteException;
import java.util.List;

public class AppClientSocket extends AppClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;
    public static void main(String[] args) throws RemoteException {
        ServerStub appServerStub = new ServerStub(SERVER_ADDRESS, SERVER_PORT);
        System.out.print("\nConnection successfully created!\nPlease log in with your nickname before play:");
        logginToAppServer(null, appServerStub);
        List<Integer> decisions = mainMenu();
        /* -- create or join a match -- */
        switch (decisions.get(TYPE_OF_MATCH_POSITION)) {
            case CREATE_A_NEW_MATCH -> {
                System.out.println("Creation of a new match in progress...");
                try{
                    AppServer.typeOfMatch tom = AppServer.typeOfMatch.newTwoPlayersGame;
                    for(AppServer.typeOfMatch t : AppServer.typeOfMatch.values()){
                        if(t.ordinal() + 1 == decisions.get(NUMBER_OF_PLAYER_POSITION))
                            tom = t;
                    }
                    appServerStub.connect(tom);
                } catch (NotSupportedMatchesException e) {
                    if(e instanceof TooManyMatchesException){
                        System.out.print(e.getMessage() + "\nClient termination...");
                        appServerStub.removeLoggedUser(nickname);
                        System.exit(QUIT_IN_APPLCLIENTSOCKET_ERROR);
                    }
                }
                new ClientImpl(appServerStub, nickname);
            }
            case JOIN_EXISTING_MATCH -> {
                System.out.println("Joining an existing match in progress...");
                try{
                    appServerStub.connect(AppServer.typeOfMatch.existingGame);
                } catch (NotSupportedMatchesException e) {
                    if(e instanceof NoMatchException){
                        System.out.println("There are no match at this moment for you..\nPlease, reboot application and" +
                                " choose 'to Start a new game'.");
                        System.exit(NO_MATCH_IN_WAITING_NOW_ERROR);
                    }
                }
                new ClientImpl(appServerStub, nickname);
            }
            default -> {
                try{
                    appServerStub.close();
                } catch (RemoteException e) {
                    System.err.println("Cannot close serverStub, error: " + e.getMessage());
                }
                System.exit(QUIT_IN_APPLCLIENTSOCKET_ERROR);
            }
        }
    }
}
