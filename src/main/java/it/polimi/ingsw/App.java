package it.polimi.ingsw;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class App
{
    public static void main( String[] args ) throws RemoteException {
        new Thread(()-> {
            try {
                AppClientRMI.main(null);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}