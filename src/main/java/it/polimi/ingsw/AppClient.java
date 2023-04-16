package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class AppClient {
    static String nickname;
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
            case 1, 2 -> numbOfPlayers = askNumOfPlayer();
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
        decision = 0;
        while(decision < 1 || decision > 2){
            System.out.println("Choose between RMI Network version or Socket:\n1) RMI\n2) Socket");
            decision = scanner.nextInt();
        }
        playersDecision.add(Integer.valueOf(decision));
        return playersDecision;
    }

    private static int askNumOfPlayer() {
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
