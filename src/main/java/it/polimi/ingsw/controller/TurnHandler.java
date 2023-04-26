package it.polimi.ingsw.controller;

import it.polimi.ingsw.server.AppServerImpl;
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
    public TurnHandler(Game game){
        this.turnChecker= new TurnChecker();
        this.game = game;
        this.endGame= false;
        this.gameOver = false;
    }

    /**
     * Sets the next currentPlayer after the previous' one turn is over, checks if it's the last turn of the match
     * @param player player whose turn is over
     * @throws RemoteException
     */
    public void nextTurn(Player player) throws RemoteException {
        System.out.println("The actual player is := " + player.getNickname() + player.getClientID());
        if(!gameOver) {
            if (game.getPlayers().indexOf(player) == (game.getPlayers().size() - 1)) {
                game.setCurrentPlayer(game.getPlayers().get(0));
                System.out.println("After turn changed is := " + this.game.getCurrentPlayer().getNickname());
            } else {
                game.setCurrentPlayer(game.getPlayers().get((game.getPlayers().indexOf(player)) + 1));
                System.out.println("After turn changed is := " + this.game.getCurrentPlayer().getNickname());
            }
        } else {
            gameOverHandler();
        }
    }

    /**
     * Checks if the currentPlayer filled his Shelf and triggers the endgame accordingly, and manages the turn
     * cycle
     * @param o the Client of the player whose turn is over
     * @throws Exception
     */
    public void manageTurn(Client o) throws Exception{
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
        o.update("Your turn is finished! Please wait for the other players turn");
        this.nextTurn(player);
    }

    /**
     * At the end of the match it calculates every player's points and declares a winner based on who has the most
     */
    private void gameOverHandler() {
        System.out.println("This match has got a game over");
        for(Player p : game.getPlayers()){
            PersonalObjCard personalObjCard = p.getMyPersonalOBjCard();
            try {
                p.addPoints(personalObjCard.shelfCheck(p.getMyShelf()));
                p.addPoints(turnChecker.adjacentItemsCheck(p));
            } catch (InvalidMatchesException e) {
                throw new RuntimeException(e);
            }
        }
        Player winner = game.getPlayers().get(0);
        for(int i=0;i<game.getPlayers().size();i++){
            if(winner.getScore() < game.getPlayers().get(i).getScore()) winner = game.getPlayers().get(i);
        }
        String finalMessage = winner.getNickname() + " wins with a score of " + winner.getScore() + " points\n" +
                    "The game ends here. Thank you for playing this game!\nBYE";
        for(Player p : game.getPlayers()){
            try {
                p.setStatus("%" + finalMessage);
            } catch (RemoteException e) {
                System.err.println(e.getMessage());
            }
        }
        AppServerImpl.gameFinished();
    }

    public boolean getGameOver(){return this.gameOver;}
}