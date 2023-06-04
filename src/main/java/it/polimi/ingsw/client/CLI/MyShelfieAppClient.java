package it.polimi.ingsw.client.CLI;

import it.polimi.ingsw.server.AppServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyShelfieAppClient {
    private static final Scanner scanner = new Scanner(System.in);
    private static final int RMI_NETWORK = 1;
    private static final int SOCKET_NETWORK = 2;
    private static final int RMI_PORT = 1099;
    private static final int SOCKET_PORT = 1234;
    private static final int QUIT_IN_MYSHELFIE_APP_CLIENT_ERROR = -1;

    /**
     * Makes the client connect to RMI or Socket based on the choice taken
     */
    public static void startClient() {
        List<Object> connectionDecisions = welcome();
        Object[] mainArgs = {"CLI", connectionDecisions.get(1), connectionDecisions.get(2)};
        switch ((Integer) connectionDecisions.get(0)){
            case RMI_NETWORK -> {
                try{
                    AppClientRMI.launchClient(mainArgs);
                } catch (RemoteException e) {
                    System.err.println("Something went wrong with network connections: " + e.getMessage());
                    terminationError();
                } catch (NotBoundException e) {
                    System.err.println("Something went wrong with registry connections: " + e.getMessage());
                    terminationError();
                }
            }
            case SOCKET_NETWORK -> {
                try {
                    AppClientSocket.launchClient(mainArgs);
                } catch (RemoteException e) {
                    System.err.println("Something went wrong with network connections: " + e.getMessage());
                    terminationError();
                }
            }
            default -> {terminationError();}
        }
    }

    /**
     * Prints a "welcome" message, makes the client choose between RMI or socket connection and type the IP address and
     * saves all the choices made
     * @return an Object List with all the choices made
     */
    private static List<Object> welcome() {
        List<Object> selections = new ArrayList<>();
        int in;
        String temp;
        System.out.print("""
                Hello, welcome to MyShelfie!
                Please, choose between RMI or Socket network technology:
                1)RMI
                2)Socket
                >>""");
        in = scanner.nextInt();
        scanner.nextLine();
        while(in < 1 || in > 2){
            System.out.print("\nInvalid input, please choose again correctly: >>");
            in = scanner.nextInt();
        }
        selections.add(in);
        if(in == 1) in = RMI_PORT;
        if(in == 2) in = SOCKET_PORT;
        System.out.print("\nInsert the server ip address: >>");
        temp = scanner.nextLine();
        while(!AppClient.checkIp(temp)){
            System.out.print("\nInvalid ip address, try again: >>");
            temp = scanner.nextLine();
        }
        selections.add(temp);
        selections.add(in);
        return selections;
    }

    /**
     * Prints an error message in case a connection has gone wrong
     */
    private static void terminationError(){
        System.err.println("Something went wrong! Closing...\nTERMINATED");
        System.exit(QUIT_IN_MYSHELFIE_APP_CLIENT_ERROR);
    }
}
