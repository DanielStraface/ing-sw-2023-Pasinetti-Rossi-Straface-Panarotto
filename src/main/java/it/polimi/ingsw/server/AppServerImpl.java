package it.polimi.ingsw.server;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.distributed.socket.middleware.ClientSkeleton;
import it.polimi.ingsw.exceptions.ClientDisconnectionException;
import it.polimi.ingsw.exceptions.NotSupportedMatchesException;
import it.polimi.ingsw.exceptions.TooManyMatchesException;
import it.polimi.ingsw.model.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AppServerImpl extends UnicastRemoteObject implements AppServer {
    private static AppServerImpl instance;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final Map<Integer, ServerImpl> matches = new HashMap<>();
    private static final Map<Integer, ServerImpl> waitingQueue = new HashMap<>();
    private static final Set<String> loggedNicknames = new TreeSet<>();
    private static int waitingMatchKey = 0;
    private static int activeMatchKey = 0;
    private static int FIRST_WAITING_MATCH = 0;
    private static final int SERVER_PORT = 1234;
    private static final String APPSERVER_REGISTRY_NAME = "it.polimi.ingsw.server.AppServer";
    public static final int MAX_MATCHES_MANAGED = 100;
    private static final int ERROR_WHILE_CREATING_SERVER_SOCKET = 1;

    /**
     * Constructor method
     * @throws RemoteException
     */
    protected AppServerImpl() throws RemoteException {
    }

    /**
     * Get method for the Server's instance
     * @return the Server's instance
     * @throws RemoteException
     */
    public static AppServerImpl getInstance() throws RemoteException {
        if(instance == null){
            instance = new AppServerImpl();
        }
        return instance;
    }

    /**
     * Main method: Starts both RMI and socket technologies by invoking their respective method,
     * everything inside a run method
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Server is starting...\nServer is ready!");
        Thread rmiThread = new Thread(){
            @Override
            public void run(){
                try{
                    startRMI();
                    //gameFinished();
                } catch (RemoteException e) {
                    System.err.println("Cannot start RMI. This protocol will be disabled. " + e.getMessage());
                }
            }
        };
        rmiThread.start();

        Thread socketThread = new Thread(){
            @Override
            public void run(){
                try{
                    startSocket();
                } catch (RemoteException e) {
                    System.err.println("Cannot start socket. This protocol will be disabled.");
                }
            }
        };
        socketThread.start();

        try {
            rmiThread.join();
            socketThread.join();
        } catch (InterruptedException e) {
            System.err.println("No connection protocol available. Exiting...");
        }
    }

    /**
     * Starts RMI technology
     * @throws RemoteException
     */
    public static void startRMI() throws RemoteException {
        AppServerImpl server = getInstance();
        System.out.println("Server is ready to receive clients requests via RMI (Remote Method Invocation)");
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind(APPSERVER_REGISTRY_NAME, server);
    }

    /**
     * Starts socket technology
     * @throws RemoteException
     */
    public static void startSocket() throws RemoteException {
        AppServerImpl instance = getInstance();
        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
            while(true){
                System.out.println("Server is ready to receive clients requests via socket");
                Socket socket = serverSocket.accept();
                instance.executorService.submit(() -> {
                    Server server = null;
                    ClientSkeleton clientSkeleton = null;
                    try {
                        clientSkeleton = new ClientSkeleton(socket);
                    } catch (RemoteException e) {
                        System.err.println("Cannot create clientSkeleton: " + e.getMessage());
                        System.exit(-1);
                    }
                    String nicknameToLog = null;
                    boolean temp;
                    while(true){
                        try {
                            nicknameToLog = clientSkeleton.receiveNicknameToLog();
                            temp = instance.log(nicknameToLog);
                            clientSkeleton.sendLoginResult(temp);
                            if(temp) {clientSkeleton.setNickname(nicknameToLog); break;}
                        } catch (RemoteException ignored) {
                        }
                    }
                    while(true){
                        try{
                            AppServer.typeOfMatch type = clientSkeleton.setupMatch();
                            try{
                                server = instance.connect(type);
                                if(server != null){
                                    clientSkeleton.sendMatchServer(true);
                                    server.register(clientSkeleton, nicknameToLog);
                                } else {
                                    clientSkeleton.sendMatchServer(false);
                                    instance.removeLoggedUser(nicknameToLog);
                                }
                            } catch (NotSupportedMatchesException e){
                                instance.removeLoggedUser(nicknameToLog);
                                clientSkeleton.sendMatchServer(false);
                            }
                            break;
                        } catch (RemoteException ignored) {
                        }
                    }
                    while(true){
                        try {
                            clientSkeleton.receive(server);
                        } catch (RemoteException e) {
                            System.err.println("Error while receiving message from client: " + e.getMessage());
                        } catch (ClientDisconnectionException e){
                            break;
                        }
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Problem occurred!!\nCannot manage server socket " + e.getMessage() +
                    "Server termination...");
            System.out.println(ERROR_WHILE_CREATING_SERVER_SOCKET);
        }
    }

    /**
     * Removes a match from its Map after it is over and deletes its saved .ser file
     */
    public static void gameFinished(){
        if(matches != null){
            for(Integer i : matches.keySet()){
                if(matches.get(i).getGameOver()) {
                    matches.remove(i);
                    System.out.println("The match #" + i + " is finished!\nRemoving from matches...Done!\n" +
                            "There are " + matches.size() + " matches now");
                    try {
                        Path path = FileSystems.getDefault().getPath("./match" + i + ".ser");
                        if(Files.deleteIfExists(path))
                            System.out.println("Saving file of match # " + i + " delete successfully");
                        else System.err.println("Error while delete the saving file of match # " + i);
                    } catch (IOException e) {
                        System.err.println("Error while deleting the saving file of match # " + i);
                    }
                }
            }
        }
    }

    public synchronized static void forceGameRemove(int matchID){
        if(matches != null){
            System.out.println("The match # " + matchID + " must be removed!");
            List<String> matchNicknames = (matches.get(matchID)).getMatchNicknames();
            System.out.println("mN size := " + matchNicknames.size());
            for(String nickname : matchNicknames){
                System.out.println("IIII");
                loggedNicknames.remove(nickname);
            }
            matches.remove(matchID);
            System.out.println("The match # " + matchID + " is correctly removed!\nThere are "
                    + matches.size() + " matches now");
            System.out.println("The waiting queue is " + waitingQueue.size() + " matches long\n" +
                    "The next match to be served is # " + FIRST_WAITING_MATCH);
        } else System.err.println("There are no match to be removed in matches list!");
    }

    /**
     * Creates a new match depending on its type enum, puts it in the "matches" Map and creates a "waiting queue" Map
     * to manage the players' queue
     * @param type typeOfMatch enum
     * @return the match's server created
     * @throws RemoteException
     * @throws NotSupportedMatchesException
     */
    @Override
    public Server connect(typeOfMatch type) throws RemoteException, NotSupportedMatchesException {
        ServerImpl match = null;
        switch(type){
            case newTwoPlayersGame, newThreePlayersGame, newFourPlayersGame -> {
                if(matches != null && (waitingQueue.size() + matches.size() >= MAX_MATCHES_MANAGED)) {
                    System.err.println("Too many matches managed!");
                    throw new TooManyMatchesException();
                }
                try {
                    match = new ServerImpl(type);
                    waitingQueue.put(waitingMatchKey, match);
                    waitingMatchKey++;
                    match.connectedClient++;
                    System.out.println("The current running matches are " + matches.size() +
                            "\nThe waiting queue is " + waitingQueue.size() + " matches long");
                } catch (RemoteException e) {
                    System.err.println("Error while creating the match server in ServerImpl class" + e.getMessage());
                }
            }
            case existingGame -> {
                if(waitingQueue.size() == 0){
                    System.err.println("A client tried to join an existing match but the waiting list is empty");
                    return null;
                }
                System.out.println("fwm := " + FIRST_WAITING_MATCH);
                match = waitingQueue.get(FIRST_WAITING_MATCH);
                int numberOfClientConnected = match.connectedClient;
                if(numberOfClientConnected == match.getPlayersGameNumber() - 1) {
                    matches.put(activeMatchKey, waitingQueue.remove(FIRST_WAITING_MATCH));
                    activeMatchKey++;
                    if(waitingQueue.size() == 0){
                        FIRST_WAITING_MATCH = waitingMatchKey;
                        System.out.println("No match in waiting");
                    }
                    else {
                        FIRST_WAITING_MATCH = waitingQueue.keySet().stream()
                                .min(Comparator.comparing(Integer::valueOf)).orElse(-1);
                        System.out.println("The next match to be served is the #" + FIRST_WAITING_MATCH);
                    }
                }
                match.connectedClient++;
                System.out.println("The current running matches are " + matches.size() +
                        "\nThe waiting queue is " + waitingQueue.size() + " matches long");
            }
            default -> {
                System.err.println("Something went wrong, not supported enum");
            }
        }
        return match;
    }

    /**
     * Prints a login message and adds the nicknames to the logged users String Set
     * @param nickname the joined player's nickname String
     * @return true boolean if the nickname isn't already present
     */
    @Override
    public boolean log(String nickname) {
        synchronized (loggedNicknames){
            boolean temp = loggedNicknames.add(nickname);
            if(temp) System.out.println("New client correctly logged in! Its name is " + nickname);
            else System.out.println("A client tried to log in but its nickname was already been chosen.");
            return temp;
        }
    }

    /**
     * Removes a logged nickname from the logged users String Set
     * @param nickname the player's nickname String to be removed
     * @throws RemoteException
     */
    @Override
    public void removeLoggedUser(String nickname) throws RemoteException {
        synchronized (loggedNicknames){
            loggedNicknames.remove(nickname);
            System.out.println("Nickname log out of " + nickname);
        }
    }

    /**
     * Get method for the matchID int given its Server
     * @param server the Server to get its matchID from
     * @return int -> match's ID
     */
    public static int getMatchID(Server server){
        return matches.entrySet()
                .stream()
                .filter(entry -> server.equals(entry.getValue()))
                .mapToInt(Map.Entry::getKey)
                .boxed()
                .toList()
                .get(0);
    }
}