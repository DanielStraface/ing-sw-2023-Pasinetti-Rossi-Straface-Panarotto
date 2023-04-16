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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppServerImpl extends UnicastRemoteObject implements AppServer {
    private static AppServerImpl instance;
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    protected AppServerImpl() throws RemoteException {
    }
    public static AppServerImpl getInstance() throws RemoteException {
        if(instance == null){
            instance = new AppServerImpl();
        }
        return instance;
    }

    public static void main(String[] args) {
        Thread rmiThread = new Thread(){
            @Override
            public void run(){
                try{
                    startRMI();
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

        try {
            rmiThread.join();
            socketThread.join();
        } catch (InterruptedException e) {
            System.err.println("No connection protocol available. Exiting...");
        }
    }

    public static void startRMI() throws RemoteException {
        AppServerImpl server = getInstance();

        Registry registry = LocateRegistry.getRegistry();
        registry.rebind("server", server);
    }

    public static void startSocket() throws RemoteException {
        AppServerImpl instance = getInstance();
        try(ServerSocket serverSocket = new ServerSocket(1234)){
            System.out.println("Server is starting...");
            while(true){
                System.out.println("Server ready!");
                Socket socket = serverSocket.accept();
                instance.executorService.submit(() ->{
                    try{
                        System.out.println("Server accept a new connection from "
                                + socket.getInetAddress() + " on port" + socket.getPort());
                        ClientSkeleton clientSkeleton = new ClientSkeleton(socket);
                        Server server = ServerImpl.getInstance();
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

    @Override
    public Server connect() throws RemoteException {
        return ServerImpl.getInstance();
    }
}
