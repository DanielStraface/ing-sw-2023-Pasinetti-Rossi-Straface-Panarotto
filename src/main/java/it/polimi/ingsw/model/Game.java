package it.polimi.ingsw.model;

import java.util.List;
import java.util.Random;

class Game {

    public static final int NUMB = 3;

    private int playersNumber;
    private List<Player> players;
    private GameBoard gameboard;
    private int[][] validGrid;
    private Bag bag;
    private CommonObjCard commonObjCard;
    private Player currentPlayer;

    public Game (int playersNumber){this.playersNumber = playersNumber;}
    public List<Player> getPlayers(){return players;}
    public GameBoard getGameboard(){return gameboard;}
    public CommonObjCard getCommonObjCard(){return commonObjCard;}
    public Bag getBag(){return bag;}
    public Player getCurrentPlayer(){return currentPlayer;}

    //***********************************************
    /**
     * Method generatePersonalObjCards obtains from a cardReader instance a list of personalObj card previously store in
     * a Json file and the distribute one of them to each player of the match.
     */
    public void generatePersonalObjCards(){
        /* Creation of cardReader and fill the list with personalObjCard in reading order*/
        PersonalCardReader cardReader = new PersonalCardReader();
        List<PersonalObjCard> cardsList = cardReader.readFromFile();
        /* Generation of random number to extract from the list a specific personalObjCard and use it as a parameter
        *  for the setPersonalObjCard method of player entity.
        *  This is done for each player in the match */
        Random rand = new Random();
        for(int i=0;i<NUMB;i++){
            int n = rand.nextInt();
            this.getPlayers().get(i).setPersonalObjCard(cardsList.get(n));
            cardsList.remove(n);
        }
    }
}