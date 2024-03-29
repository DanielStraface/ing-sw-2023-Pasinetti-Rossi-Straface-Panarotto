package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.server.AppServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * The AppClient class represents the player as a user in a client/server application pattern.
 * It contains everything necessary in order to play to this game such as notification to the match server,
 * UX/UI methods reference, view management ect...
 */
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

    /**
     * The UIType represents the type of UI implemented in MyShelfie:
     *      CLI - Command Line Interface
     *      GUI - Graphical User Interface
     */
    public enum UIType {CLI, GUI}

    /**
     * method for asking the player's match setup choices
     * @return the list with all the choices made
     */
    protected static List<Integer> mainMenu(){
        System.out.print("\n\nThis is MyShelfie main menu, please choose from this menu list:\n" +
                "1)Create/Continue a match\n2)Join an existing match\n3)Quit from MyShelfie\n>>");
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
        List<Integer> playersDecision = new ArrayList<>();
        playersDecision.add(decision);
        playersDecision.add(numbOfPlayers);
        return playersDecision;
    }

    /**
     * method that check if the given string is in the form x.x.x.x with x in [0, 255] inclusive.
     * @param ip - the string to be checked
     * @return true iff String ip -> is an ip string type
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
     * method to ask how many players are/will be in the type of match selected
     * @return the number of players
     */
    private static int askNumOfPlayer() {
        final String insertNumberOfPlayer = "Insert the number of players of the match: >>";
        int choice;
        System.out.print(insertNumberOfPlayer);
        choice = scanner.nextInt();
        while(choice < 2 || choice > 4){
            System.out.print("""

                    Invalid player selection, please try again (note that the game admits two,three or four players)
                    >>""");
            choice = scanner.nextInt();
        }
        return choice;
    }

    /**
     * method that checks if the nickname is already taken, if not so it registers the client to the server
     * @param uiType the type of the chosen UI
     * @param appS the match Server App
     * @param stub the corresponding ServerStub
     * @throws RemoteException if the execution of log method call goes wrong
     * @return true iff the user nickname is accepted
     */
    protected static boolean logginToAppServer(AppClient.UIType uiType, AppServer appS, ServerStub stub)
            throws RemoteException {
        if(uiType == UIType.CLI) nickname = TextualUI.askNickname();
        if(appS != null){
            return appS.log(nickname, true);
        } else {
            return stub.log(nickname);
        }
    }
}
