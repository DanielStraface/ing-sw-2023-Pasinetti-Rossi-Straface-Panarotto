package it.polimi.ingsw.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class AppClientImpl extends AppClient{
    private static final Scanner scanner = new Scanner(System.in);
    private static final int RMI_NETWORK = 1;
    private static final int SOCKET_NETWORK = 2;
    private static final int QUIT_IN_APPCLIENTIMPL_ERROR = 2;
    public static void startClient() {
        int connectionDecision = welcome();
        switch (connectionDecision){
            case RMI_NETWORK -> {
                try{
                    AppClientRMI.main(null);
                } catch (RemoteException | NotBoundException e) {
                    System.err.println("Something went wrong with network connections: " + e.getMessage());
                    terminationError();
                }
            }
            case SOCKET_NETWORK -> {
                try {
                    AppClientSocket.main(null);
                } catch (RemoteException e) {
                    System.err.println("Something went wrong with network connections: " + e.getMessage());
                    terminationError();
                }
            }
            default -> {terminationError();}
        }
    }

    private static int welcome() {
        int in;
        System.out.print("Hello, welcome to MyShelfie!\nPlease, choose between RMI or Socket network technology:" +
                "\n1)RMI\n2)Socket\n>>");
        in = scanner.nextInt();
        while(in < 1 || in > 2){
            System.out.print("\nInvalid input, please choose again correctly: >>");
            in = scanner.nextInt();
        }
        return in;
    }

    private static void terminationError(){
        System.err.println("Something went wrong! Closing...\nTERMINATED");
        System.exit(QUIT_IN_APPCLIENTIMPL_ERROR);
    }
}
