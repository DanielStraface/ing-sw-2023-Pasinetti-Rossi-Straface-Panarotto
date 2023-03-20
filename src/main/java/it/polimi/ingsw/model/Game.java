package it.polimi.ingsw.model;

import it.polimi.ingsw.model.comcard.CommonObjCard;
import it.polimi.ingsw.model.personcard.PersonalCardReader;
import it.polimi.ingsw.model.personcard.PersonalObjCard;

import java.util.Arrays;

import java.util.List;
import java.util.Random;

class Game {

    private static final int DIM_GAMEBOARD=9;
    public static final int NUMB = 3;

    private int playersNumber;
    private List<Player> players;
    private GameBoard gameboard;
    private int[][] validGrid = new int[DIM_GAMEBOARD][DIM_GAMEBOARD];
    private Bag bag;
    private List<CommonObjCard> commonObjCards;
    private Player currentPlayer;

    /** constructor for Game class */
    public Game (int playersNumber){
        this.playersNumber = playersNumber;
        createPlayers();
        createGameBoard();
        createBag();
        generatePersonalObjCards();
        generateCommonObjCards();
    }

    //***********************************************
    private void createPlayers() {
        //TO IMPLEMENT
    }

    private void createGameBoard(){
        setGridForTwo(this.validGrid);
        switch(this.playersNumber) {
            case 3 -> {
                validGrid[0][3] = 1;
                validGrid[2][2] = 1;
                validGrid[2][6] = 1;
                validGrid[3][8] = 1;
                validGrid[5][0] = 1;
                validGrid[6][2] = 1;
                validGrid[6][6] = 1;
                validGrid[8][5] = 1;
            }
            case 4 -> {
                validGrid[0][3] = 1;
                validGrid[0][4] = 1;
                validGrid[1][5] = 1;
                validGrid[2][2] = 1;
                validGrid[2][6] = 1;
                validGrid[3][1] = 1;
                validGrid[3][8] = 1;
                validGrid[4][0] = 1;
                validGrid[4][8] = 1;
                validGrid[5][0] = 1;
                validGrid[5][7] = 1;
                validGrid[6][2] = 1;
                validGrid[6][6] = 1;
                validGrid[7][3] = 1;
                validGrid[8][4] = 1;
                validGrid[8][5] = 1;
            }
            default -> {}
        }
    }

    /** method to set the GameGrid for two players
     *  0 = invalid slot
     *  1 = playable slot */
    private void setGridForTwo (int[][] Grid) {
        int i, j;
        for (i = 0; i < 9; i++) {
            for (j = 0; j < 9; j++) {
                if (i==1){
                    if(j>2 && j<5){
                        Grid[i][j] = 1;
                    }
                }
                if (i==2 || i==6){
                    if(j>2 && j<6){
                        Grid[i][j] = 1;
                    }
                }
                if (i==3){
                    if(j>1 && j<8){
                        Grid[i][j] = 1;
                    }
                }
                if (i==4){
                    if(j>0 && j<8){
                        Grid[i][j] = 1;
                    }
                }
                if (i==5){
                    if(j>0 && j<7){
                        Grid[i][j] = 1;
                    }
                }
                if (i==7){
                    if(j>3 && j<6){
                        Grid[i][j] = 1;
                    }
                }
            }
        }
    }

    private void createBag() {
        //TO IMPLEMENT
    }

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
        this.commonObjCards.add( new CommonObjCard(this.playersNumber, name.get(n)));
        name.remove(n);
        //add to commonObjCards list the second new commonObjCard with is points array and type set.
        //this is done randomly by getting a new name list type
        this.commonObjCards.add( new CommonObjCard(this.playersNumber, name.get(random.nextInt(name.size()))));
        //commonObjCard.get(0).doCheck(players.get(0));
    }

    /** get methods */
    public List<Player> getPlayers(){return players;}
    public GameBoard getGameboard(){return gameboard;}
    public List<CommonObjCard> getCommonObjCard(){return commonObjCards;}
    public Bag getBag(){return bag;}
    public Player getCurrentPlayer(){return currentPlayer;}
}