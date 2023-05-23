package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.exceptions.NoMatchException;
import it.polimi.ingsw.exceptions.NotMessageFromServerYet;
import it.polimi.ingsw.exceptions.NotSupportedMatchesException;
import it.polimi.ingsw.exceptions.TooManyMatchesException;
import it.polimi.ingsw.server.AppServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class AppClientSocket extends AppClient {
    private static final String SERVER_ADDRESS = "172.20.10.11";
    private static final int SERVER_PORT = 1234;
    public static void launchClient(Object[] args) throws RemoteException {
        ServerStub appServerStub = new ServerStub(SERVER_ADDRESS, SERVER_PORT);
        System.out.print("\nConnection successfully created!\nPlease log in with your nickname before play:");
        ClientImpl userClient = null;
        List<Integer> decisions = null;
        UIType uiType = null;
        Object uiReference = null;
        if(args[0].equals("CLI")){
            uiType = UIType.CLI;
            logginToAppServer(uiType,null, appServerStub);
            decisions = TextualUI.setupConnectionByUser();
        }
        if(args[0].equals("GUI")){
            uiType = UIType.GUI;
            decisions = new ArrayList<>();
            System.out.println("Wait for user match choices");
            nickname = (String) args[1];
            if(!logginToAppServer(uiType, null, appServerStub)){
                ((GUI) args[4]).askNicknameManager();
                try{
                    appServerStub.close();
                } catch (RemoteException e) {
                    System.err.println("Cannot close serverStub, error: " + e.getMessage());
                }
                return;
            }
            if(args[2].equals("Create a new match")){
                decisions.add(CREATE_A_NEW_MATCH);
                String temp = (String) args[3];
                decisions.add(Integer.parseInt(temp.substring(0, 1)));
            }
            else decisions.add(JOIN_EXISTING_MATCH);
            uiReference = args[4];
        }
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
                userClient = new ClientImpl(appServerStub, nickname, uiType, uiReference);
            }
            case JOIN_EXISTING_MATCH -> {
                System.out.println("Joining an existing match in progress...");
                try{
                    appServerStub.connect(AppServer.typeOfMatch.existingGame);
                } catch (NotSupportedMatchesException e) {
                    String msgToSend = "There are no match at this moment for you..\nPlease, reboot application and" +
                            " choose 'to Start a new game'.";
                    if(e instanceof NoMatchException){
                        if(args[0].equals("CLI")){
                            System.out.println(msgToSend);
                            System.exit(NO_MATCH_IN_WAITING_NOW_ERROR);
                        }
                        if(args[0].equals("GUI"))
                            ((UI) args[4]).update(msgToSend);
                        return;

                    }
                }
                userClient = new ClientImpl(appServerStub, nickname, uiType, uiReference);
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
        ClientImpl finalUserClient = userClient;
        new Thread(() -> {
            while (true) {
                try {
                    appServerStub.receive(finalUserClient);
                    if(finalUserClient.getClientState() == ClientImpl.ClientState.GAMEOVER) break;
                } catch (NotMessageFromServerYet | RemoteException ignored) {
                }
            }
        }).start();
    }
}
