package it.polimi.ingsw.listeners;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.server.AppServerImpl;

import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 * The AppClientRMI class represents a specific type of AppClient class used for the RMI connection type.
 * It contains a heartbeat method to monitor the client activity connection status.
 * See AppClient class documentation for more information.
 */
public class ModelSubject {
    private boolean changed = false;
    private Vector<Client> obs;

    /**
     * Constructor method
     */
    public ModelSubject(){
        this.obs = new Vector<>();
    }

    /**
     * Method to add a client as a listener
     * @param o client to be added as listener
     */
    public synchronized void addListener(Client o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    /**
     * Method to delete all listeners (clients) from its Vector
     */
    public synchronized void deleteListeners(){obs = new Vector<>();}

    /**
     * Updates the MatchLog with the client and all the choices made
     * @param o the client making the choices
     * @param coords the item's coordinates chosen
     * @param column the shelf's column choice
     * @throws RemoteException if the execution of update method call goes wrong
     */
    public synchronized void informLog(Client o, List<int[]> coords, Integer column) throws RemoteException {
        Object[] arrLocal;
        synchronized (this) {arrLocal = obs.toArray();}

        for (int i = arrLocal.length-1; i>=0; i--){
            if(arrLocal[i] instanceof MatchLog)
                ((MatchLog) arrLocal[i]).update(o, coords, column);
        }
    }

    /**
     * Notifies the Game class of most changes made in a match
     * @param arg - the game model
     */
    public void notifyObservers(Game arg) {
        Object[] arrLocal;

        synchronized(this){
            if (!changed)
                return;
            arrLocal = obs.toArray();
            clearChanged();
        }
        GameView gmv = new GameView(arg);
        String[] nicknames = new String[gmv.getPlayers().size()];
        gmv.getPlayers().stream().map(PlayerView::getNickname).toList().toArray(nicknames);
        for(int i=0;i<arrLocal.length - 1;i++){
            int finalI = i;
            new Thread(() -> {
                Client vl = (Client) arrLocal[finalI];
                try{
                    if(finalI <= arrLocal.length - 2){
                        vl.update(gmv);
                    }
                } catch (RemoteException e) {
                    if(e.getCause() != null && e.getCause() instanceof SocketException){
                        System.out.println("THE USER OF THE PLAYER " + nicknames[finalI] + " HAS DISCONNECTED");
                        new Thread(() -> {
                            AppServerImpl appServer;
                            try {
                                appServer = AppServerImpl.getInstance();
                                for(int j=0;j<arrLocal.length;j++){
                                    if(j != finalI){
                                        Client client = (Client) arrLocal[j];
                                        String disconnectionMsg = "The user of player " + nicknames[finalI]
                                                + " has disconnected! The game ends here...";
                                        try {
                                            if(client.getClientID() != 5)
                                                appServer.removeLoggedUser(((Client) arrLocal[j]).getNickname());
                                            client.update(disconnectionMsg);
                                        } catch (RemoteException ignored) {
                                        }
                                    }
                                }
                                appServer.removeLoggedUser(nicknames[finalI]);
                            } catch (RemoteException ex) {
                                System.err.println("Cannot reached the appServer: " + e.getMessage());
                            }
                        }).start();
                    } else System.err.println("Error while notify game in Server: " + e.getMessage());
                }
                }).start();
        }
    }

    /**
     * Notifies a Common Objective Card completion by a player
     * @param player the player that completed a CommonObjCard
     * @param arg the completion message String
     */
    public void notifyObservers(Player player, String arg) {
        Object[] arrLocal;

        synchronized(this){
            if (!changed)
                return;
            arrLocal = obs.toArray();
            clearChanged();
        }

        new Thread(() -> {
            for (int i = arrLocal.length-1; i>=0; i--){
                Client vl = (Client) arrLocal[i];
                try {
                    if(vl.getClientID() == 5)
                        vl.update("New info from " + player.getNickname() + ": " + arg);
                    else vl.update(arg);
                } catch (RemoteException e) {
                    System.err.println("Cannot obtain the clientID to notify: " + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * notifies about disconnection
     * @param game Game
     * @param disconnectedName type od disconnection
     * @param msg message to be  included in the notification
     * @throws RemoteException if the execution of getClientID method call goes wrong
     */
    public void notifyDisconnection(Game game, String disconnectedName, String msg) throws RemoteException{
        setChanged();
        Client[] arrLocal = new Client[obs.size()];

        synchronized(this){
            if (!changed)
                return;
            obs.toArray(arrLocal);
            clearChanged();
        }

        if(disconnectedName.equals(":Empty Bag")){
            for(int i=arrLocal.length-1;i>=0;i--){
                int finalI = i;
                new Thread(() -> {
                    Client vl = arrLocal[finalI];
                    List<Object> notificationList = Arrays.asList(Client.QuitState.EMPTY_BAG, msg);
                    try {
                        if(vl.getClientID() != 5) vl.update(notificationList);
                        else System.out.println(msg);
                    } catch (RemoteException e) {
                        System.err.println("Cannot notify the emptyBag in ModelSubject: " + e.getMessage());
                    }
                }).start();
            }
        }
        String[] nicknames = new String[game.getPlayers().size()];
        game.getPlayers().stream().map(Player::getNickname).toList().toArray(nicknames);
        int upperBounder;
        List<Integer> clientIDTempList = Arrays.stream(arrLocal).map(elem -> {
            try {
                return elem.getClientID();
            } catch (RemoteException ignored) {
            }
            return null;
        }).toList();
        if(clientIDTempList.contains(5)) upperBounder = arrLocal.length - 1;
        else upperBounder = arrLocal.length;
        for(int i=0;i<upperBounder;i++){
            if(!nicknames[i].equals(disconnectedName)){
                Client vl = arrLocal[i];
                if(vl.getClientID() != 5){
                    List<Object> notificationList = Arrays.asList(Client.QuitState.QUIT, msg);
                    new Thread(() -> {
                        try {
                            vl.update(notificationList);
                        } catch (RemoteException ignored) {
                        }
                    }).start();
                }
            }
        }
    }

    /**
     * Sets the "changed" flag to true
     */
    protected synchronized void setChanged() {
        changed = true;
    }

    /**
     * Sets the "changed" flag to false
     */
    protected synchronized void clearChanged() {
        changed = false;
    }
}