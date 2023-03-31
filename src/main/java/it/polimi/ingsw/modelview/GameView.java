package it.polimi.ingsw.model.modelview;

import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable; //deprecate
import java.util.Observer; //deprecate

public class GameView extends Observable implements Observer {
    private final GameBoardView gameBoardView;
    private final PlayerView playerView;
    private final List<CommonObjCardView> commonObjCardViewList = new ArrayList<>();

    public GameBoardView getGameBoardView() {
        return gameBoardView;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

    public List<CommonObjCardView> getCommonObjCardViewList() {
        return commonObjCardViewList;
    }

    public GameView(Game game) {
        this.gameBoardView = new GameBoardView(game.getGameboard());
        this.playerView = new PlayerView(game.getCurrentPlayer());
        this.commonObjCardViewList.add( new CommonObjCardView(game.getCommonObjCard().get(0).getType()));
        game.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
