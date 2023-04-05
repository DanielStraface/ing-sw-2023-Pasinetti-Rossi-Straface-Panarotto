package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.*;

public interface ViewListener {
    void update(Game game, GameBoard gb);
    void update(Game game);
    void update(Player player, Item[][] gameGrid);
    void update(Player player, Shelf shelf);
    void update(Player player, Integer column);
}

/**
 * A class can implement the {@code Observer} interface when it
 * wants to be informed of changes in observable objects.
 *
 * @see     Observable
 *
 * @param <SubjectType> the type of the observable object
 *                     that this observer is observing
 * @param <Event> the enumeration of the event that this observer is observing
 *
 * @implNote
 * This class is a Generic Implementation of the deprecated {@link java.util.Observer}.
 */
/**
 * This method is called whenever the observed object is changed. An
 * application calls an {@code Observable} object's
 * {@code notifyObservers} method to have all the object's
 * observers notified of the change.
 *
 * @param   o     the observable object.
 * @param   arg   an argument passed to the {@code notifyObservers}
 *                 method.
 */