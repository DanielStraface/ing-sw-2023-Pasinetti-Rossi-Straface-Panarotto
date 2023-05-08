package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.listeners.ModelSubject;
import it.polimi.ingsw.model.personcard.PersonalObjCard;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Player extends ModelSubject implements Serializable {
    private String nickname;    /** has to be unique */

    private int score;
    private int clientID;
    private Shelf myShelf;
    private PersonalObjCard myPersonalObjCard;
    private List<Item> selectItems;
    private boolean isFirstPlayer;
    private static final int INVALID = 0;
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
     * @throws RemoteException
     */
    public void pickItems(List<int[]> selectedCoords,Item[][] gameGrid, int[][] validGrid) throws RemoteException {
        for (int[] selectedCoord : selectedCoords) {
            int row = selectedCoord[0];
            int col = selectedCoord[1];
            selectItems.add(gameGrid[row][col]);
            gameGrid[row][col] = new Item(null);
            validGrid[row][col] = PLAYABLE;
        }
        setChangedAndNotifyListener(gameGrid);
    }

    /**
     *  method to put Items into personal shelf and remove them from the selectItems list
     * @param selectedCol the Shelf's column selected by the current player
     * @throws OutOfBoundsException
     * @throws InvalidNumberOfItemsException
     * @throws RemoteException
     */
    public void putItemInShelf(int selectedCol) throws RemoteException, FullColumnException {
        Item[][] grid=myShelf.getShelfGrid();
        /* For-cycle to search the last row available*/
        int lastRow = 0;
        for (int row = 0; row<6; row++) {
            if (grid[row][selectedCol].getCategoryType() == null ) {
                lastRow = row;
            }
        }
        if(lastRow == 0){
            throw new FullColumnException();
        }
            /* For-cycle to put items into the selected column starting from the last row available*/
            for (int i = 0; i < selectItems.size(); i++, lastRow--) {
                grid[lastRow][selectedCol] = selectItems.get(i);
            }
            while(!selectItems.isEmpty()){
                selectItems.remove(0);
            }
        setChangedAndNotifyListener(this.myShelf);
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
        setChanged();
        notifyObservers(toDisplay);
    }

    /* set methods */
    public void setPersonalObjCard(PersonalObjCard card){
        this.myPersonalObjCard = card;
    }
    public void setMyShelf(Shelf shelf){this.myShelf = shelf;}
    public void setIsFirstPlayer() {this.isFirstPlayer=true;}
    public void setNicknameAndClientID(String nickname, int clientID){
        this.nickname = nickname;
        this.clientID = clientID;
    }
    public void setStatus(String msg) throws RemoteException {
        setChangedAndNotifyListener(msg);
    }

    /* get methods */
    public String getNickname(){
        return this.nickname;
    }
    public int getClientID(){
        return this.clientID;
    }
    public int getScore(){
        return this.score;
    }
    public PersonalObjCard getMyPersonalOBjCard(){
        return this.myPersonalObjCard;
    }
    public Shelf getMyShelf(){
        return this.myShelf;
    }
    public boolean getIsFirstPlayer(){
        return this.isFirstPlayer;
    }
    public List<Item> getSelectItems(){return this.selectItems;}

    private void setChangedAndNotifyListener(Item[][] gg) throws RemoteException{
        setChanged();
        notifyObservers(gg);
    }
    private void setChangedAndNotifyListener(Shelf sh) throws RemoteException{
        setChanged();
        notifyObservers(sh);
    }
    private void setChangedAndNotifyListener(String msg) {
        setChanged();
        notifyObservers(msg);
    }
}



