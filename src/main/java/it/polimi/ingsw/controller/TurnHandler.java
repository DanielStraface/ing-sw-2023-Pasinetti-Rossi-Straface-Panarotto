package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.personcard.PersonalObjCard;

public class TurnHandler {
    private TurnChecker turnChecker;
    private PlayerAction playerAction;
    private Game game;
    private boolean gameOver;
    public TurnHandler(Game game){
        this.turnChecker= new TurnChecker();
        this.playerAction = new PlayerAction();
        this.game = game;
        this.gameOver= false;
    }

    public void nextTurn(Player player){
        if(gameOver) {
            if (game.getPlayers().indexOf(player) == (game.getPlayers().size() - 1)) {
                game.setCurrentPlayer(game.getPlayers().get(0));
            } else {
                game.setCurrentPlayer(game.getPlayers().get((game.getPlayers().indexOf(player)) + 1));
            }
        }
        System.out.println("The Game is over!");
        return;
    };
    public void manageTurn() throws Exception{
        Player player = game.getCurrentPlayer();
        PersonalObjCard personalObjCard = player.getMyPersonalOBjCard();
        playerAction.playerGameActionExecutor(game);

        if(turnChecker.manageCheck(player, game)){
            if(game.getPlayers().indexOf(player) == game.getPlayers().size()-1){
                player.addPoints(personalObjCard.shelfCheck(player.getMyShelf()));
                gameOver = true;
                this.nextTurn(player);
            } else{
                player.addPoints(personalObjCard.shelfCheck(player.getMyShelf()));
                this.nextTurn(player);
            }
        } else {
            this.nextTurn(player);
        }
    };
}



