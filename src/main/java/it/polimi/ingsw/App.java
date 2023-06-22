package it.polimi.ingsw;

import it.polimi.ingsw.client.CLI.MyShelfieAppClient;
import it.polimi.ingsw.server.AppServerImpl;
import it.polimi.ingsw.client.GUI.GUI;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * App class is the MyShelfie main class that allow the user to start the game as client (via GUI or CLI) or as server
 * See AppServer and MyShelfieAppClient documentations for more information.
 */
public class App {

    /**
     * Method main opens a server,CLI or GUI based on input
     * @param args args of type String[]
     * @throws RemoteException if the execution of a remote method goes wrong
     */
    public static void main( String[] args ) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Welcome to MyShelfie!\nWhat do you want to launch?\n1)Server\n2)CLI\n3)GUI\n>>");
        int decision = scanner.nextInt();
        while(decision < 1 || decision > 3){
            System.out.print("\nWrong selection, try again: >>");
            decision = scanner.nextInt();
        }
        switch (decision){
            case 1 -> AppServerImpl.main(null);
            case 2 -> MyShelfieAppClient.startClient();
            case 3 -> GUI.main(null);
        }
    }
}