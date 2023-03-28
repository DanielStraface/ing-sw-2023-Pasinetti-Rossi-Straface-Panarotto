package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class TurnHandler {
    private TurnChecker turnChecker;
    private PlayerAction playerAction;
    private Game game;
    public TurnHandler(Game game){
        this.turnChecker= new TurnChecker();
        this.playerAction = new PlayerAction();
        this.game = game;
    }

    public void nextTurn(Player player){
        if(game.getPlayers().indexOf(player) == (game.getPlayers().size() - 1)){
            game.setCurrentPlayer(game.getPlayers().get(0));
        } else {
            game.setCurrentPlayer(game.getPlayers().get((game.getPlayers().indexOf(player)) + 1));
        }
    };
    public void manageTurn(){
        Player player = game.getCurrentPlayer();
        playerAction.playerGameActionExecutor(game);
        //if this is the last turn, the following operations will be different
        if(turnChecker.manageCheck(player, game)){
            //if this player seats before the "isFirstPlayer" player the match ends after this turn
            //during this must be updated the players score by personalObjCard
            //
        } else {
            //the turn passes until the next player is the first one
            this.nextTurn(player);
        }
    };
}
