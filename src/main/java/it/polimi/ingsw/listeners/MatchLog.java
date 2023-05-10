package it.polimi.ingsw.listeners;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.modelview.GameView;

import java.rmi.RemoteException;
import java.util.List;

public class MatchLog implements Client {

    private final int matchID;
    private int turnCounter;

    public MatchLog(int matchID){
        this.turnCounter = 1;
        this.matchID = matchID;
    }
    @Override
    public void update(GameView game) throws RemoteException {
        System.out.println("Log := match#" + matchID + ", turn n. " + turnCounter + " : " +
                "player " + game.getCurrentPlayer().getNickname() + " with clientID " +
                game.getCurrentPlayer().getClientID() + " is currently playing.");
        turnCounter++;
    }

    @Override
    public void update(String msg) throws RemoteException {
        if(msg.equals("START GAME")) return;
        System.out.println("Log := match#" + matchID + " new clients string message notification : \n->" + msg);
    }

    @Override
    public void update(int clientID) throws RemoteException {

    }

    @Override
    public String getNickname() throws RemoteException {
        return null;
    }

    @Override
    public int getClientID() throws RemoteException {
        return 5;
    }

    public void update(Client client, List<int[]> coords, Integer column) throws RemoteException {
        System.out.println("Log := match#" + matchID + ", user player " + client.getClientID() +
                " has taken and reorder from the gameboard the following items (in coords):");
        for(int[] arr : coords){
            System.out.print(arr[0] + "," + arr[1] + " ");
        }
        System.out.println("\nThose items will be insert in the " + (column + 1) + "th column");
    }
}
