package it.polimi.ingsw.controller;

import it.polimi.ingsw.server.AppServerImpl;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.exceptions.InvalidMatchesException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.personcard.PersonalObjCard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class TurnHandler {
    private final TurnChecker turnChecker;
    private final Game game;
    private boolean endGame;
    private boolean gameOver;
    private static final int ENDGAME_POINTS = 1;

    /**
     * Constructor method
     */
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
    public void nextTurn(int matchID, Player player) throws RemoteException {
        game.setTurnFinishedPlayerID(player.getClientID());
        if(!gameOver) {
            if (game.getPlayers().indexOf(player) == (game.getPlayers().size() - 1)) {
                saveModelAndSetNewPlayer(matchID, game.getPlayers().get(0));
                //game.setCurrentPlayer(game.getPlayers().get(0));
            } else {
                saveModelAndSetNewPlayer(matchID, game.getPlayers().get((game.getPlayers().indexOf(player)) + 1));
                //game.setCurrentPlayer(game.getPlayers().get((game.getPlayers().indexOf(player)) + 1));
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
    public void manageTurn(int matchID, Client o) throws RemoteException {
        Player player = game.getCurrentPlayer();
        if(turnChecker.manageCheck(player, game) || endGame) {
            if(!endGame) player.addPoints(ENDGAME_POINTS);
            endGame = true;
            game.setGameOverPointToken(true, player.getNickname());
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
        this.nextTurn(matchID,player);
    }

    /**
     * At the end of the match it calculates every player's points and declares a winner based on who has the most
     */
    private void gameOverHandler() {
        System.out.println("This match has got a game over");
        List<Player> players = new ArrayList<Player>();
        List<Integer> adjacentPointsAdded = new ArrayList<>();
        List<Integer> persObjPointsAdded = new ArrayList<>();
        for(Player p : game.getPlayers()){
            PersonalObjCard personalObjCard = p.getMyPersonalOBjCard();
            try {
                p.addPoints(personalObjCard.shelfCheck(p.getMyShelf()));
                Integer persObj = personalObjCard.shelfCheck(p.getMyShelf());
                persObjPointsAdded.add(persObj);
                p.addPoints(turnChecker.adjacentItemsCheck(p));
                Integer adj = turnChecker.adjacentItemsCheck(p);
                adjacentPointsAdded.add(adj);
                players.add(p);
            } catch (InvalidMatchesException e) {
                System.err.println("Error occurred while calculating the players point: " + e.getMessage());
            }
        }
        Player winner = game.getPlayers().get(0);
        for(int i=0;i<game.getPlayers().size();i++){
            if(winner.getScore() < game.getPlayers().get(i).getScore()) winner = game.getPlayers().get(i);
        }
        String initMessage ="-"+ players.get(0).getNickname() + " has a score of " + players.get(0).getScore() +
                " (+"+persObjPointsAdded.get(0)+" personal objective points, +" + adjacentPointsAdded.get(0) + " adjacent items points)\n";
        for(int i=1; i< players.size(); i++){
            String tempMessage ="-"+ players.get(i).getNickname() + " has a score of " + players.get(i).getScore() +
                    " (+"+persObjPointsAdded.get(i)+" personal objective points, +" + adjacentPointsAdded.get(i) + " adjacent items points)\n";
            initMessage = initMessage.concat(tempMessage);
        }
        String finalMessage = initMessage.concat("\n"+winner.getNickname() + " wins with a score of " + winner.getScore()
                + " points!\n\nThe game ends here. Thank you for playing this game!\nBYE!\n\nWaiting for app termination by user");
        try {
            this.game.setGameOverFinalMessage(finalMessage);
        } catch (RemoteException e) {
            System.err.println("Cannot notify the gameOver: " + e.getMessage());
        }
        AppServerImpl.gameFinished();
    }

    /**
     * Method used to help with the player turn cycle
     * @param matchID the matchID where the turn needs to be changed
     * @param player the player whose turn is over
     * @throws RemoteException
     */
    private void saveModelAndSetNewPlayer(int matchID, Player player) throws RemoteException {
        game.setAndSave(matchID, player);
    }

    /**
     * Get method for GameOver boolean
     * @return GameOver boolean that indicates whether the game is on its last turn or not
     */
    public boolean getGameOver(){return this.gameOver;}
}