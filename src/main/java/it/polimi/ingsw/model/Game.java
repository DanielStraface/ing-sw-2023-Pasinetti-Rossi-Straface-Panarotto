package it.polimi.ingsw.model;

import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.listeners.ModelSubject;
import it.polimi.ingsw.model.comcard.CommonObjCard;
import it.polimi.ingsw.model.comcard.CommonObjCardReader;
import it.polimi.ingsw.model.personcard.PersonalCardReader;
import it.polimi.ingsw.model.personcard.PersonalObjCard;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class Game extends ModelSubject implements Serializable {

    private static final int DIM_GAMEBOARD=9;
    private static final int PLAYABLE = 1;
    private static final int OCCUPIED = 2;
    private int playersNumber;
    private List<Player> players;
    private GameBoard gameboard;
    private int[][] validGrid = new int[DIM_GAMEBOARD][DIM_GAMEBOARD];
    private Bag bag;
    private List<CommonObjCard> commonObjCards;
    private Player currentPlayer;
    private int prevClientID;
    private String gameOverFinalMessage;

    /**
     * constructor for Game class
     * @param playersNumber the number of players in a game
     * @throws InvalidNumberOfPlayersException
     * @throws RemoteException
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
        for(Category c : Category.values()){
            for(int i=0;i<ITEM_NUM;i++){
                this.bag.setItemCards(new Item(c));
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
     * @throws RemoteException
     */
    public void refillGameBoard() throws RemoteException{
        for(int i=0;i<DIM_GAMEBOARD;i++){
            for(int j=0;j<DIM_GAMEBOARD;j++){
                if(gameboard.getValidGrid()[i][j]==PLAYABLE){
                    try {
                        this.gameboard.getGameGrid()[i][j] = this.bag.drawItem();
                        this.gameboard.getValidGrid()[i][j] = OCCUPIED;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else if(gameboard.getValidGrid()[i][j]==0) {
                    this.gameboard.getGameGrid()[i][j] = new Item(null);
                }
            }
        }
        //setChangedAndNotifyListeners(this.gameboard);
    }

    /* set methods */
    public void setCurrentPlayer(Player player) throws RemoteException{
        this.currentPlayer = player;
        setChangedAndNotifyListeners(this);
    }
    public void setTurnFinishedPlayerID(int prevClientID){
        this.prevClientID = prevClientID;
    }
    public void setGameBoard (GameBoard gameboard) { this.gameboard = gameboard; }
    public void setGameOverFinalMessage(String finalMessage) throws RemoteException {
        this.gameOverFinalMessage = finalMessage;
        setChangedAndNotifyListeners(this);
    }


    /* get methods */
    public int getPlayersNumber(){return this.playersNumber;}
    public List<Player> getPlayers(){return players;}
    public GameBoard getGameboard(){return gameboard;}
    public List<CommonObjCard> getCommonObjCard(){return commonObjCards;}
    public Bag getBag(){return bag;}
    public Player getCurrentPlayer(){return currentPlayer;}
    public int getPrevClientID(){return this.prevClientID;}
    public String getGameOverFinalMessage(){return this.gameOverFinalMessage;}
    private void setChangedAndNotifyListeners(GameBoard gb) throws RemoteException {
        setChanged();
        notifyObservers(gb);
    }
    private void setChangedAndNotifyListeners(Game gm) throws RemoteException{
        setChanged();
        notifyObservers(gm);
    }
}