package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.listeners.ModelSubject;
import it.polimi.ingsw.model.personcard.PersonalObjCard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Player class represents the players of the game. The player is identified by a nickname (string) and a clientID
 * (int). His other attributes are: score (int), isFirstPlayer, a reference to his personal shelf with myShelf, to his personal
 * objective cards with myPersonalObjCards, to the list of items that he draws from the game board with selectItems.

 */
public class Player extends ModelSubject implements Serializable {
    private String nickname;    /** has to be unique */

    private int score;
    private int clientID;
    private Shelf myShelf;
    private PersonalObjCard myPersonalObjCard;
    private List<Item> selectItems;
    private boolean isFirstPlayer;
    private static final int PLAYABLE = 1;

    /**
     * constructor for Player class
     */
    public Player(){
        this.score = 0;
        this.isFirstPlayer = false;
        this.myShelf = new Shelf();
        this.selectItems = new ArrayList<>();
    }

    /**
     *  method to pick Items from the game board and put them in the selectItems list
     * @param selectedCoords the GameBoard's coordinates selected by the current player's inputs
     * @param gameGrid the GameBoard's item matrix
     * @param validGrid the GameBoard's int matrix to check is the slot is valid or not
     */
    public void pickItems(List<int[]> selectedCoords,Item[][] gameGrid, int[][] validGrid) {
        System.out.println("selectedCoordsSize := " + selectedCoords.size());
        for (int[] selectedCoord : selectedCoords) {
            int row = selectedCoord[0];
            int col = selectedCoord[1];
            selectItems.add(gameGrid[row][col]);
            gameGrid[row][col] = new Item(null, 0);
            validGrid[row][col] = PLAYABLE;
        }
    }

    /**
     *  method to put Items into personal shelf and remove them from the selectItems list
     * @param selectedCol the Shelf's column selected by the current player
     * @throws OutOfBoundsException attempt to acces an array element with an index that is outside the valid range
     * of the array
     * @throws InvalidNumberOfItemsException if the items drawn by the player are more than three
     */
    public void putItemInShelf(int selectedCol) {
        int[] lastRows = myShelf.getLastRow();
        Item[][] grid=myShelf.getShelfGrid();

        int lastRow = lastRows[selectedCol];

        /* For-cycle to put items into the selected column starting from the last row available*/
        for (int i = 0; i < selectItems.size(); i++, lastRow--) {
            grid[lastRow][selectedCol] = selectItems.get(i);
        }
        updateLastRows(selectedCol, selectItems.size());
        while(!selectItems.isEmpty()){
            selectItems.remove(0);
        }
    }

    /**
     * updateLastRows updates the number of empty slots in the last row of the shelf. Return true if the update is
     * done successfully.
     * @param selectedColumn int
     * @param numOfSelectItem int
     * @return true if the update is done successfully.
     */
    public boolean updateLastRows(int selectedColumn, int numOfSelectItem){
        System.out.println("selectedCol := " + selectedColumn + ", numOf := " + numOfSelectItem);
        int[] lastRows = myShelf.getLastRow();
        switch (numOfSelectItem){
            case 1 -> lastRows[selectedColumn] = lastRows[selectedColumn] - 1;
            case 2 -> lastRows[selectedColumn] = lastRows[selectedColumn] - 2;
            case 3 -> lastRows[selectedColumn] = lastRows[selectedColumn] - 3;
            default ->{
                System.err.println("Last row not updated correctly");
                return false;
            }
        }
        return true;
    }

    /**
     * Method for adding points to the score
     * @param points points to be added to the player
     */
    public void addPoints(int points) {
        this.score += points;
    }

    /**
     * Method for adding points to the score specifically by completing a CommonObjectiveCard
     * @param points points to be added to the player
     * @param toDisplay displays the completion to the player
     */
    public void addPointsByCommonObjCard(int points, String toDisplay) {
        this.score += points;
    }

    /* set methods */

    /**
     * the method sets the personal objective card of the player.
     * @param card
     */
    public void setPersonalObjCard(PersonalObjCard card){
        this.myPersonalObjCard = card;
    }

    /**
     * the method sets the personal shelf of the player.
     * @param shelf
     */
    public void setMyShelf(Shelf shelf){this.myShelf = shelf;}

    /**
     * the method sets isFirstPlayer to true.
     */
    public void setIsFirstPlayer() {this.isFirstPlayer=true;}

    /**
     * the method sets the nickname and the ClientId of the player.
     * @param nickname
     * @param clientID
     */
    public void setNicknameAndClientID(String nickname, int clientID){
        this.nickname = nickname;
        this.clientID = clientID;
    }

    /**
     * get method for the nickname of the player.
     * @return String nickname
     */
    public String getNickname(){
        return this.nickname;
    }

    /**
     * get method for the ClientID.
     * @return int clientID
     */
    public int getClientID(){
        return this.clientID;
    }

    /**
     * get method that return the score of the player.
     * @return int score
     */
    public int getScore(){
        return this.score;
    }

    /**
     * get method that return the player's personal objective card.
     * @return myPersonalObjCard
     */
    public PersonalObjCard getMyPersonalOBjCard(){
        return this.myPersonalObjCard;
    }

    /**
     * get method that return the personal shelf of the player.
     * @return
     */
    public Shelf getMyShelf(){
        return this.myShelf;
    }

    /**
     * get method that return true if the player is the player who started the game.
     * @return boolean isFirstPlayer
     */
    public boolean getIsFirstPlayer(){
        return this.isFirstPlayer;
    }

    /**
     * get method that return the items drawn by the player.
     * @return List selectItems
     */
    public List<Item> getSelectItems(){return this.selectItems;}
}



