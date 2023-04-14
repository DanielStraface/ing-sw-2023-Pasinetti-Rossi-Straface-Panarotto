package it.polimi.ingsw;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.rmi.ClientImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppClientRMI {
    private static final int TYPE_OF_MATCH = 0;
    private static final int PLAYERS_NUMBER = 1;
    private static final int NICKNAME_PLACEHOLDER = 2;
    public static void main(String[] args) throws RemoteException, NotBoundException {
        List<String> decisions = new ArrayList<>();
        decisions.add(args[TYPE_OF_MATCH]);
        decisions.add(args[PLAYERS_NUMBER]);
        decisions.add(args[NICKNAME_PLACEHOLDER]);
        Registry registry = LocateRegistry.getRegistry();
        Server server = (Server) registry.lookup("server");
        switch (decisions.get(TYPE_OF_MATCH)){
            case "1" -> {
                int numOfPlayers = Integer.parseInt(decisions.get(PLAYERS_NUMBER));
                System.out.println("num: " + numOfPlayers + ", decs: " + decisions.get(NICKNAME_PLACEHOLDER));
                ClientImpl client = new ClientImpl(server, numOfPlayers , decisions.get(NICKNAME_PLACEHOLDER));
                //client.run();
            }
            case "2" -> {
                ClientImpl client = new ClientImpl(server, decisions.get(NICKNAME_PLACEHOLDER));
                //client.run();
            }
            default -> {
                System.exit(20);
            }
        }
    }
}