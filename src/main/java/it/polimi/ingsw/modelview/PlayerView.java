package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.Player;

import java.io.Serial;
import java.io.Serializable;

/**
 * The immutable view of Player
 */
public class PlayerView implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private final int score;
    private final ShelfView myShelf;
    private final PersonalObjCardView myPersonalObjCard;
    private final String nickname;
    private final int clientID;
    private final boolean isFirstPlayer;

    /**
     * constructor method
     * @param player Player
     */
    public PlayerView(Player player) {
        this.score = player.getScore();
        this.myShelf = new ShelfView(player.getMyShelf().getShelfGrid(), player.getMyShelf().getLastRow());
        this.myPersonalObjCard = new PersonalObjCardView(player.getMyPersonalOBjCard().getCardGrid(),
                player.getMyPersonalOBjCard().getPersonalObjCardDescription());
        this.nickname = player.getNickname();
        this.clientID = player.getClientID();
        this.isFirstPlayer = player.getIsFirstPlayer();
    }

    /**
     * get method
     * @return int score
     */
    public int getScore(){
        return this.score;
    }

    /**
     * get method
     * @return myPersonalObjCard, the personal objective card of the player
     */
    public PersonalObjCardView getMyPersonalOBjCard(){
        return this.myPersonalObjCard;
    }

    /**
     * get method
     * @return myShelf, the personal shelf of the player
     */
    public ShelfView getMyShelf(){
        return this.myShelf;
    }

    /**
     * get method
     * @return String nickname, the nickname of the player
     */
    public String getNickname(){return this.nickname;}

    /**
     * get method
     * @return clientID
     */
    public int getClientID(){return this.clientID;}

    /**
     * get method
     * @return true if the player is the player who started the game
     */
    public boolean getIsFirstPlayer(){return this.isFirstPlayer;}
}
