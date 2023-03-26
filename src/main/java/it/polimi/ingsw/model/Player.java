package it.polimi.ingsw.model;

import it.polimi.ingsw.model.personcard.PersonalObjCard;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String nickname;    /** has to be unique */

    private int score;
    private int clientID;
    private Shelf myShelf;
    private PersonalObjCard myPersonalObjCard;
    private List<Item> selectItems;
    private boolean isFirstPlayer;

    /** constructor for Player class */
    public Player(String nickname, int clientID) throws Exception {
        this.nickname = nickname;
        this.clientID = clientID;
        this.score = 0;
        this.isFirstPlayer = false;
        this.myShelf = new Shelf();
        this.selectItems=new ArrayList<>();
    }

    /** method to pick Items from the game board*/
    public void pickItems(int[][] selectedCoords,Item[][] gameGrid, int[][] validGrid) throws Exception {
        boolean sameX = true;
        boolean sameY = true;
        boolean consecutiveX = true;
        boolean consecutiveY = true;
        int x = selectedCoords[0][0];
        int y = selectedCoords[0][1];
        /* For-cycle to analyse values of coordinates: first bond: Items from the same row or column*/
        for (int i = 1; i < selectedCoords.length; i++) {
            /* If all coordinates haven't the same x: items will not be picked from the same row */
            if (selectedCoords[i][0] != x) {
                sameX = false;
                        }
            /* If all coordinates haven't the same y: items will not be picked from the same column */
            if (selectedCoords[i][1] != y) {
                sameY = false;
            }
        }
        /* If all coordinates haven't the same x or the same y: items can not be picked from the game board */
        if (!sameX && !sameY) {
            throw new Exception("Invalid selection: no same rows or cols");
        }
        for (int i = 1; i < selectedCoords.length; i++) {
            if (selectedCoords[i][0] != selectedCoords[i - 1][0] + 1) {
                consecutiveX = false;
            }
            if (selectedCoords[i][1] != selectedCoords[i - 1][1] + 1) {
                consecutiveY = false;
            }
        }
        if (!consecutiveX && !consecutiveY) {
            throw new Exception("Invalid selection: No consecutive selection");
        }

        /* For-cycle to analyse values of coordinates: second bond: Items with almost a free side on game board*/
        for (int i = 0; i < selectedCoords.length; i++) {
            int row = selectedCoords[i][0];
            int col = selectedCoords[i][1];

            if (col == 0 || col == 8 || row == 0 || row == 8) {
                continue;
            }
            /* if there are no items in the previous or following column, the second constraint is respected */
            if (gameGrid[row][col - 1] == null || gameGrid[row][col + 1] == null) {
                continue;
            }
            /* if there are no items in the previous or following row, the second constraint is respected */
            if (gameGrid[row - 1][col] == null || gameGrid[row + 1][col] == null) {
                continue;
            }
            if (gameGrid[row][col - 1] != null && gameGrid[row][col + 1] != null &&
                    gameGrid[row - 1][col] != null || gameGrid[row + 1][col] != null) {
                throw new Exception("Invalid selection: no free sides");
            }
        }
        /* For-cycle to pick items from the game board and put them in the selectItems list*/
        for (int i = 0; i < selectedCoords.length; i++) {
            int row = selectedCoords[i][0];
            int col = selectedCoords[i][1];
            selectItems.add(gameGrid[row][col]);
            gameGrid[row][col] = new Item(null);
            validGrid[row][col] = 1;
        }
    }

    /** method to put Items into personal shelf*/
    public void putItemInShelf(int selectedCol,Item[] sortedItems) throws Exception{
        Item[][] grid=myShelf.getShelfGrid();
        if (selectedCol >= 5) {
            throw new Exception("selectedCol must be less than 5");
        }
        /* For-cycle to search the last row available*/
        int lastRow = -1;
        for (int row = 0; row<6; row++) {
            if (grid[row][selectedCol] == null) {
                lastRow = row;
            }
        }
        /* For-cycle to put items into the selected column starting from the last row available*/
        for (int i = 0; i < sortedItems.length; i++, lastRow--) {
            grid[lastRow][selectedCol] = sortedItems[i];
        }
    }

    public void addPoints(int points) {
        this.score += points;
    }

    /* set methods */
    public void setPersonalObjCard(PersonalObjCard card){
        this.myPersonalObjCard = card;
    }
    public void setMyShelf(Shelf shelf){this.myShelf = shelf;}

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
    public boolean setIsFirstPlayer() {
        return this.isFirstPlayer=true;
    }
    public List<Item> getSelectItems(){return this.selectItems;}
}




