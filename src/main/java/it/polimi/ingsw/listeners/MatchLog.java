package it.polimi.ingsw.listeners;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.modelview.GameView;

import java.rmi.RemoteException;
import java.util.List;

/**
 * The MatchLog contains various methods to keep track and update statistics about a running match
 * (connection/disconnections, choices made by the players, etc.) and display them on the server
 */
public class MatchLog implements Client {

    private final int matchID;
    private final String nickname;
    private int listenerID;
    private int turnCounter;

    /**
     * Constructor method
     * @param matchID int -> match's ID
     */
    public MatchLog(int matchID){
        this.turnCounter = 1;
        this.matchID = matchID;
        this.nickname = "MatchLog for match#" + this.matchID;
    }

    /**
     * Update method passing a GameView to display a match's info
     * @param game GameView
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public void update(GameView game) throws RemoteException {
        System.out.println("Log := match#" + matchID + ", turn n. " + turnCounter + " : " +
                "player " + game.getCurrentPlayer().getNickname() + " with clientID " +
                game.getCurrentPlayer().getClientID() + " is currently playing.");
        turnCounter++;
    }

    /**
     * Update method passing a message String to display a notification about one of the clients playing
     * @param msg String
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public void update(String msg) throws RemoteException {
        if(msg.equals("START GAME")) return;
        System.out.println("Log := match#" + matchID + " new clients string message notification : \n->" + msg);
    }

    /**
     * Update method passing a clientID to set a listenerID to a client
     * @param clientID int
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public void update(int clientID) throws RemoteException {
        this.listenerID = clientID;
    }

    /**
     * Update method passing a client and all his choices made to display those
     * @param client the client making the choices
     * @param coords the item's coordinates chosen
     * @param column the shelf's column choice
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    public void update(Client client, List<int[]> coords, Integer column) throws RemoteException {
        System.out.println("Log := match#" + matchID + ", user player " + client.getClientID() +
                " has taken and reorder from the gameboard the following items (in coords):");
        for(int[] arr : coords){
            System.out.print(arr[0] + "," + arr[1] + " ");
        }
        System.out.println("\nThose items will be insert in the " + (column + 1) + "th column");
    }

    /**
     * update method
     * @param notificationList the list of object contains the disconnection information
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public void update(List<Object> notificationList) throws RemoteException {
        System.out.println("Log := match#" + matchID + ", client disconnection event : \n->" +
                notificationList.get(1));
        System.out.println();
    }

    /**
     * Get method for a player's nickname String
     * @return nickname String
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public String getNickname() throws RemoteException {
        return this.nickname;
    }

    /**
     * Get method for a player's clientID
     * @return int -> clientID
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public int getClientID() throws RemoteException {
        return this.listenerID;
    }
}
