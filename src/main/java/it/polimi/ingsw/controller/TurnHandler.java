package it.polimi.ingsw.controller;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.exceptions.InvalidMatchesException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.personcard.PersonalObjCard;

import java.rmi.RemoteException;

public class TurnHandler {
    private TurnChecker turnChecker;
    private Game game;
    private boolean endGame;
    private boolean gameOver;
    private static final int ENDGAME_POINTS = 1;
    private static final int GAME_OVER = 100;
    public TurnHandler(Game game){
        this.turnChecker= new TurnChecker();
        this.game = game;
        this.endGame= false;
        this.gameOver = false;
    }
 /** nextTurn passes the turn if no shelf has been filled yet. If a player has filled the library, it calls
  *  GameOverHandler method. */
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

    /**
     * manageTurn adds a point to the current player if the current player is the first to have filled the library.
     * The method verify if the player is the FirstPlayer or the player before the FirstPlayer, to verify if the game is to end,
     * then it calls NextTUrn method.
     * @throws Exception
     */
    public void manageTurn() throws Exception {
    public void manageTurn(Client o) throws Exception{
        Player player = game.getCurrentPlayer();

        if(turnChecker.manageCheck(player, game) || endGame) {
            System.err.println("HERE1");
            if(!endGame) player.addPoints(ENDGAME_POINTS);
            endGame = true;
            Player firstPlayer = null;
            for (Player p : game.getPlayers()) {
                if (p.getIsFirstPlayer()) firstPlayer = p;
            }
            if (game.getPlayers().indexOf(player) == game.getPlayers().size() - 1) {
                System.err.print("HERE2");
                if (game.getPlayers().indexOf(firstPlayer) == 0) {
                    System.err.println("HERE3");
                    gameOver = true;
                }
            } else if (game.getPlayers().indexOf(player) == game.getPlayers().indexOf(firstPlayer) - 1) {
                System.err.println("HERE4");
                gameOver = true;
            }
        }
        o.update("Your turn is finished! Please wait for the other players turn");
        this.nextTurn(player);
    }

    /**
     * gameOverHandler is called at the end of the game to add points of personal objective cards and adjacenses and
     * assign the final score to the player.
     */
    private void gameOverHandler() {
        System.out.println("GAME OVER");
        int counter = 1;
        for(Player p : game.getPlayers()){
            PersonalObjCard personalObjCard = p.getMyPersonalOBjCard();
            try {
                p.addPoints(personalObjCard.shelfCheck(p.getMyShelf()));
                p.addPoints(turnChecker.adjacentItemsCheck(p));
                System.out.println("Player " + counter + ":");
                System.out.println("Your points: " + p.getScore());
                counter++;
            } catch (InvalidMatchesException e) {
                throw new RuntimeException(e);
            }
        }
        Player winner = game.getPlayers().get(0);
        if(winner.getScore() < game.getPlayers().get(1).getScore()) winner = game.getPlayers().get(1);
        String finalMessage = winner.getNickname() + " wins with a score of " + winner.getScore() + " points\n" +
                    "The game ends here. Thank you for playing this game!\nBYE";
        System.out.println(finalMessage);
        for(Player p : game.getPlayers()){
            try {
                p.setStatus(GAME_OVER, finalMessage);
            } catch (RemoteException e) {
                System.err.println(e.getMessage());
            }
        }
        System.exit(10);
    }
}