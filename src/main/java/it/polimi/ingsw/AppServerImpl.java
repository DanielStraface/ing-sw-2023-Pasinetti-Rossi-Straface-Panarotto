package it.polimi.ingsw;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.distributed.socket.middleware.ClientSkeleton;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AppServerImpl extends UnicastRemoteObject implements AppServer {
    private static AppServerImpl instance;
    private static Map<Integer, ServerImpl> matches;
    private static Map<Integer, ServerImpl> waitingQueue;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private static int FIRST_WAITING_MATCH;
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
        System.out.println("Server is ready to receive clients requests via socket");
        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("server", server);
    }

    public static void startSocket() throws RemoteException {
        AppServerImpl instance = getInstance();
        try(ServerSocket serverSocket = new ServerSocket(1234)){
            while(true){
                System.out.println("Server is ready to receive clients requests via socket");
                Socket socket = serverSocket.accept();
                instance.executorService.submit(() ->{
                    try{
                        System.out.println("Server accept a new connection from "
                                + socket.getInetAddress() + " on port" + socket.getPort());
                        ClientSkeleton clientSkeleton = new ClientSkeleton(socket);
                        Server server = instance.connect();
                        clientSkeleton.receive(server);
                        //server.register(clientSkeleton, 2, "myName");
                        while(true){
                            System.out.println("Server is ready to receive message from " + socket.getInetAddress());
                            clientSkeleton.receive(server);
                        }
                    } catch(RemoteException e) {
                        System.err.println("Cannot receive from client. Closing this connection...");
                    } finally {
                        try{
                            socket.close();
                        } catch (IOException e) {
                            System.err.println("Cannot close socket");
                        }
                    }
                });
            }
        } catch (IOException e) {
            throw new RemoteException("Cannot start socket server", e);
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
    public Server connect(String newGame) throws RemoteException {
        if(waitingQueue == null){
            waitingQueue = new HashMap<>();
            matches = new HashMap<>();
            FIRST_WAITING_MATCH = 0;
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
        /*for(int i=0;i<waitingQueue.size();i++){
            System.out.println("Waiting match #" + i + " is a " + waitingQueue.get(i).getPlayersGameNumber() +
                    " but there are only " + match.connectedClient);
        }*/
        if(newGame.equals("NO MATCH FOUND")) match.connectedClient += 5;
        return match;
    }
}