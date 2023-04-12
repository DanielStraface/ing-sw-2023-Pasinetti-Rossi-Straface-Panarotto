package it.polimi.ingsw.modelview;

import it.polimi.ingsw.listeners.GameViewSubject;
import it.polimi.ingsw.listeners.PlayerListener;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Shelf;

import java.io.Serializable;

public class PlayerView extends GameViewSubject implements PlayerListener, Serializable {
    private final int score;
    private final ShelfView myShelf;
    private final PersonalObjCardView myPersonalObjCard;

    public PlayerView(Player player) {
        this.score = player.getScore();
        this.myShelf = new ShelfView(player.getMyShelf().getShelfGrid());
        this.myPersonalObjCard = new PersonalObjCardView(player.getMyPersonalOBjCard().getCardGrid());
    }
    public int getScore(){
        return this.score;
    }
    public PersonalObjCardView getMyPersonalOBjCard(){
        return this.myPersonalObjCard;
    }
    public ShelfView getMyShelf(){
        return this.myShelf;
    }

    @Override
    public void update(Player player, Item[][] gameGrid) {
        setChanged();
        notifyObservers(gameGrid);
    }

    @Override
    public void update(Player player, Shelf shelf) {
        setChanged();
        notifyObservers(new ShelfView(shelf.getShelfGrid()));
    }

    @Override
    public void update(String commonObjCardReached) {
        setChanged();
        notifyObservers(commonObjCardReached);
    }
}
