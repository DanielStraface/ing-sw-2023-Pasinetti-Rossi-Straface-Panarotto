package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.TextualUI;

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

        view.addListener(controller);
        game.addListener(view);
        for(Player player : game.getPlayers()){
            player.addListener(view);
        }

        controller.chooseFirstPlayer();

        view.run();
    }
}