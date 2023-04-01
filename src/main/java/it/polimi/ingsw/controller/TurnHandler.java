package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.InvalidMatchesException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.personcard.PersonalObjCard;

public class TurnHandler {
    private TurnChecker turnChecker;
    private PlayerAction playerAction;
    private Game game;
    private boolean endGame;
    private boolean gameOver;
    private static final int ENDGAME_POINTS = 1;
    public TurnHandler(Game game){
        this.turnChecker= new TurnChecker();
        this.playerAction = new PlayerAction();
        this.game = game;
        this.endGame= false;
        this.gameOver = false;
    }

    public void nextTurn(Player player){
        if(!gameOver) {
            if (game.getPlayers().indexOf(player) == (game.getPlayers().size() - 1)) {
                game.setCurrentPlayer(game.getPlayers().get(0));
            } else {
                game.setCurrentPlayer(game.getPlayers().get((game.getPlayers().indexOf(player)) + 1));
            }
        } else {
            gameOverHandler();
        }
    }

    public void manageTurn() throws Exception{
        Player player = game.getCurrentPlayer();

        if(turnChecker.manageCheck(player, game) || endGame) {
            if(!endGame) player.addPoints(ENDGAME_POINTS);
            endGame = true;
            Player firstPlayer = null;
            for (Player p : game.getPlayers()) {
                if (p.getIsFirstPlayer()) firstPlayer = p;
            }
            if (game.getPlayers().indexOf(player) == game.getPlayers().size() - 1) {
                if (game.getPlayers().indexOf(firstPlayer) == 0) {
                    gameOver = true;
                }
            } else if (game.getPlayers().indexOf(player) == game.getPlayers().indexOf(firstPlayer) - 1) {
                gameOver = true;
            }
        }
        this.nextTurn(player);
    }

    private void gameOverHandler() {
        for(Player p : game.getPlayers()){
            PersonalObjCard personalObjCard = p.getMyPersonalOBjCard();
            try {
                p.addPoints(personalObjCard.shelfCheck(p.getMyShelf()));
                p.addPoints(turnChecker.adjacentItemsCheck(p));
            } catch (InvalidMatchesException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.print("GAME OVER");
    }
}


