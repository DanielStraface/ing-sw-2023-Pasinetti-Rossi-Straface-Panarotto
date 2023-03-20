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
    private List<Item> pickItems(int[][] selectedcoords, Item[][] gameGrid) throws Exception {
        if (selectedcoords != null) {

            boolean sameX = true;
            boolean sameY = true;
            int x = selectedcoords[0][0];
            int y = selectedcoords[0][1];
            for (int i = 1; i < selectedcoords.length; i++) {
                if (selectedcoords[i][0] != x) {
                    sameX = false;
                    break;
                }
                if (selectedcoords[i][1] != y) {
                    sameY = false;
                    break;
                }
            }
            if (!sameX && !sameY) {
                throw new Exception("Invalid selection: no same rows or cols");
            }

            for (int i = 0; i < selectedcoords.length; i++) {
                int row = selectedcoords[i][0];
                int col = selectedcoords[0][i];

                if (gameGrid[row][col - 1] == null || gameGrid[row][col + 1] == null) {
                    continue;
                }
                if (gameGrid[row - 1][col] == null || gameGrid[row + 1][col] == null) {
                    continue;
                }
                throw new Exception("Invalid selection: no free sides");
            }

            selectItems = new ArrayList<Item>();
            for (int i = 0; i < selectedcoords.length; i++) {
                int row = selectedcoords[i][0];
                int col = selectedcoords[i][1];
                selectItems.add(gameGrid[row][col]);
                gameGrid[row][col] = null;
            }
        }
        return selectItems;
    }

    private void putItemInShelf(Item[] selectedItems, int selectedCol){
        Item[][] grid=myShelf.getShelfGrid();
        if (selectedCol >= 5) {
            throw new IllegalArgumentException("selectedCol must be less than 5");
        }
        int lastRow = -1;
        for (int row = 0; row<6; row++) {
            if (grid[row][selectedCol] == null) {
                lastRow = row;
            }
        }
        for (int i = 0; i < selectedItems.length; i++, lastRow--) {
            grid[lastRow][selectedCol] = selectedItems[i];
        }
    }

    public void setPersonalObjCard(PersonalObjCard card){
        this.myPersonalObjCard = card;
    }

    public void setMyShelf(Shelf shelf){
        this.myShelf = shelf;
    }

    public void addPoints(int points) {
        this.score += points;
    }
}



