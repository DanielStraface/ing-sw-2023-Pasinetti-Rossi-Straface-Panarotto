package it.polimi.ingsw;

import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ServerImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class App
{
    public static void main( String[] args ) throws RemoteException {
        /*Server server = new ServerImpl();

        ClientImpl client = new ClientImpl(server, 2, "ThisName");*/
        /*new Thread(){
            @Override
            public void run(){
                String[] args = new String[1];
                args[0] = "0";
                try {
                    AppClientSocket.main(args);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();*/
        new Thread(()-> {
            AppClientImpl.startClient();
        }).start();
    }
}