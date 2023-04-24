package it.polimi.ingsw;

import it.polimi.ingsw.client.AppClientImpl;

import java.rmi.RemoteException;

public class App
{
    public static void main( String[] args ) throws RemoteException {
        new Thread(()-> {
            AppClientImpl.startClient();
        }).start();
    }
}