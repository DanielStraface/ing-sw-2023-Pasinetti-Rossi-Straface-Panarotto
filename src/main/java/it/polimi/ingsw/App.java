package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.view.TextualUI;

import java.util.ArrayList;
import java.util.List;

public class App
{
    public static void main( String[] args ) {
        final int NUMB_OF_PLAYERS = 2;
        /* creation of game */
        Game game = null;
        try{
            game = new Game(NUMB_OF_PLAYERS);
        } catch (InvalidNumberOfPlayersException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }

        /* creation of view and link with controller */
        TextualUI view = new TextualUI();
        Controller controller = new Controller(game, view);

        /* creation of gameView (immutable obj of game) */
        GameView gameView = new GameView(game);
        List<PlayerView> playerViews = new ArrayList<>();
        /*add listener to view (controller) and listener to gameView (view)*/
        view.addListener(controller);
        gameView.addListener(view);
        int counter = 0;
        for(Player player : game.getPlayers()){
            /* creation of a playerView for this player */
            PlayerView playerView = new PlayerView(player);
            playerViews.add(playerView);
            /* add listener to this playerView (view) */
            playerViews.get(counter).addListener(view);
            /* add listener to this player (playerView) */
            player.addListener(playerView);
            counter++;
        }
        /* add listener to game (gameView)*/
        game.addListener(gameView);

        controller.chooseFirstPlayer();

        view.run();
    }
}