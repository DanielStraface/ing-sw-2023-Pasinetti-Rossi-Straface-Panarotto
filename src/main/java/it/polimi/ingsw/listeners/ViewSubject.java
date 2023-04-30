package it.polimi.ingsw.listeners;


import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.exceptions.InvalidSelectionException;
import it.polimi.ingsw.view.TextualUI;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

public class ViewSubject {
    private boolean changed = false;
    private Vector<Server> obs;

    public ViewSubject(){
        obs = new Vector<>();
    }

    public synchronized void addListener(Server o){
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    public void notifyObservers(Client o, Integer arg) throws RemoteException {
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
            Server vl = (Server) arrLocal[i];
            vl.update((Client) o, arg);
        }
    }

    public void notifyObservers(Client o, List<int[]> arg) throws RemoteException, InvalidSelectionException {
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
            Server vl = (Server)arrLocal[i];
            try{
                vl.update(o, arg);
            } catch (RemoteException e) {
                if(e.getMessage().contains("Try again")){
                    String msg = e.getMessage().substring(92);
                    throw new InvalidSelectionException(msg);
                } else {
                    throw new RemoteException(e.getMessage());
                }
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
