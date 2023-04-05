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
        Game game = null;
        try{
            game = new Game(NUMB_OF_PLAYERS);
        } catch (InvalidNumberOfPlayersException e){
            System.err.println(e.getMessage());
            System.exit(1);
        }

        TextualUI view = new TextualUI();
        Controller controller = new Controller(game, view);

        GameView gameView = new GameView(game);
        List<PlayerView> playerViews = new ArrayList<>();
        view.addListener(controller);
        gameView.addListener(view);
        int counter = 0;
        for(Player player : game.getPlayers()){
            PlayerView playerView = new PlayerView(player);
            playerViews.add(playerView);
            playerViews.get(counter).addListener(view);
            player.addListener(playerView);
            counter++;
        }
        game.addListener(gameView);

        controller.chooseFirstPlayer();

        view.run();
    }
}