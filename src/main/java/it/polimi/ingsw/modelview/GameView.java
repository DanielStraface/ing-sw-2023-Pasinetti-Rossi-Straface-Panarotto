package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameView implements Serializable {
    private static final long serialVersionUID = 1L;
    private final GameBoardView gameBoard;
    private final List<CommonObjCardView> commonObjCards;
    private final PlayerView currentPlayer;

    public GameView(Object o) {
        Game game = (Game) o;
        this.gameBoard = new GameBoardView(game.getGameboard().getGameGrid(), game.getGameboard().getValidGrid());
        this.commonObjCards = new ArrayList<>();
        this.commonObjCards.add(new CommonObjCardView(game.getCommonObjCard().get(0)));
        this.commonObjCards.add(new CommonObjCardView(game.getCommonObjCard().get(1)));
        this.currentPlayer = new PlayerView(game.getCurrentPlayer());
    }

    public GameBoardView getGameBoard(){return this.gameBoard;}
    public List<CommonObjCardView> getCommonObjCard(){return this.commonObjCards;}
    public PlayerView getCurrentPlayer(){return this.currentPlayer;}
}