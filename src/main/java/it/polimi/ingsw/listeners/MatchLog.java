package it.polimi.ingsw.listeners;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.modelview.GameView;

import java.rmi.RemoteException;
import java.util.List;

public class MatchLog implements Client {

    private final int matchID;
    private final String nickname;
    private int listenerID;
    private int turnCounter;

    public MatchLog(int matchID){
        this.turnCounter = 1;
        this.matchID = matchID;
        this.nickname = "MatchLog for match#" + this.matchID;
    }
    @Override
    public void update(GameView game) throws RemoteException {
        System.out.print("\nLog := match#" + matchID + ", turn n. " + turnCounter + " : " +
                "player " + game.getCurrentPlayer().getNickname() + " with clientID ");
        if(game.getExceptionToDisplay() != null){
            System.out.print(" got an error: " + game.getExceptionToDisplay().getMessage() +
                    "\nThe turn must be repeated");
            return;
        } else System.out.print(game.getCurrentPlayer().getClientID() + " is currently playing.");
        turnCounter++;
    }

    @Override
    public void update(String msg) throws RemoteException {
        if(msg.equals("START GAME")) return;
        System.out.println("Log := match#" + matchID + " new clients string message notification : \n->" + msg);
    }

    @Override
    public void update(int clientID) throws RemoteException {
        this.listenerID = clientID;
    }

    @Override
    public String getNickname() throws RemoteException {
        return this.nickname;
    }

    @Override
    public int getClientID() throws RemoteException {
        return this.listenerID;
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
