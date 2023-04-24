package it.polimi.ingsw.server;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.distributed.socket.middleware.ClientSkeleton;
import it.polimi.ingsw.exceptions.NotMessageFromClientYet;
import it.polimi.ingsw.exceptions.TooManyMatchesException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class AppServerImpl extends UnicastRemoteObject implements AppServer {
    private static AppServerImpl instance;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private static Map<Integer, ServerImpl> matches;
    private static Map<Integer, ServerImpl> waitingQueue;
    private static int FIRST_WAITING_MATCH;
    private static final int JOIN_EXISTING_GAME = 0;
    private static final int TYPE_OF_MATCH = 1;
    private static final int NICKNAME_POSITION = 2;
    private static final int SERVER_PORT = 1234;
    private static final String APPSERVER_REGISTRY_NAME = "it.polimi.ingsw.server.AppServer";
    private static final int MAX_MATCHES_MANAGED = 100;
    private static final int ERROR_WHILE_CREATING_SERVER_SOCKET = 1;
    protected AppServerImpl() throws RemoteException {
    }
    public static AppServerImpl getInstance() throws RemoteException {
        if(instance == null){
            instance = new AppServerImpl();
        }
        return instance;
    }

    public static void main(String[] args) {
        System.out.println("Server is starting...\nServer is ready!");
        Thread rmiThread = new Thread(){
            @Override
            public void run(){
                try{
                    startRMI();
                    gameFinished();
                } catch (RemoteException e) {
                    System.err.println("Cannot start RMI. This protocol will be disabled.");
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

        /*Thread finishedGameThread = new Thread(){
            @Override
            public void run(){
                while(true){
                    gameFinished();
                }
            }
        };
        finishedGameThread.start();*/

        try {
            rmiThread.join();
            socketThread.join();
        } catch (InterruptedException e) {
            System.err.println("No connection protocol available. Exiting...");
        }
    }

    public static void startRMI() throws RemoteException {
        AppServerImpl server = getInstance();
        System.out.println("Server is ready to receive clients requests via RMI (Remote Method Invocation)");
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind(APPSERVER_REGISTRY_NAME, server);
    }

    public static void startSocket() throws RemoteException {
        AppServerImpl instance = getInstance();
        try(ServerSocket serverSocket = new ServerSocket(SERVER_PORT)){
            while(true){
                System.out.println("Server is ready to receive clients requests via socket");
                Socket socket = serverSocket.accept();
                instance.executorService.submit(() ->{
                    String NoGame = "NO_GAME";
                    int numberOfRequest = 0;
                    try{
                        System.out.println("Server accept a new connection from "
                                + socket.getInetAddress() + " on port" + socket.getPort());
                        ClientSkeleton clientSkeleton = new ClientSkeleton(socket);
                        Server server = null;
                        List<Object> information = clientSkeleton.receive();
                        Integer typeOfMatch = (Integer) information.get(TYPE_OF_MATCH);
                        if(typeOfMatch.intValue() != JOIN_EXISTING_GAME) {
                            try{
                                server = instance.connect("NEW GAME");
                            } catch (TooManyMatchesException e){
                                clientSkeleton.update(e.getMessage());
                            }
                        }
                        else server = instance.connect();
                        if(server == null){
                            clientSkeleton.update(NoGame);
                            clientSkeleton.update("There are no match at this moment for you.." +
                                    "\nPlease, reboot application and choose 'to Start a new game'.");
                        }
                        String nickname = (String) information.get(NICKNAME_POSITION);
                        server.register(clientSkeleton, typeOfMatch, nickname);
                        System.out.println("Server is ready to receive message from " + socket.getInetAddress());
                        while(true){
                            try {
                                clientSkeleton.receive(server);
                            } catch (NotMessageFromClientYet e) {
                            } catch (RemoteException e) {
                                System.err.println("Error while receiving message from client: " + e.getMessage());
                            }
                            numberOfRequest++;
                        }
                    } catch(RemoteException e) {
                        System.err.println("Cannot establish a connection while setting the match with" +
                                " the client. Closing this connection..." + e.getMessage());
                    } finally {
                        try{
                            socket.close();
                            System.out.println("Connection closed");
                        } catch (IOException e) {
                            System.err.println("Cannot close socket");
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

    public static void gameFinished(){
        if(matches != null){
            for(Integer i : matches.keySet()){
                if(matches.get(i).getGameOver()) {
                    matches.remove(i);
                    System.out.println("The match #" + i + " is finished!\nRemoving from matches...Done!\n" +
                            "There are " + matches.size() + " matches now");
                }
            }
        }
    }

    @Override
    public Server connect() throws RemoteException {
        if( waitingQueue == null || waitingQueue.size() == 0){
            System.out.println("No game in waiting list...create a new match display to client user");
            return null;
        }
        ServerImpl match = waitingQueue.get(FIRST_WAITING_MATCH);
        int numberOfClientConnected = match.connectedClient;
        if(numberOfClientConnected == match.getPlayersGameNumber() - 1) {
            matches.put(matches.size(), waitingQueue.remove(FIRST_WAITING_MATCH));
            if(waitingQueue.size() >= 0){
                FIRST_WAITING_MATCH++;
            } else {
                FIRST_WAITING_MATCH = 0;
                return connect();
            }
            System.out.println("New match is running!\nThe next match to be served is the #" + FIRST_WAITING_MATCH);
        }
        match.connectedClient++;
        System.out.println("The current running matches are " + matches.size() +
                "\nThe waiting queue is " + waitingQueue.size() + " matches long");
        return match;
    }

    @Override
    public Server connect(String newGame) throws RemoteException, TooManyMatchesException {
        if(waitingQueue == null){
            waitingQueue = new HashMap<>();
            matches = new HashMap<>();
            FIRST_WAITING_MATCH = 0;
        }
        if(matches != null && (waitingQueue.size() + matches.size() >= MAX_MATCHES_MANAGED)){
            System.err.println("Too many matches managed!");
            throw new TooManyMatchesException();
        }

        ServerImpl match = null;
        try {
            match = new ServerImpl();
        } catch (RemoteException e) {
            System.err.println("Error while creating the match server in ServerImpl class" + e.getMessage());
        }
        waitingQueue.put(waitingQueue.size(), match);
        match.connectedClient++;
        System.out.println("The current running matches are " + matches.size() +
                        "\nThe waiting queue is " + waitingQueue.size() + " matches long");
        if(newGame.equals("NO MATCH FOUND")) match.connectedClient += 5;
        return match;
    }
}