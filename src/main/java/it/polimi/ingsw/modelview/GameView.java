package it.polimi.ingsw.modelview;

import it.polimi.ingsw.model.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GameView implements Serializable {
    private static final long serialVersionUID = 1L;
    private final GameBoardView gameboard;
    private final List<CommonObjCardView> commonObjCards;
    private final PlayerView currentPlayer;

    public GameView(Object o) {
        Game game = (Game) o;
        this.gameboard = new GameBoardView(game.getGameboard().getGameGrid());
        this.commonObjCards = new ArrayList<>();
        this.commonObjCards.add(new CommonObjCardView(game.getCommonObjCard().get(0)));
        this.commonObjCards.add(new CommonObjCardView(game.getCommonObjCard().get(1)));
        this.currentPlayer = new PlayerView(game.getCurrentPlayer());
    }

    public GameBoardView getGameBoard(){return this.gameboard;}
    public List<CommonObjCardView> getCommonObjCard(){return this.commonObjCards;}
    public PlayerView getCurrentPlayer(){return this.currentPlayer;}
}