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
    public static void main(String[] args) throws RemoteException, NotBoundException {
        List<Integer> decision = welcome();
        Registry registry = LocateRegistry.getRegistry();
        Server server = (Server) registry.lookup("server");
        switch (decision.get(0)){
            case 1 -> {
                ClientImpl client = new ClientImpl(server, decision.get(1));
                client.run();
            }
            case 2 -> {
                ClientImpl client = new ClientImpl(server);
                client.run();
            }
            default -> {
                System.exit(20);
            }
        }
    }

    public static List<Integer> welcome() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello, welcome to MyShelfie!");
        System.out.println("1) Start a new game");
        System.out.println("2) Join an existing game");
        System.out.println("3) Quit from MyShelfie");
        int decision = scanner.nextInt();
        int numbOfPlayers = 0;
        switch(decision){
            case 1 -> numbOfPlayers = startNewGame();
            case 2 -> numbOfPlayers = joinNewGame();
            case 3 -> {
                System.out.println("Goodbye player! See you soon");
                System.err.println("Termination of MyShelie");
                System.exit(1);
            }
            default -> {
                System.err.println("Wrong selection, app termination...");
                System.exit(2);
            }
        }
        System.err.println("Decide CLI or GUI have not implemented yet");
        List<Integer> playersDecision = new ArrayList<Integer>();
        playersDecision.add(Integer.valueOf(decision));
        playersDecision.add(Integer.valueOf(numbOfPlayers));
        return playersDecision;
    }

    private static int joinNewGame() {
        return 0;
    }

    private static int startNewGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the number of players of the match: ");
        return scanner.nextInt();
    }
}