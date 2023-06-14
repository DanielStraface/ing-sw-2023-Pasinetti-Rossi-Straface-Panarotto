package it.polimi.ingsw.server;

import it.polimi.ingsw.client.CLI.AppClientRMI;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.distributed.socket.middleware.ClientSkeleton;
import it.polimi.ingsw.exceptions.*;

import java.io.FileNotFoundException;
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
import java.util.concurrent.TimeUnit;


public class AppServerImpl extends UnicastRemoteObject implements AppServer {
    private static AppServerImpl instance;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final Map<Integer, ServerImpl> matches = new HashMap<>();
    private static final Map<Integer, ServerImpl> waitingQueue = new HashMap<>();
    private static final Set<String> loggedNicknames = new TreeSet<>();
    private static final List<Integer> previousMatch = new ArrayList<>();
    private static int waitingMatchKey = 0;
    private static int activeMatchKey = 0;
    private static int FIRST_WAITING_MATCH = 0;
    private static final int SERVER_PORT = 1234;
    private static final String APPSERVER_REGISTRY_NAME = "it.polimi.ingsw.server.AppServer";
    public static final int MAX_MATCHES_MANAGED = 100;
    private static final int ERROR_WHILE_CREATING_SERVER_SOCKET = 1;
    private static final List<String> connectedRMIClient = new ArrayList<>();
    private static final List<Boolean> connectedRMIClientFlag = new ArrayList<>();
    private static final List<Boolean> noMoreHeartbeat = new ArrayList<>();

    /**
     * Constructor method
     * @throws RemoteException if AppServerImpl call goes wrong
     */
    protected AppServerImpl() throws RemoteException {
    }

    /**
     * Get method for the Server's instance
     * @return the Server's instance
     * @throws RemoteException if AppServerImpl call goes wrong
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
     * @param args String[]
     */
    public static void main(String[] args) {
        System.out.println("Server is starting...\nServer is ready!");

        setupActiveMatchId();

        Thread rmiThread = new Thread(){
            @Override
            public void run(){
                try{
                    startRMI();
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
     * @throws RemoteException if the communication with the registry goes wrong
     */
    public static void startRMI() throws RemoteException {
        AppServerImpl server = getInstance();
        System.out.println("Server is ready to receive clients requests via RMI (Remote Method Invocation)");
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind(APPSERVER_REGISTRY_NAME, server);
    }

    /**
     * Starts socket technology
     * @throws RemoteException if the execution of a remote method call goes wrong
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
                            temp = instance.log(nicknameToLog, false);
                            clientSkeleton.sendLoginResult(temp);
                            if(temp) {clientSkeleton.setNickname(nicknameToLog); break;}
                        } catch (RemoteException e) {
                            if(e.getMessage().contains("Connection reset"))
                                System.err.println("The client of clientSkeleton " + clientSkeleton + " has disconnected!");
                            return;
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
                        } catch (RemoteException e) {
                            if(e.getMessage().contains("Connection reset")){
                                System.err.println("The client of the nickname " + nicknameToLog +
                                        " has disconnected!");
                                try {
                                    instance.removeLoggedUser(nicknameToLog);
                                } catch (RemoteException ex) {
                                    System.err.println("Cannot remove the nickname of teh disconnected user: "
                                    + e.getMessage());
                                }
                                return;
                            }
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
                    String finalNicknameToLog = nicknameToLog;
                    ServerImpl finalServer = (ServerImpl) server;
                    new Thread(() -> {
                        while(true){
                            try{
                                if(socket.getInputStream().read() == -1){
                                    System.out.println("Disconnection");
                                }
                            } catch (IOException e) {
                                if(finalServer != null && !matches.containsKey(finalServer.getMatchId())) return;
                                System.out.println("THE USER OF " + finalNicknameToLog + " HAS DISCONNECTED!");
                                try {
                                    if(finalServer != null && !finalServer.getInactiveMatch()){
                                        List<String> notificationList = Collections.singletonList(finalNicknameToLog);
                                        //instance.removeLoggedUser(finalNicknameToLog);
                                        finalServer.update(notificationList);
                                        int matchID = (finalServer).getMatchId();
                                        if(waitingQueue.containsKey(matchID)) {
                                            waitingQueue.remove(matchID);
                                            System.out.println("Match # " + matchID +
                                                    " correctly removed from waiting queue");
                                        } else if(matches.containsKey(matchID)){
                                            matches.remove(matchID);
                                            System.out.println("Match # " + matchID +
                                                    " correctly removed from matches list");
                                        }
                                    }
                                } catch (RemoteException ex) {
                                    System.err.println("Cannot remove the nickname: " + e.getMessage());
                                }
                                break;
                            }
                        }
                        //System.out.println("This thread has ended its task");
                    }).start();
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
                    ServerImpl serverMatch = matches.remove(i);
                    for(String nickname : serverMatch.getMatchNicknames()){
                        try{
                            instance.removeLoggedUser(nickname);
                        } catch (RemoteException e) {
                            System.err.println("Cannot removed the nickname " + nickname + ": " + e.getMessage());
                        }
                    }
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

    /**
     * remove a game forcefully
     * @param matchID the ID of the game to be removed
     */
    public synchronized static void forceGameRemove(int matchID){
        if(matches.size() > 0 && matches.containsKey(matchID)){
            System.out.println("The running match # " + matchID + " must be removed!");
            List<String> matchNicknames = (matches.get(matchID)).getMatchNicknames();
            for(String nickname : matchNicknames){
                try {
                    instance.removeLoggedUser(nickname);
                } catch (RemoteException e) {
                    System.err.println("Cannot removed the nickname " + nickname + ": " + e.getMessage());
                }
            }
            matches.remove(matchID);
            System.out.println("The match # " + matchID + " is correctly removed!\nThere are "
                    + matches.size() + " matches now");
            System.out.println("The waiting queue is " + waitingQueue.size() + " matches long\n" +
                    "The next match to be served is # " + FIRST_WAITING_MATCH);
        }
        if(waitingQueue.size() > 0 && waitingQueue.containsKey(matchID)){
            System.out.println("The waiting match # " + matchID + " must be removed!");
            List<String> matchNicknames = (waitingQueue.get(matchID)).getMatchNicknames();
            matchNicknames.forEach(n -> System.out.println(n + " "));
            for(String nickname : matchNicknames){
                if(nickname != null) {
                    try {
                        instance.removeLoggedUser(nickname);
                    } catch (RemoteException e) {
                        System.err.println("Cannot removed user nickname " + nickname + ":" + e.getMessage());
                    }
                }
            }
            waitingQueue.remove(matchID);
            System.out.println("The waiting match # " + matchID + " is correctly removed!\nThere are "
                    + waitingQueue.size() + " waiting matches now");
            }
        //da sistemare
    }

    /**
     * sets up the active match ID to the next available value, based on the previous saved matches. If there are no previous matches, the
     * active match ID is not set. If the maximum number of saved matches is reached, an error message  is printed
     */
    private static void setupActiveMatchId(){
        for(int counter=0;counter<MAX_MATCHES_MANAGED;counter++){
            try {
                if(Controller.loadGame("match" + counter + ".ser") != null){
                    previousMatch.add(counter);
                }
            } catch (FileNotFoundException ignored) {
            }
        }
        if(previousMatch.size() == 0){
            System.out.println("There are no previous match saved.");
            return;
        }
        else System.out.println("There are " + previousMatch.size() + " previous match saved.");
        if(previousMatch.size() == MAX_MATCHES_MANAGED) System.err.println("Max saving file reached!");
        else {
            activeMatchKey = previousMatch.size();
            System.out.println("The first activeMatchKey is " + activeMatchKey);
        }
    }

    /**
     * Creates a new match depending on its type enum, puts it in the "matches" Map and creates a "waiting queue" Map
     * to manage the players' queue
     * @param type typeOfMatch enum
     * @return the match's server created
     * @throws RemoteException if the execution of a remote method call goes wrong
     * @throws NotSupportedMatchesException if there is an unsupported number of matches
     */
    @Override
    public Server connect(typeOfMatch type) throws RemoteException, NotSupportedMatchesException {
        ServerImpl match = null;
        switch(type){
            case newTwoPlayersGame, newThreePlayersGame, newFourPlayersGame -> {
                if(matches != null &&
                        (waitingQueue.size() + matches.size() + previousMatch.size() >= MAX_MATCHES_MANAGED)) {
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
                FIRST_WAITING_MATCH = waitingQueue.keySet().stream()
                        .min(Comparator.comparing(Integer::valueOf)).orElse(-1);
            }
            case existingGame -> {
                if(waitingQueue.size() == 0){
                    System.err.println("A client tried to join an existing match but the waiting list is empty");
                    return null;
                }
                if(FIRST_WAITING_MATCH == -1) {
                    FIRST_WAITING_MATCH = 0;
                }
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
    public boolean log(String nickname, boolean isRMI) {
        synchronized (loggedNicknames){
            boolean temp = loggedNicknames.add(nickname);
            if(temp) System.out.println("New client correctly logged in! Its name is " + nickname);
            else System.out.println("A client tried to log in but its nickname was already been chosen.");
            if(isRMI){
                new Thread(() -> {
                    synchronized (connectedRMIClient){
                        connectedRMIClient.add(nickname);
                        connectedRMIClientFlag.add(true);
                        noMoreHeartbeat.add(false);
                    }
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                System.out.println("This is " + nickname + " heartbeat");
                                checkClientStatus(nickname);
                            } catch (ClientRMITimeoutException e) {
                                timer.cancel();
                                if(e instanceof AnotherClientRMITimeoutException){
                                } else if(e instanceof NoMoreHeartbeatException) {
                                    System.out.println("Client " + nickname + " is entered in the game phase," +
                                            "no more heartbeat needed");
                                    timer.cancel();
                                    connectedRMIClient.remove(nickname);
                                } else {
                                    if(waitingQueue.size() > 0){
                                        List<List<String>> allWaitingMatchesNickname =
                                                waitingQueue.values().stream().map(ServerImpl::getMatchNicknames).toList();
                                        for(List<String> waitingMatchNicknames : allWaitingMatchesNickname){
                                            if(waitingMatchNicknames.contains(nickname)){
                                                for(String name : waitingMatchNicknames) {
                                                    try {
                                                        if(name != null) instance.removeLoggedUser(name);
                                                    } catch (RemoteException ex) {
                                                        System.err.println("Cannot removed nickname " + nickname +
                                                                " in ClientRMiTimeoutException waitingMatch: "
                                                                + ex.getMessage());
                                                    }
                                                }

                                                System.out.println("flag := " +
                                                        allWaitingMatchesNickname.indexOf(waitingMatchNicknames));
                                                ServerImpl serverMatch = waitingQueue.get(
                                                        waitingQueue.keySet().stream().toList().get(
                                                                allWaitingMatchesNickname.indexOf(waitingMatchNicknames)
                                                        )
                                                        );
                                                List<String> notificationList = Collections.singletonList(nickname);
                                                try {
                                                    serverMatch.update(notificationList);
                                                } catch (RemoteException ex) {
                                                    System.err.println("Cannot notify disconnectionList: " + e.getMessage());
                                                }
                                            }
                                            break;
                                        }
                                    } else {
                                        try {
                                            instance.removeLoggedUser(nickname);
                                        } catch (RemoteException ex) {
                                            System.err.println("Cannot removed nickname " + nickname +
                                                    " in ClientRMiTimeoutException: " + ex.getMessage());
                                        }
                                    }
                                }
                            }
                        }
                    }, 7500, CLIENT_TIMEOUT);
                    try{
                        TimeUnit.SECONDS.sleep(CLIENT_TIMEOUT / 2000);
                    } catch (InterruptedException ex) {
                        System.err.println("Cannot sleep while reset connection flag of client " + nickname);
                    }
                    while (true){
                        try{
                            TimeUnit.SECONDS.sleep(CLIENT_TIMEOUT / 1000);
                            synchronized (connectedRMIClient){
                                if(!connectedRMIClient.contains(nickname)) return;
                                connectedRMIClientFlag.remove(connectedRMIClient.indexOf(nickname));
                                connectedRMIClientFlag.add(connectedRMIClient.indexOf(nickname), false);
                            }
                        } catch (InterruptedException ex) {
                            System.err.println("Cannot sleep while reset connection flag of client " + nickname);
                        }
                    }
                }).start();
            }
            return temp;
        }
    }

    /**
     * Removes a logged nickname from the logged users String Set
     * @param nickname the player's nickname String to be removed
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public void removeLoggedUser(String nickname) throws RemoteException {
        synchronized (loggedNicknames){
            loggedNicknames.remove(nickname);
            System.out.println("Nickname log out of " + nickname);
        }
    }

    /**
     * sends a heartbeat signal to the server to communicate that the client is still connected
     * @param client  the identifier of the client
     * @return true to indicate the success of the signal
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public boolean heartbeat(String client) throws RemoteException {
        synchronized (connectedRMIClient){
            connectedRMIClientFlag.remove(connectedRMIClient.indexOf(client));
            connectedRMIClientFlag.add(connectedRMIClient.indexOf(client), true);
        }
        return true;
    }

    /**
     * stops the heartbeat signal from the client
     * @param client the identifier of the client
     * @return true to indicate the success of the  stop signal
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public boolean heartbeatStop(String client) throws RemoteException {
        synchronized (connectedRMIClient){
            noMoreHeartbeat.remove(connectedRMIClient.indexOf(client));
            noMoreHeartbeat.add(connectedRMIClient.indexOf(client), true);
            connectedRMIClientFlag.remove(connectedRMIClient.indexOf(client));
        }
        return true;
    }

    /**
     * check the status of the specified client for RMI connectivity and heartbeat signals
     * @param client the identifier of the client
     * @throws ClientRMITimeoutException if client RMI has disconnected during setup situations
     */
    public synchronized void checkClientStatus(String client) throws ClientRMITimeoutException {
        if(noMoreHeartbeat.get(connectedRMIClient.indexOf(client))) throw new NoMoreHeartbeatException();
        if(!loggedNicknames.contains(client)) throw new AnotherClientRMITimeoutException();
        if(!connectedRMIClientFlag.get(connectedRMIClient.indexOf(client))) throw new ClientRMITimeoutException();
        // heartbeat 0 6 12 18 24
        // reset 3 9 15 21 27
        // control 7.5 13.5
    }

    /**
     * adds the match ID to the list of previously saved matches
     * @param matchID the ID of match to be saved
     */
    public static void addPrevMatchSave(int matchID){
        previousMatch.add(matchID);
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

    /**
     * get method
     * @return the list of the previously saved match IDs
     */
    public static List<Integer> getPreviousMatch(){return previousMatch;}
}