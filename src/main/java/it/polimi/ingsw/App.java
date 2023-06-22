package it.polimi.ingsw;

import it.polimi.ingsw.client.CLI.MyShelfieAppClient;
import it.polimi.ingsw.server.AppServerImpl;
import it.polimi.ingsw.client.GUI.GUI;

import java.io.IOException;
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
        try{
            String[] cmds = new String[1];
            if(System.getProperty("os.name").contains("Windows")){
                cmds[0] = "cls";
                Runtime.getRuntime().exec(cmds);
            }
            else{
                cmds[0] = "clear";
                Runtime.getRuntime().exec(cmds);
            }
        } catch (IOException e) {
            System.err.println("Cannot clear the terminal screen");
        }
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