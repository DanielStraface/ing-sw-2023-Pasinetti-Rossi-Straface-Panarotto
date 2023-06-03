package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameView implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private final GameBoardView gameBoard;
    private final List<CommonObjCardView> commonObjCards;
    private final List<PlayerView> players;
    private final PlayerView currentPlayer;
    private final int prevClientID;
    private final String gameOverFinalMessage;
    private final boolean gameOverPointToken;
    private final String gameOverPointPlayerNickname;

    /**
     * constructor method
     * @param game
     */
    public GameView(Game game) {
        this.commonObjCards = new ArrayList<>();
        this.players = new ArrayList<>();
        this.gameBoard = new GameBoardView(game.getGameboard());
        this.commonObjCards.add(new CommonObjCardView(game.getCommonObjCard().get(0)));
        this.commonObjCards.add(new CommonObjCardView(game.getCommonObjCard().get(1)));
        for(Player player : game.getPlayers()){
            this.players.add(new PlayerView(player));
        }
        this.currentPlayer = new PlayerView(game.getCurrentPlayer());
        this.prevClientID = game.getPrevClientID();
        this.gameOverFinalMessage = game.getGameOverFinalMessage();
        this.gameOverPointToken = game.getGameOverPointToken();
        this.gameOverPointPlayerNickname = game.getGameOverPointPlayerName();
    }

    /**
     * get method
     * @return the game board
     */
    public GameBoardView getGameBoard(){return this.gameBoard;}

    /**
     * get method
     * @return List -> commonObjCards
     */
    public List<CommonObjCardView> getCommonObjCard(){return this.commonObjCards;}

    /**
     * get method
     * @return List -> players
     */
    public List<PlayerView> getPlayers(){return this.players;}

    /**
     * get method
     * @return currentPlayer, the player who is playing
     */
    public PlayerView getCurrentPlayer(){return this.currentPlayer;}

    /**
     * get method
     * @return int prevClientID
     */
    public int getPrevClientID(){return this.prevClientID;}

    /**
     * get method
     * @return String -> gameOverFinalMessage
     */
    public String getGameOverFinalMessage(){return this.gameOverFinalMessage;}

    /**
     * get method
     * @return boolean -> gameOverPointToken
     */
    public boolean getGameOverPointToken(){return this.gameOverPointToken;}
    /**
     * get method
     * @return String -> gameOverPointPlayerNickname
     */
    public String getGameOverPointPlayerNickname(){return this.gameOverPointPlayerNickname;}
}