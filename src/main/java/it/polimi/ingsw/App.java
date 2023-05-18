package it.polimi.ingsw;

import it.polimi.ingsw.client.MyShelfieAppClient;
import it.polimi.ingsw.server.AppServerImpl;

import java.rmi.RemoteException;
import java.util.Scanner;

public class App {
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
            //case 3 -> new Thread(MyShelfieAppClient::startClient).start();
        }
        //new Thread(MyShelfieAppClient::startClient).start();
    }
}