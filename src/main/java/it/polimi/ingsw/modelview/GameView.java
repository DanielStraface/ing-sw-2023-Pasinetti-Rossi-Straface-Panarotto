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
    private final Exception exceptionToDisplay;

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
        this.exceptionToDisplay = game.getExceptionToDisplay();
    }
    public GameBoardView getGameBoard(){return this.gameBoard;}
    public List<CommonObjCardView> getCommonObjCard(){return this.commonObjCards;}
    public List<PlayerView> getPlayers(){return this.players;}
    public PlayerView getCurrentPlayer(){return this.currentPlayer;}
    public int getPrevClientID(){return this.prevClientID;}
    public String getGameOverFinalMessage(){return this.gameOverFinalMessage;}
    public Exception getExceptionToDisplay(){return this.exceptionToDisplay;}
}