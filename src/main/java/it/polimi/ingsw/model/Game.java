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
     * How to reader and create the PersonalObjCard
     */
    public void generatePersonalObjCards(){
        PersonalCardReader cardReader = new PersonalCardReader();
        List<PersonalObjCard> cardsList = cardReader.readFromFile();
        Random rand = new Random();
        for(int i=0;i<NUMB;i++){
            int n = rand.nextInt();
            this.getPlayers().get(i).setPersonalObjCard(cardsList.get(n));
            cardsList.remove(n);
        }
    }
}