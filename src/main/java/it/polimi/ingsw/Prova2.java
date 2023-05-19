package it.polimi.ingsw;

import it.polimi.ingsw.client.CLI.MyShelfieAppClient;

public class Prova2 {
    public static void main(String[] args) {new Thread(MyShelfieAppClient::startClient).start();}
}
