package it.polimi.ingsw;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AppClient {
    private static String nickname;
    public static void main(String[] args){
        List<Integer> decisions = welcome();
        String[] elements = new String[3];
        elements[elements.length - 1] = AppClient.nickname;
        elements[0] = decisions.get(0).toString();
        elements[1] = decisions.get(1).toString();
        switch(decisions.get(decisions.size() - 1)){
            case 1 -> {
                try {
                    AppClientRMI.main(elements);
                } catch (RemoteException e) {
                    System.err.println("Something went wrong with network connections: " + e.getMessage());
                } catch (NotBoundException e) {
                    System.err.println("Something went wrong with String[] args parameters: " + e.getMessage());
                }
            }
            case 2 -> {
                try {
                    AppClientSocket.main(elements);
                } catch (RemoteException e) {
                    System.err.println("Something went wrong with network connections: " + e.getMessage());
                }
            }
            default -> {
                System.out.println("Something went wrong! Closing...");
                System.err.println("TERMINATED");
                System.exit(1);
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
        askNickname();
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
        List<Integer> playersDecision = new ArrayList<Integer>();
        playersDecision.add(Integer.valueOf(decision));
        playersDecision.add(Integer.valueOf(numbOfPlayers));
        decision = 0;
        while(decision < 1 || decision > 2){
            System.out.println("Choose between RMI Network version or Socket:\n1) RMI\n2) Socket");
            decision = scanner.nextInt();
        }
        playersDecision.add(Integer.valueOf(decision));
        System.err.println("Decide CLI or GUI have not implemented yet");
        return playersDecision;
    }

    private static int joinNewGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the number of players of the match: ");
        return scanner.nextInt();
    }

    private static int startNewGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert the number of players of the match: ");
        return scanner.nextInt();
    }

    private static void askNickname(){
        String input = null;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insert your nickname");
        while(input == null){
            input = scanner.nextLine();
        }
        nickname = input;
    }
}
