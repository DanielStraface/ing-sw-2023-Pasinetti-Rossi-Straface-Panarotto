package it.polimi.ingsw.modelview;

import it.polimi.ingsw.listeners.GameViewListener;
import it.polimi.ingsw.listeners.GameViewSubject;
import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;

public class GameView extends GameViewSubject implements GameViewListener {
    private final GameBoardView gameboard;
    private final List<CommonObjCardView> commonObjCards;
    private final PlayerView currentPlayer;

    public GameView(Game game) {
        this.gameboard = new GameBoardView(game.getGameboard().getGameGrid());
        this.commonObjCards = new ArrayList<>();
        this.commonObjCards.add(new CommonObjCardView(game.getCommonObjCard().get(0)));
        this.commonObjCards.add(new CommonObjCardView(game.getCommonObjCard().get(1)));
        this.currentPlayer = new PlayerView(game.getCurrentPlayer());
    }
    public GameBoardView getGameBoard(){return gameboard;}
    public List<CommonObjCardView> getCommonObjCard(){return commonObjCards;}
    public PlayerView getCurrentPlayer(){return currentPlayer;}

    @Override
    public void update(Game game, GameBoard gb) {
        setChanged();
        notifyObservers(new GameBoardView(gb.getGameGrid()));
    }

    @Override
    public void update(Game game) {
        setChanged();
        notifyObservers(new GameView(game));
    }

    @Override
    public void update(Player player, Integer column) {
        setChanged();
        notifyObservers(column);
    }
}
