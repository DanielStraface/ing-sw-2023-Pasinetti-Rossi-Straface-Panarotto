package it.polimi.ingsw;

import it.polimi.ingsw.client.CLI.MyShelfieAppClient;
import it.polimi.ingsw.server.AppServerImpl;
import it.polimi.ingsw.client.GUI.GUI;

import java.util.Scanner;

public class Prova1 {
    public static void main(String[] args){
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
        //new Thread(MyShelfieAppClient::startClient).start();
    }
}
