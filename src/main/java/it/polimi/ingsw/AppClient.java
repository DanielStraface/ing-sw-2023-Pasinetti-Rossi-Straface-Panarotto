package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class AppClient {
    private static final int CLOSE_APP_FROM_MAIN_MENU = 1;
    static String nickname;
    public static List<Integer> welcome() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Hello, welcome to MyShelfie!\n1)Start a new game\n2)Join an existing game\n" +
                "3)Load a previous game\n4)Quit from MyShelfie\n>>");
        int decision = scanner.nextInt();
        while(decision < 1 || decision > 4){
            System.out.print("\nInvalid selection from above menu! Please try again\n>>");
            decision = scanner.nextInt();
        }
        askNickname();
        int numbOfPlayers = 0;
        switch(decision){
            case 1 -> numbOfPlayers = askNumOfPlayer();
            case 3 -> {
                displayResearch();
            }
            case 4 -> {
                System.out.print("\nGoodbye player! See you soon\nTermination of MyShelfie...");
                System.exit(CLOSE_APP_FROM_MAIN_MENU);
            }
            default -> {}
        }
        System.err.println("Decide CLI or GUI have not implemented yet");
        List<Integer> playersDecision = new ArrayList<Integer>();
        playersDecision.add(Integer.valueOf(decision));
        playersDecision.add(Integer.valueOf(numbOfPlayers));
        decision = 0;
        while(decision < 1 || decision > 2){
            System.out.print("Choose between RMI Network version or Socket: " + "\n1) RMI\n2) Socket\n>>");
            decision = scanner.nextInt();
        }
        playersDecision.add(Integer.valueOf(decision));
        return playersDecision;
    }

    private static void askNickname(){
        String input;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert your nickname\n>>");
        input = scanner.nextLine();
        while(input.contains("%") || input.contains("!") || input.contains("?") || input.contains("=") ||
                input.contains("(") || input.contains(")") || input.contains("'") ||
                input.contains("/") || input.contains("£") || input.contains("$") || input.contains("€")){
            System.out.print("\nThis chars are not allowed !£$%&/()=?' , please try again\n>>");
            input = scanner.nextLine();
        }
        nickname = input;
    }

    private static int askNumOfPlayer() {
        final String insertNumberOfPlayer = "Insert the number of players of the match: >>";
        Scanner scanner = new Scanner(System.in);
        int choice;
        System.out.print(insertNumberOfPlayer);
        choice = scanner.nextInt();
        while(choice < 2 || choice > 4){
            System.out.print("\nInvalid player selection, please try again (note that the game admits two," +
                    "three or four players)\n>>");
            choice = scanner.nextInt();
        }
        return choice;
    }

    private static void displayResearch() {
        System.out.print("Searching for a previous game...It may take a while");
    }
}
