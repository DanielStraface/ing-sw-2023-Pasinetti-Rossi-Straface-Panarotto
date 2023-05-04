package it.polimi.ingsw.listeners;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.ShelfView;

import java.rmi.RemoteException;
import java.util.Vector;

public class ModelSubject {
    private boolean changed = false;
    private Vector<Client> obs;
    private Vector<Client> listeners;
    private boolean isTheFirstTurn;

    public ModelSubject(){
        this.obs = new Vector<>();
        this.listeners = new Vector<>();
        this.isTheFirstTurn = true;
    }

    public synchronized void addListener(Client o) {
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    public synchronized void addListenerForPlayer(Client o) {
        if (o == null)
            throw new NullPointerException();
        if (!listeners.contains(o)) {
            listeners.addElement(o);
        }
    }

    public void notifyObservers(GameBoard arg) throws RemoteException {
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

        for (int i = arrLocal.length-1; i>=0; i--){
            Client vl = (Client)arrLocal[i];
            vl.update(new GameBoardView(arg.getGameGrid()));
        }
    }

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

        Player currentP = arg.getCurrentPlayer();
        for (int i = arrLocal.length-1; i>=0; i--){
            Client vl = (Client) arrLocal[i];
            if(vl.getClientID() == currentP.getClientID()){
                vl.update(new GameView(arg), arg.getCurrentPlayer().getClientID());
            } else if(isTheFirstTurn) {
                vl.update(new GameBoardView(arg.getGameboard().getGameGrid()));
                isTheFirstTurn = false;
            }
        }
    }

    public void notifyObservers(Item[][] arg) throws RemoteException{
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
            arrLocal = listeners.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length-1; i>=0; i--){
            Client vl = (Client) arrLocal[i];
            vl.update(arg);
        }
    }

    public void notifyObservers(Shelf arg) throws RemoteException{
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
            arrLocal = listeners.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length-1; i>=0; i--){
            Client vl = (Client) arrLocal[i];
            vl.update(new ShelfView(arg.getShelfGrid(), arg.getLastRow()));
        }
    }

    public void notifyObservers(String arg) {
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
            arrLocal = listeners.toArray();
            clearChanged();
        }

        for (int i = arrLocal.length-1; i>=0; i--){
            Client vl = (Client) arrLocal[i];
            try {
                vl.update(arg);
            } catch (RemoteException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    protected synchronized void setChanged() {
        changed = true;
    }

    protected synchronized void clearChanged() {
        changed = false;
    }
}