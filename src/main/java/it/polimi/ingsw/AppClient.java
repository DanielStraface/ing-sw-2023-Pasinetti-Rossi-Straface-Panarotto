package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class AppClient {
    static String nickname;
    public static List<Integer> welcome() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello, welcome to MyShelfie!");
        System.out.print("1) Start a new game\n2) Join an existing game\n" +
                "3) Load a previous game\n4) Quit from MyShelfie\n>>");
        int decision = scanner.nextInt();
        askNickname();
        int numbOfPlayers = 0;
        switch(decision){
            case 1 -> numbOfPlayers = askNumOfPlayer();
            case 2 -> {}
            case 3 -> {
                displayResearch();
            }
            case 4 -> {
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
            System.out.print("Choose between RMI Network version or Socket:\n1) RMI\n2) Socket\n>>");
            decision = scanner.nextInt();
        }
        playersDecision.add(Integer.valueOf(decision));
        return playersDecision;
    }

    private static void displayResearch() {
        System.out.print("Searching for a previous game...It may take a while");
    }

    private static int askNumOfPlayer() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert the number of players of the match: >>");
        return scanner.nextInt();
    }

    private static void askNickname(){
        String input;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert your nickname\n>>");
        input = scanner.nextLine();
        while(input.contains("%") || input.contains("!") || input.contains("?") || input.contains("=") ||
                input.contains("(") || input.contains(")") || input.contains("'") ||
                input.contains("/") || input.contains("£") || input.contains("$") || input.contains("€")){
            System.out.print("\nThis chars are not allowed !£$%&/()=?' , please try again");
            input = scanner.nextLine();
        }
        nickname = input;
    }
}
