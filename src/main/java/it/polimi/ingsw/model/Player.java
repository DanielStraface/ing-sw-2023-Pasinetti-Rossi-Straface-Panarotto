package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InvalidNumberOfItemsException;
import it.polimi.ingsw.exceptions.InvalidSelectionException;
import it.polimi.ingsw.model.personcard.PersonalObjCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Player extends Observable {
    private String nickname;    /** has to be unique */

    private int score;
    private int clientID;
    private Shelf myShelf;
    private PersonalObjCard myPersonalObjCard;
    private List<Item> selectItems;
    private boolean isFirstPlayer;
    private static final int INVALID = 0;

    /** constructor for Player class */
    public Player() {
        this.score = 0;
        this.isFirstPlayer = false;
        this.myShelf = new Shelf();
        this.selectItems=new ArrayList<>();
    }

    /** method to pick Items from the game board*/
    public void pickItems(List<int[]> selectedCoords,Item[][] gameGrid, int[][] validGrid) throws Exception {
        if(selectedCoords.isEmpty()){
            throw new IllegalStateException("selectedCoords is empty");
        }
        for(int i=0;i<selectedCoords.size();i++){
            if(validGrid[selectedCoords.get(i)[0]][selectedCoords.get(i)[1]] == INVALID)
                throw new InvalidSelectionException("Selected item in invalid slot");
        }
        boolean sameX = true;
        boolean sameY = true;
        int XOfFirstCoordinate= selectedCoords.get(0)[0];
        int YOfFirstCoordinate= selectedCoords.get(0)[1];
        /* For-cycle to analyse values of coordinates: first bond: Items from the same row or column*/
        for(int i=1;i<selectedCoords.size();i++) {
            /*If all coordinates haven't the same x: items will not be picked from the same row */
            if(selectedCoords.get(i)[0]!= XOfFirstCoordinate){
                sameX=false;
            }
            /* If all coordinates haven't the same y: items will not be picked from the same column */
            if(selectedCoords.get(i)[1]!= YOfFirstCoordinate){
                sameY=false;
            }
        }
        /* If all coordinates haven't the same x or the same y: items can not be picked from the game board */
        if (!sameX && !sameY) {
            throw new InvalidSelectionException("Invalid selection: no same rows or cols");
        }

        boolean consecutiveX=true;
        boolean consecutiveY=true;
        if(sameX){
            int minY=selectedCoords.get(0)[1];
            int maxY=selectedCoords.get(0)[1];

            for(int i=1;i<selectedCoords.size();i++){
                if(selectedCoords.get(i)[1]<minY){
                    minY=selectedCoords.get(i)[1];
                }
            }
            for(int i=1;i<selectedCoords.size();i++) {
                if (selectedCoords.get(i)[1] > maxY) {
                    maxY = selectedCoords.get(i)[1];
                }
            }
            if (selectedCoords.size() == 3) {
                if (maxY - minY != 2) {
                    consecutiveY = false;
                }
            }
            if (selectedCoords.size() == 2) {
                if (maxY - minY != 1) {
                    consecutiveY = false;
                }
            }
            if(!consecutiveY){
                throw new Exception("Invalid selection: No consecutive selection");
            }
        }

        if(sameY){
            int minX=selectedCoords.get(0)[0];
            int maxX=selectedCoords.get(0)[0];

            for(int i=1;i<selectedCoords.size();i++){
                if(selectedCoords.get(i)[1]<minX){
                    minX=selectedCoords.get(i)[1];
                }
            }
            for(int i=1;i<selectedCoords.size();i++) {
                if (selectedCoords.get(i)[1] > maxX) {
                    maxX = selectedCoords.get(i)[1];
                }
            }
            if (selectedCoords.size() == 3) {
                if (maxX - minX != 2) {
                    consecutiveX = false;
                }
            }
            if (selectedCoords.size() == 2) {
                if (maxX - minX != 1) {
                    consecutiveX = false;
                }
            }
            if(!consecutiveX){
                throw new Exception("Invalid selection: No consecutive selection");
            }
        }

        /* For-cycle to analyse values of coordinates: second bond: Items with almost a free side on game board*/
        for (int i = 0; i < selectedCoords.size(); i++) {
            final int FIRST_ROW=0;
            final int LAST_ROW=8;
            final int FIRST_COLUMN=0;
            final int LAST_COLUMN=8;
            int row = selectedCoords.get(i)[0];
            int col = selectedCoords.get(i)[1];

            /* if items are on the edge of the game board they have always almost a free side  */
            if (col == FIRST_COLUMN || col == LAST_COLUMN || row == FIRST_ROW || row == LAST_ROW) {
                continue;
            }
            /* if there are no items in the previous or following column, the second constraint is respected */
            if (gameGrid[row][col - 1].getCategoryType() == null || gameGrid[row][col + 1].getCategoryType() == null) {
                continue;
            }
            /* if there are no items in the previous or following row, the second constraint is respected */
            if (gameGrid[row - 1][col].getCategoryType() == null || gameGrid[row + 1][col].getCategoryType() == null) {
                continue;
            }
            if (gameGrid[row][col - 1].getCategoryType() != null && gameGrid[row][col + 1].getCategoryType() != null &&
                    gameGrid[row - 1][col].getCategoryType() != null || gameGrid[row + 1][col].getCategoryType() != null) {
                throw new InvalidSelectionException("Invalid selection: no free sides");
            }
        }
        /* For-cycle to pick items from the game board and put them in the selectItems list*/
        for (int i = 0; i < selectedCoords.size(); i++) {
            int row = selectedCoords.get(i)[0];
            int col = selectedCoords.get(i)[1];
            selectItems.add(gameGrid[row][col]);
            gameGrid[row][col] = new Item(null);
            validGrid[row][col] = 1;
        }
        setChanged();
        notifyObservers(gameGrid);
    }

    /** method to put Items into personal shelf*/
    public void putItemInShelf(int selectedCol,List<Item> sortedItems) throws ArrayIndexOutOfBoundsException, InvalidNumberOfItemsException{
        Item[][] grid=myShelf.getShelfGrid();
        if (selectedCol >= 5) {
            throw new ArrayIndexOutOfBoundsException("selectedCol must be less than 5");
        }
        /* For-cycle to search the last row available*/
        int lastRow = 0;
        for (int row = 0; row<6; row++) {
            if (grid[row][selectedCol].getCategoryType() ==null ) {
                lastRow = row;
            }
        }
        /* For-cycle to put items into the selected column starting from the last row available*/
        if(sortedItems.size()>3){
            throw new InvalidNumberOfItemsException();
        }
        for (int i = 0; i < sortedItems.size(); i++, lastRow--) {
            grid[lastRow][selectedCol] = sortedItems.get(i);
        }
        this.selectItems = new ArrayList<>();
        setChanged();
        notifyObservers(this.myShelf);
    }

    public void addPoints(int points) {
        this.score += points;
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
    public List<Item> getCopiedItems(){
        List<Item> copiedItems = new ArrayList<>();
        for(int i=0;i<selectItems.size();i++){
            copiedItems.add(selectItems.get(i));
        }
        while(!selectItems.isEmpty()){
            selectItems.remove(0);
        }
        return copiedItems;
    }
    public List<Item> getSelectItems(){return this.selectItems;}
}



