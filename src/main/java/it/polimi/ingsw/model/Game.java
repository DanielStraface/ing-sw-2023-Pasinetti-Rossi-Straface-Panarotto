package it.polimi.ingsw.model;

import it.polimi.ingsw.model.comcard.CommonObjCard;
import it.polimi.ingsw.model.personcard.PersonalCardReader;
import it.polimi.ingsw.model.personcard.PersonalObjCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Game {

    public static final int NUMB = 3;

    private int playersNumber;
    private List<Player> players;
    private GameBoard gameboard;
    private int[][] validGrid;
    private Bag bag;
    private List<CommonObjCard> commonObjCards;
    private Player currentPlayer;

    public Game (int playersNumber){this.playersNumber = playersNumber;}
    public List<Player> getPlayers(){return players;}
    public GameBoard getGameboard(){return gameboard;}
    public List<CommonObjCard> getCommonObjCard(){return commonObjCards;}
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

    /**
     * Method generateCommonObjCards create the two commonObjCards of the match.
     */
    public void generateCommonObjCards(){
        /* Randomly generate an int that will be used for pop a commonObjCard type*/
        Random random = new Random();
        List<Integer> name = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        //generate the type by popping the name list
        int n = random.nextInt(name.size());
        //add to commonObjCards list the first new commonObjCard with is points array and type set
        this.commonObjCards.add( new CommonObjCard(this.playersNumber, name.get(random.nextInt(n))));
        name.remove(n);
        //add to commonObjCards list the second new commonObjCard with is points array and type set.
        //this is done randomly by getting a new name list type
        this.commonObjCards.add( new CommonObjCard(this.playersNumber, name.get(random.nextInt(name.size()))));
        //commonObjCard.get(0).doCheck(players.get(0));
    }
}