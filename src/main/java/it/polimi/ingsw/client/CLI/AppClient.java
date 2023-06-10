package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.server.AppServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public abstract class AppClient {
    protected static final int TYPE_OF_MATCH_POSITION = 0;
    protected static final int CREATE_A_NEW_MATCH = 1;
    protected static final int JOIN_EXISTING_MATCH = 2;
    protected static final int NUMBER_OF_PLAYER_POSITION = 1;
    private static final int CLOSE_APP_FROM_MAIN_MENU = 1;
    protected static final int NO_MATCH_IN_WAITING_NOW_ERROR = -2;
    protected static final int QUIT_IN_APPCLIENTRMI_ERROR = -3;
    protected static final int QUIT_IN_APPLCLIENTSOCKET_ERROR = -4;;
    protected static String nickname;
    private static final Scanner scanner = new Scanner(System.in);
    public enum UIType {CLI, GUI}

    /**
     * method for asking the player's match setup choices
     * @return the list with all the choices made
     */
    protected static List<Integer> mainMenu(){
        System.out.print("\n\nThis is MyShelfie main menu, please choose from this menu list:\n" +
                "1)Start a new game\n2)Join an existing game\n3)Quit from MyShelfie\n>>");
        int decision = scanner.nextInt();
        while(decision < 1 || decision > 4){
            System.out.print("\nInvalid selection from above menu! Please try again\n>>");
            decision = scanner.nextInt();
        }
        int numbOfPlayers = 0;
        switch(decision){
            case 1 -> numbOfPlayers = askNumOfPlayer();
            case 3 -> {
                System.out.print("\nGoodbye player! See you soon\nTermination of MyShelfie...");
                System.exit(CLOSE_APP_FROM_MAIN_MENU);
            }
            default -> {}
        }
        //int guiChoice = askForGUI();
        List<Integer> playersDecision = new ArrayList<>();
        playersDecision.add(decision);
        playersDecision.add(numbOfPlayers);
        //playersDecision.add(guiChoice);
        return playersDecision;
    }

    /**
     * method that check if the given string is in the form x.x.x.x with x in [0, 255] inclusive.
     * @param ip - the string to be checked
     * @return true <==> String ip -> is an ip string type
     */
    public static boolean checkIp(String ip) {
        if(ip.endsWith(".")) return false;
        String[] numbers = ip.split("\\.");
        for(String s : numbers){
            for(char c : s.toCharArray()){
                if(c < '0' || c > '9')
                    return false;
            }
        }
        return numbers.length == 4 &&
                Arrays.stream(numbers)
                .map(Integer::parseInt)
                .filter(n -> n >= 0 && n <= 255)
                .count() == 4;
    }

    /**
     * method to ask the player to choose between TUI or GUI
     * @return the choice made
     */
    private static int askForGUI() {
        int temp;
        System.out.print("\nSelect the type of UI:\n1)CLI\n2)GUI\n>>");
        temp = scanner.nextInt();
        while(temp < 1 || temp > 2){
            System.out.print("\nInvalid UI selection, please try again: >>");
            temp = scanner.nextInt();
        }
        return temp;
    }

    /**
     * method to ask the player to choose a nickname with no special characters
     */
    protected static void askNickname(){
        String input;
        System.out.print("\nInsert your nickname >>");
        input = scanner.nextLine();
        while(input.contains("%") || input.contains("!") || input.contains("?") || input.contains("=") ||
                input.contains("(") || input.contains(")") || input.contains("'") ||
                input.contains("/") || input.contains("£") || input.contains("$") || input.contains("€")){
            System.out.print("\nThis chars are not allowed !£$%&/()=?' , please try again\n>>");
            input = scanner.nextLine();
        }
        nickname = input;
    }

    /**
     * method to ask how many players are/will be in the type of match selected
     * @return the number of players
     */
    private static int askNumOfPlayer() {
        final String insertNumberOfPlayer = "Insert the number of players of the match: >>";
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

    /**
     * method that checks if the nickname is already taken, if not so it registers the client to the server
     * @param appS the match Server App
     * @param stub the corresponding ServerStub
     * @throws RemoteException if the server is unreachable
     */
    protected static boolean logginToAppServer(AppClient.UIType uiType, AppServer appS, ServerStub stub)
            throws RemoteException {
        if(uiType == UIType.CLI) nickname = TextualUI.askNickname();
        if(appS != null){
            return appS.log(nickname);
            //else System.out.print("\nThis nickname is already used by another user, you must choose another one.");
        } else {
            return stub.log(nickname);
            //else System.out.print("\nThis nickname is already used by another user, you must choose another one.");
        }
        /*while(true){
            if(uiType == AppClient.UIType.CLI) nickname = TextualUI.askNickname();
            if(appS != null){
                if(appS.log(nickname)) break;
                else System.out.print("\nThis nickname is already used by another user, you must choose another one.");
            } else {
                if(stub.log(nickname)) break;
                else System.out.print("\nThis nickname is already used by another user, you must choose another one.");
            }
        }
        System.out.print("Log successfully completed!");*/
    }

    /*public static void readyToLog(String nickname, String typeOfMatch, String numOfPlayers){
        logginToAppServer();
        while(true){
            if(!fromGUI) askNickname();
            if(appS != null){
                if(appS.log(nickname)) break;
                else System.out.print("\nThis nickname is already used by another user, you must choose another one.");
            } else {
                if(stub.log(nickname)) break;
                else System.out.print("\nThis nickname is already used by another user, you must choose another one.");
            }
        }
        System.out.print("Log successfully completed!");
    }*/

}
