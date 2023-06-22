package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.client.UI;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.exceptions.NoMatchException;
import it.polimi.ingsw.exceptions.NotSupportedMatchesException;
import it.polimi.ingsw.exceptions.TooManyMatchesException;
import it.polimi.ingsw.server.AppServer;

import java.net.SocketException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The AppClientRMI class represents a specific type of AppClient class used for the RMI connection type.
 * It contains a heartbeat method to monitor the client activity connection status.
 * See AppClient class documentation for more information.
 */
public class AppClientRMI extends AppClient{
    private static final String APPSERVER_REGISTRY_NAME = "it.polimi.ingsw.server.AppServer";
    private static final int SERVER_PORT = 1099;
    private static final long HEARTBEAT_INTERVAL = 6000;
    private static boolean inGameFlag = false;
    private static final Object lock = new Object();

    /**
     * Manages all aspects of RMI connection, the choice between CLI/GUI and to either create or join a match
     * @param args -> Object[]
     * @throws RemoteException due to the invocation of methods: getRegistry, lookup, connect, removeLoggedUser
     * @throws NotBoundException when a name is not currently bound
     */
    public static void launchClient(Object[] args) throws RemoteException, NotBoundException {
        Server matchServerRef = null;
        String serverAddress = (String) args[1];
        Registry registry = LocateRegistry.getRegistry(serverAddress,SERVER_PORT);
        AppServer serverApp = (AppServer) registry.lookup(APPSERVER_REGISTRY_NAME);
        System.out.print("\nConnection successfully created!\nPlease log in with your nickname before play:");
        List<Integer> decisions = null;
        UIType uiType = null;
        Object uiReference = null;
        ClientImpl refClientImpl = null;
        boolean isOk;
        if(args[0].equals("CLI")){
            uiType = UIType.CLI;
            do {
                isOk = logginToAppServer(uiType, serverApp, null);
                if(!isOk) System.out.print("\nThis nickname is already used by another user, you must choose another one.");
            } while (!isOk);
            System.out.print("Log successfully completed!");
            startHeartbeat(serverApp);
            decisions = TextualUI.setupConnectionByUser();
        }
        if(args[0].equals("GUI")){
            uiType = UIType.GUI;
            decisions = new ArrayList<>();
            System.out.println("Wait for user match choices");
            nickname = (String) args[2];
            if(!logginToAppServer(uiType, serverApp, null)){
                ((GUI) args[5]).askNicknameManager();
                return;
            }
            if(args[3].equals("Create/Continue a match")){
                decisions.add(CREATE_A_NEW_MATCH);
                String temp = (String) args[4];
                decisions.add(Integer.parseInt(temp.substring(0, 1)));
            }
            else decisions.add(JOIN_EXISTING_MATCH);
            uiReference = args[5];
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
                    matchServerRef = serverApp.connect(tom);
                    refClientImpl = new ClientImpl(matchServerRef, nickname, uiType, uiReference);
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
                    String msgToSend = "There are no match at this moment for you.." +
                            "\nPlease, reboot application and choose 'to Create/Continue a match'.";
                    serverApp.removeLoggedUser(nickname);
                    if(args[0].equals("CLI")){
                        System.out.println(msgToSend);
                        System.exit(NO_MATCH_IN_WAITING_NOW_ERROR);
                    }
                    if(args[0].equals("GUI"))
                        ((UI) args[5]).update(msgToSend);
                    return;
                }
                refClientImpl = new ClientImpl(matchServerRef, nickname, uiType, uiReference);
            }
            default -> System.exit(QUIT_IN_APPCLIENTRMI_ERROR);
        }

        if(args[0].equals("GUI")) startHeartbeat(serverApp);

        ClientImpl finalRefClientImpl = refClientImpl;
        new Thread(() -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    synchronized (lock){
                        if(finalRefClientImpl.getClientState() == ClientImpl.ClientState.PLAYING){
                            inGameFlag = true;
                            timer.cancel();
                        }
                    }
                }
            }, HEARTBEAT_INTERVAL, HEARTBEAT_INTERVAL);
        }).start();
        if(matchServerRef != null) matchServerRef.startGame();
    }

    /**
     * Manage the haertbeat by client side aspect.
     * Call the remote heartbeat method on server side periodically in order to implement the heartbeat.
     * @param serverApp -> AppServer, the current serverApp of the game
     */
    private static void startHeartbeat(AppServer serverApp){
        new Thread(() -> {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    try {
                        if(!inGameFlag){
                            serverApp.heartbeat(nickname);
                        } else {
                            timer.cancel();
                            serverApp.heartbeatStop(nickname);
                        }
                    } catch (RemoteException e) {
                        System.err.println("Cannot call the heartbeat method: ");
                        if(e.getCause() instanceof SocketException){
                            System.err.println("Disconnection occurred - " + e.getCause().getMessage());
                            System.exit(-2);
                        } else System.err.print(e.getMessage());
                    }
                }
            }, 0, HEARTBEAT_INTERVAL);
        }).start();
    }
}