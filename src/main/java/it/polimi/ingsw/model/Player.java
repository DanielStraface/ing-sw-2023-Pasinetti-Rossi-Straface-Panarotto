package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Shelf;

import java.util.ArrayList;
import java.util.List;

class Player {
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

        public String getNickname(){
            return this.nickname;
        }
        public int getClientID(){
            return this.clientID;
        }
        public int getScore(){
            return score;
        }
        public PersonalObjCard getMyPersonalOBjCard(){
            return myPersonalObjCard;
        }
        public Shelf getMyShelf(){
            return myShelf;
        }
        public boolean getFirstPlayer(){
            return this.isFirstPlayer;
        }
        private List<Item> pickItems(Coordinate[] selectedcoords, Item[][] gameGrid) throws Exception {
            if (selectedcoords== null || selectedcoords.length==0){
                throw new Exception("No item selected");
            }
            int x= selectedcoords[0].getX();
            int y= selectedcoords[0].getY();

            for(int i=1; i<selectedcoords.length; i++){
                if(selectedcoords[i].getX()!=x && selectedcoords[i].getY()!=y) {
                    throw new Exception("Invalid selection:different cols and rows");
                }
            }
            for (int i = 0; i < selectedcoords.length; i++) {
                Coordinate coord = selectedcoords[i];
                int row = coord.getX();
                int col = coord.getY();

                if (gameGrid[row][col - 1] == null || gameGrid[row][col + 1] == null) {
                    continue;
                }
                if (gameGrid[row - 1][col] == null || gameGrid[row + 1][col] == null) {
                    continue;
                }
                throw new Exception("Invalid selection: no free sides");
            }
            selectItems = new ArrayList<Item>();
            for (int i=0; i<selectedcoords.length; i++) {
                int row = selectedcoords[i].getX();
                int col = selectedcoords[i].getY();

                selectItems[i]= gameGrid[row][col];
                gameGrid[row][col]=null;
            }
            return selectItems;
        }
        private void putItemInShelf(Item selectedItem, int selectedCol, Shelf[][] myShelf){
            if (selectedCol >= 5) {
                throw new IllegalArgumentException("selectedCol must be less than 5");
            }
            int lastRow = -1;
            for (int row = 0; row<6; row++) {
                if (myShelf[row][selectedCol] == null) {
                    lastRow = row;
                }
            }
            if (lastRow == -1) {
                throw new IllegalStateException("Column " + selectedCol + " is full");
            }
            myShelf[lastRow][selectedCol] = selectedItem;

            return myShelf;
            }
        }
    }
}



