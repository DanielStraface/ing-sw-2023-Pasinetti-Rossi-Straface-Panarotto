package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.exceptions.NoElementException;
import it.polimi.ingsw.listeners.ModelSubject;
import it.polimi.ingsw.model.comcard.CommonObjCard;
import it.polimi.ingsw.model.comcard.CommonObjCardReader;
import it.polimi.ingsw.model.personcard.PersonalCardReader;
import it.polimi.ingsw.model.personcard.PersonalObjCard;
import it.polimi.ingsw.server.AppServerImpl;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

/**
 * Game class represents the main logic of MyShelfie. It creates the game throw methods: createPlayers, createBag that refills the bag with the items,
 * generatePersonalObjCards that distributes one of the personal objective cards to each player of the match,
 * generateCommonObjCards that creates the two objective cards of the match, refillGameBoard that refills the
 * game board when necessary, setter and getter.
 */
public class Game extends ModelSubject implements Serializable {

    private static final int DIM_GAMEBOARD=9;
    private static final int PLAYABLE = 1;
    private static final int OCCUPIED = 2;
    private final int playersNumber;
    private final List<Player> players;
    private GameBoard gameboard;
    private int[][] validGrid = new int[DIM_GAMEBOARD][DIM_GAMEBOARD];
    private final Bag bag;
    private final List<CommonObjCard> commonObjCards;
    private Player currentPlayer;
    private int prevClientID;
    private String gameOverFinalMessage;
    private boolean gameOverPointToken;
    private String gameOverPointPlayerName;

    /**
     * constructor for Game class
     * @param playersNumber the number of players in a game
     * @throws InvalidNumberOfPlayersException if number of players entered is less than two or more than four
     * @throws RemoteException if the creation of the game board goes wrong
     */
    public Game (int playersNumber) throws InvalidNumberOfPlayersException, RemoteException{
        if(playersNumber <= 1 || playersNumber >= 5) throw new InvalidNumberOfPlayersException();
        this.playersNumber = playersNumber;
        this.prevClientID = -1;
        this.players = new ArrayList<Player>(playersNumber);
        this.bag = new Bag();
        this.gameboard = new GameBoard(playersNumber);
        this.commonObjCards = new ArrayList<CommonObjCard>();
        createPlayers();
        createBag();
        refillGameBoard();
        generatePersonalObjCards();
        generateCommonObjCards();
    }

    //***********************************************

    /**
     * Creates x number of Players (x = playersNumber)
     */
    private void createPlayers(){
        for(int i=0;i<this.playersNumber;i++){
            //nickname: "space" for each player, clientID: 0, isFirstPlayer: false;
            players.add(new Player());
        }
    }

    /**
     * Refills bag with 22 Items of each Category
     */
    private void createBag() {
        final int ITEM_NUM = 22;
        Random random = new Random();
        for(Category c : Category.values()){
            for(int i=0;i<ITEM_NUM;i++){
                this.bag.setItemCards(new Item(c, random.nextInt(3) + 1));
            }
        }
    }

    /**
     * Method generatePersonalObjCards obtains from a cardReader instance a list of personalObj card previously store in
     * a Json file and the distribute one of them to each player of the match.
     */
    public void generatePersonalObjCards(){
        /* Creation of cardReader and fill the list with personalObjCard in reading order*/
        PersonalCardReader cardReader = new PersonalCardReader();
        List<PersonalObjCard> cardsList = new LinkedList<>(cardReader.readFromFile());
        /* Generation of random number to extract from the list a specific personalObjCard and use it as a parameter
         *  for the setPersonalObjCard method of player entity.
         *  This is done for each player in the match */
        Random rand = new Random();
        for(int i=0;i<this.playersNumber;i++){
            int n = rand.nextInt(cardsList.size());
            this.getPlayers().get(i).setPersonalObjCard(cardsList.remove(n));
        }
    }

    /**
     * Method generateCommonObjCards create the two commonObjCards of the match.
     */
    public void generateCommonObjCards() {
        CommonObjCardReader descrReader = new CommonObjCardReader();
        List<String> descriptions = new LinkedList<>(descrReader.readFromFile());
        /* Randomly generate an int that will be used for pop a commonObjCard type*/
        Random random = new Random();
        List<Integer> type = new LinkedList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        try{
            for(int i=0;i<2;i++){
                //generate the type by popping the name list
                int n = random.nextInt(type.size());
                //add to commonObjCards list the first new commonObjCard with is points array and type set
                this.commonObjCards.add( new CommonObjCard(this.playersNumber, type.remove(n), descriptions.remove(n)));
                //this.commonObjCards.add( new CommonObjCard(this.playersNumber, type.remove(n)));
            }
        } catch (InvalidNumberOfPlayersException e){
            System.err.println("Error: creation of commonObjCards");
        }

    }

    /**
     * Refills every PLAYABLE slot of the GameBoard with an Item of a random Category drawn from the Bag
     */
    public void refillGameBoard() {
        int[][] vlg = this.gameboard.getValidGrid();
        for(int i=0;i<DIM_GAMEBOARD;i++){
            for(int j=0;j<DIM_GAMEBOARD;j++){
                if(vlg[i][j]==PLAYABLE){
                    try {
                        this.gameboard.getGameGrid()[i][j] = this.bag.drawItem();
                        this.gameboard.getValidGrid()[i][j] = OCCUPIED;
                    } catch (NoElementException e) {
                        System.err.println(e.getMessage() + ", client notification and termination of the game");
                        try {
                            this.notifyDisconnection(this, ":Empty Bag", e.getMessage());
                            return;
                        } catch (RemoteException ex) {
                            System.err.println("Cannot notify the empty bag: " + e.getMessage());
                        }
                    }
                } else if(vlg[i][j]==0) {
                    this.gameboard.getGameGrid()[i][j] = new Item(null, 0);
                }
            }
        }
        //setChangedAndNotifyListeners(this.gameboard);
    }

    /**
     * notifies that a player has reached a common objective card
     * @param player player who has reached a common objective card
     * @param toDisplay the message to be display
     */
    public void commonObjCardReached(Player player, String toDisplay){
        setChanged();
        notifyObservers(player, toDisplay);
    }

    /** set method that save the matchID.
     * @param player the current player
     * @param matchID int
     * @throws RemoteException if the execution of setChangedAndNotifyListeners method call goes wrong
     */
    public void setAndSave(int matchID, Player player) throws RemoteException {
        this.currentPlayer = player;
        String fileName = "match" + matchID + ".ser";
        Controller.saveGame(this, fileName);
        AppServerImpl.addPrevMatchSave(matchID);
        setChangedAndNotifyListeners(this);
    }

    /**
     * set method for current player
     * @param player Player
     * @throws RemoteException if the execution of setChangedAndNotifyListeners method call goes wrong
     */
    public void setCurrentPlayer(Player player) throws RemoteException{
        this.currentPlayer = player;
        setChangedAndNotifyListeners(this);
    }

    /**
     * set method for a player who finished the turn.
     * @param prevClientID int
     */
    public void setTurnFinishedPlayerID(int prevClientID){
        this.prevClientID = prevClientID;
    }

    /**
     * set method for the game board.
     * @param gameboard GameBoard
     */
    public void setGameBoard (GameBoard gameboard) { this.gameboard = gameboard; }

    /**
     * set metod for the valid grid.
     * @param validGrid int[][]
     */
    public void setValidGrid (int[][] validGrid) { this.validGrid = validGrid; }

    /**
     * Method to set specific CommonObjCards, used only for tests
     * @param type1 first CommonObjCard type
     * @param type2 second CommonObjCard type
     * @throws InvalidNumberOfPlayersException when the number of players given is invalid
     */
    public void setCommonObjCards(int type1,int type2) throws InvalidNumberOfPlayersException {
        CommonObjCardReader descrReader = new CommonObjCardReader();
        List<String> descriptions = new LinkedList<>(descrReader.readFromFile());
        List<Integer> type = new LinkedList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12));
        this.getCommonObjCard().clear();
        this.commonObjCards.add( new CommonObjCard(this.playersNumber, type.remove(type1), descriptions.remove(type1)));
        this.commonObjCards.add( new CommonObjCard(this.playersNumber, type.remove(type2), descriptions.remove(type2)));
    }

    /**
     * set method for the game over message.
     * @param finalMessage the game over message
     * @throws RemoteException if the execution of setChangedAndNotifyListeners method call goes wrong
     */
    public void setGameOverFinalMessage(String finalMessage) throws RemoteException {
        this.gameOverFinalMessage = finalMessage;
        setChangedAndNotifyListeners(this);
    }

    /**
     * set method
     * @param value true if a player has filled the shelf
     * @param nickname name of the player who filled the library
     */
    public void setGameOverPointToken(boolean value, String nickname){
        this.gameOverPointToken = value;
        this.gameOverPointPlayerName = nickname;
    }

    /**
     * get method that returns the number of players.
     */
    public int getPlayersNumber(){return this.playersNumber;}

    /**
     * get method.
     * @return List players
     */
    public List<Player> getPlayers(){return players;}

    /**
     * get method.
      * @return game board
     */
    public GameBoard getGameboard(){return gameboard;}

    /**
     * get method.
     * @return List commonObjCards
     */
    public List<CommonObjCard> getCommonObjCard(){return commonObjCards;}

    /**
     * get method.
     * @return bag
     */
    public Bag getBag(){return bag;}

    /**
     * get method
     * @return currentPlayer, the player who is playing.
     */
    public Player getCurrentPlayer(){return currentPlayer;}

    /**
     * get method.
     * @return prevClientID
     */
    public int getPrevClientID(){return this.prevClientID;}

    /**
     * get method.
     * @return int[][] -> validGrid
     */
    public int[][] getValidGrid(){return validGrid;}

    /**
     * get method.
     * @return string gameOverFinalMessage
     */
    public String getGameOverFinalMessage(){return this.gameOverFinalMessage;}
    /**
     * get method.
     * @return boolean gameOverPointToken
     */
    public boolean getGameOverPointToken(){return this.gameOverPointToken;}
    /**
     * get method
     * @return String -> gameOverPointPlayerNickname
     */
    public String getGameOverPointPlayerName(){return this.gameOverPointPlayerName;}

    /**
     * set method for the game.
     * @param gm Fame
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    private void setChangedAndNotifyListeners(Game gm) throws RemoteException{
        setChanged();
        notifyObservers(gm);
    }
}