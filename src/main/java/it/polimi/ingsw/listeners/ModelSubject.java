package it.polimi.ingsw.listeners;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;

import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

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
     * @throws RemoteException
     */
    public synchronized void informLog(Client o, List<int[]> coords, Integer column) throws RemoteException {
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
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
     * @throws RemoteException
     */
    public void notifyObservers(Game arg) throws RemoteException{
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        Object[] arrLocal;

        synchronized (this) {
            /* We don't want the Observer doing callbacks into
             * arbitrary code while holding its own Monitor.
             * The code where we extract each Observable from
             * the Vector and store the state of the Observer
             * needs synchronization, but notifying observers
             * does not (should not).  The worst result of any
             * potential race-condition here is that:
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */
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
                    /*if(e.getCause() != null && e.getCause() instanceof SocketException){
                        System.err.println("The user of the player " + nicknames[finalI] + " has disconnected");
                        new Thread(() -> {
                            for(int j=0;j<arrLocal.length;j++){
                                if(j != finalI){
                                    Client client = (Client) arrLocal[j];
                                    String disconnectionMsg = "The user of player " + nicknames[finalI]
                                            + " has disconnected! The game ends here...";
                                    try {
                                        client.update(disconnectionMsg);
                                    } catch (RemoteException ignored) {
                                    }
                                }
                            }
                        }).start();
                    } else System.err.println("Error while notify game in Server: " + e.getMessage());*/
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
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        Object[] arrLocal;

        synchronized (this) {
            /* We don't want the Observer doing callbacks into
             * arbitrary code while holding its own Monitor.
             * The code where we extract each Observable from
             * the Vector and store the state of the Observer
             * needs synchronization, but notifying observers
             * does not (should not).  The worst result of any
             * potential race-condition here is that:
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */
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

    public void notifyDisconnection(Game game, String disconnectedName, String msg) throws RemoteException{
        setChanged();
        /*
         * a temporary array buffer, used as a snapshot of the state of
         * current Observers.
         */
        Object[] arrLocal;

        synchronized (this) {
            /* We don't want the Observer doing callbacks into
             * arbitrary code while holding its own Monitor.
             * The code where we extract each Observable from
             * the Vector and store the state of the Observer
             * needs synchronization, but notifying observers
             * does not (should not).  The worst result of any
             * potential race-condition here is that:
             * 1) a newly-added Observer will miss a
             *   notification in progress
             * 2) a recently unregistered Observer will be
             *   wrongly notified when it doesn't care
             */
            if (!changed)
                return;
            arrLocal = obs.toArray();
            clearChanged();
        }
        GameView gmv = new GameView(game);
        String[] nicknames = new String[gmv.getPlayers().size()];
        gmv.getPlayers().stream().map(PlayerView::getNickname).toList().toArray(nicknames);
        for(int i=0;i<arrLocal.length - 1;i++){
            if(!nicknames[i].equals(disconnectedName)){
                int finalI = i;
                new Thread(() -> {
                    Client vl = (Client) arrLocal[finalI];
                    List<Object> notificationList = Arrays.asList(Client.QuitState.QUIT, msg);
                    try {
                        System.out.println("SETTIMO");
                        vl.update(notificationList);
                    } catch (RemoteException ignored) {
                    }
                }).start();
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