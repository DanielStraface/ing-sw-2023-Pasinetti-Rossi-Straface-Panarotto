package it.polimi.ingsw;

import it.polimi.ingsw.client.MyShelfieAppClient;

public class Prova1 {
    public static void main(String[] args){
        new Thread(MyShelfieAppClient::startClient).start();
    }
}
