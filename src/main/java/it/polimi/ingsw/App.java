package it.polimi.ingsw;

import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ServerImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class App
{
    public static void main( String[] args ) throws RemoteException {
        new Thread(()-> {
            AppClientImpl.startClient();
        }).start();
    }
}