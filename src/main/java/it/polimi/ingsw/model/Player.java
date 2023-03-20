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
    }

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

    /** method to pick Items from the game board*/
    private List<Item> pickItems(int[][] selectedcoords, Item[][] gameGrid) throws Exception {
        if (selectedcoords != null) {
            boolean sameX = true;
            boolean sameY = true;
            int x = selectedcoords[0][0];
            int y = selectedcoords[0][1];
            /* For-cycle to analyse values of coordinates: first bond: Items from the same row or column*/
            for (int i = 1; i < selectedcoords.length; i++) {
                /* If all coordinates haven't the same x: items will not be picked from the same row */
                if (selectedcoords[i][0] != x) {
                    sameX = false;
                    break;
                }
                /* If all coordinates haven't the same y: items will not be picked from the same column */
                if (selectedcoords[i][1] != y) {
                    sameY = false;
                    break;
                }
            }
            /* If all coordinates haven't the same x or the same y: items can not be picked from the game board */
            if (!sameX && !sameY) {
                throw new Exception("Invalid selection: no same rows or cols");
            }
            /* For-cycle to analyse values of coordinates: second bond: Items with almost a free side on game board*/
            for (int i = 0; i < selectedcoords.length; i++) {
                int row = selectedcoords[i][0];
                int col = selectedcoords[0][i];

                /* if there are no items in the previous or following column, the second constraint is respected */
                if (gameGrid[row][col - 1] == null || gameGrid[row][col + 1] == null) {
                    continue;
                }
                /* if there are no items in the previous or following row, the second constraint is respected */
                if (gameGrid[row - 1][col] == null || gameGrid[row + 1][col] == null) {
                    continue;
                }
                throw new Exception("Invalid selection: no free sides");
            }

            selectItems = new ArrayList<Item>();
            /* For-cycle to pick items from the game board and put them in the selectItems list*/
            for (int i = 0; i < selectedcoords.length; i++) {
                int row = selectedcoords[i][0];
                int col = selectedcoords[i][1];
                selectItems.add(gameGrid[row][col]);
                gameGrid[row][col] = null;
            }
        }
        return selectItems;
    }
    /** method to put Items into personal shelf*/
    private void putItemInShelf(Item[] selectItems, int selectedCol){
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
        for (int i = 0; i < selectItems.length; i++, lastRow--) {
            grid[lastRow][selectedCol] = selectItems[i];
        }
    }

    public void setPersonalObjCard(PersonalObjCard card){
        this.myPersonalObjCard = card;
    }

    public void setMyShelf(Shelf shelf){
        this.myShelf = shelf;
    }
}



