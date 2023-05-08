package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.Player;

import java.io.Serial;
import java.io.Serializable;

public class PlayerView implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private final int score;
    private final ShelfView myShelf;
    private final PersonalObjCardView myPersonalObjCard;
    private final String nickname;
    private final int clientID;

    public PlayerView(Player player) {
        this.score = player.getScore();
        this.myShelf = new ShelfView(player.getMyShelf().getShelfGrid(), player.getMyShelf().getLastRow());
        this.myPersonalObjCard = new PersonalObjCardView(player.getMyPersonalOBjCard().getCardGrid());
        this.nickname = player.getNickname();
        this.clientID = player.getClientID();
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
    public String getNickname(){return this.nickname;}
    public int getClientID(){return this.clientID;}
}
