package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;

public class PlayerView implements Serializable {
    private final int score;
    private final ShelfView myShelf;
    private final PersonalObjCardView myPersonalObjCard;

    public PlayerView(Player player) {
        this.score = player.getScore();
        this.myShelf = new ShelfView(player.getMyShelf().getShelfGrid(), player.getMyShelf().getLastRow());
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
}
