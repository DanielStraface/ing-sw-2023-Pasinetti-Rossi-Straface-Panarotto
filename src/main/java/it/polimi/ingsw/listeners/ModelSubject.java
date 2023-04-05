package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.view.TextualUI;

import java.util.Vector;

public class ModelSubject {
    private boolean changed = false;
    private Vector<GameViewListener> obs;
    private Vector<PlayerViewListener> listeners;

    public ModelSubject(){
        this.obs = new Vector<>();
        this.listeners = new Vector<>();
    }
    public synchronized void addListener(GameViewListener o){
        if (o == null)
            throw new NullPointerException();
        if (!obs.contains(o)) {
            obs.addElement(o);
        }
    }

    public synchronized void addListener(PlayerViewListener o){
        if (o == null)
            throw new NullPointerException();
        if (!listeners.contains(o)) {
            listeners.addElement(o);
        }
    }

    public synchronized void deleteListener(GameViewListener o) {
        obs.removeElement(o);
    }
    public synchronized void deleteListener(PlayerViewListener o) {
        listeners.removeElement(o);
    }

    /*public void notifyObservers() {
        notifyObservers(null);
    }*/

    public void notifyObservers(GameBoard arg) {
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
            GameViewListener vl = (GameViewListener)arrLocal[i];
            vl.update((Game) this, arg);
        }
    }

    public void notifyObservers(Game arg) {
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
            GameViewListener vl = (GameViewListener)arrLocal[i];
            vl.update(arg);
        }
    }

    public void notifyObservers(Item[][] arg) {
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
            PlayerViewListener vl = (PlayerViewListener) arrLocal[i];
            vl.update((Player) this, arg);
        }
    }

    public void notifyObservers(Integer arg) {
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
            GameViewListener vl = (GameViewListener)arrLocal[i];
            vl.update((Player) this, arg);
        }
    }

    public void notifyObservers(Shelf arg) {
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
            PlayerViewListener vl = (PlayerViewListener)arrLocal[i];
            vl.update((Player) this, arg);
        }
    }

    public synchronized void deleteObservers() {
        obs.removeAllElements();
    }

    protected synchronized void setChanged() {
        changed = true;
    }

    protected synchronized void clearChanged() {
        changed = false;
    }

    public synchronized boolean hasChanged() {
        return changed;
    }

    public synchronized int countObservers() {
        return obs.size();
    }
}