package it.polimi.ingsw;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.local.ClientImpl;
import it.polimi.ingsw.distributed.local.ServerImpl;

public class App
{
    public static void main( String[] args ) {
        Server server = new ServerImpl();
        ClientImpl client1 = new ClientImpl(server);
        //ClientImpl client2 = new ClientImpl(server);

        client1.run();
    }
}