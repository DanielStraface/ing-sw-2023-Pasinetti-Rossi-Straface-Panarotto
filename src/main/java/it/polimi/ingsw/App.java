package it.polimi.ingsw;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class App
{
    public static void main( String[] args ) throws RemoteException {
        new Thread(()-> {
            AppClient.main(null);
        }).start();
    }
}