package it.polimi.ingsw.listeners;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;

public interface ModelViewListener {
    void update(GameView game, GameBoardView gb);
    void update(GameView game);
    void update(PlayerView player, Item[][] gameGrid);
    void update(PlayerView player, ShelfView shelf);
    void update(PlayerView player, Integer column);
    void update(String msg);
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