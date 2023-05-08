package it.polimi.ingsw;

import it.polimi.ingsw.client.MyShelfieAppClient;

import java.rmi.RemoteException;

public class App
{
    public static void main( String[] args ) throws RemoteException {
        new Thread(MyShelfieAppClient::startClient).start();
    }
}