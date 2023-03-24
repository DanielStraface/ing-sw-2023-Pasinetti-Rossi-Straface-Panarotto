package it.polimi.ingsw.model;

import it.polimi.ingsw.model.personcard.PersonalObjCard;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String nickname;    /** has to be unique */

    private int score;
    private Shelf myShelf;
    private PersonalObjCard myPersonalObjCard;
    private boolean isFirstPlayer;
    private int clientID;
    private List<Item> selectItems;

    /** constructor for Player class */
    public Player(String nickname, int clientID, boolean isFirstPlayer) throws Exception {
        this.nickname = nickname;
        this.clientID = clientID;
        this.score = 0;
        this.isFirstPlayer = false;
        this.myShelf = new Shelf();
    }

    public void playerChoice(int[][] selectedCoords, Item[][] gameGrid, int[][] validGrid, int selectedCol)
            throws Exception {
        pickItems(selectedCoords, gameGrid, validGrid);
        putItemInShelf(selectedCol);
    }

    /** method to pick Items from the game board*/
    private void pickItems(int[][] selectedCoords, Item[][] gameGrid, int[][] validGrid) throws Exception {
        if (selectedCoords != null) {
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

                    if(col==0||col==8||row==0||row==8){
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
        }

            selectItems = new ArrayList<Item>();
            /* For-cycle to pick items from the game board and put them in the selectItems list*/
            for (int i = 0; i < selectedCoords.length; i++) {
                int row = selectedCoords[i][0];
                int col = selectedCoords[i][1];
                selectItems.add(gameGrid[row][col]);
                gameGrid[row][col] = null;
                validGrid[row][col] = 1;
            }
    }

    /** method to put Items into personal shelf*/
    private void putItemInShelf(int selectedCol){
        Item[][] grid=myShelf.getShelfGrid();
        if (selectedCol >= 5) {
            throw new IllegalArgumentException("selectedCol must be less than 5");
        }
        /* For-cycle to search the last row available*/
        int lastRow = -1;
        for (int row = 0; row<6; row++) {
            if (grid[row][selectedCol] == null) {
                lastRow = row;
            }
        }
        /* For-cycle to put items into the selected column starting from the last row available*/
        for (int i = 0; i < selectItems.size(); i++, lastRow--) {
            grid[lastRow][selectedCol] = selectItems.get(i);
        }
    }

    public Object addPoints(int points) {
        this.score += points;
        return null;
    }

    /* set methods */
    public void setPersonalObjCard(PersonalObjCard card){
        this.myPersonalObjCard = card;
    }
    public Object setMyShelf(Shelf shelf){
        this.myShelf = shelf;
        return null;
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
    public boolean getFirstPlayer(){
        return this.isFirstPlayer;
    }
}



