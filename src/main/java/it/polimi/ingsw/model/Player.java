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

    public Player(String nickname, int clientID, boolean isFirstPlayer) {
        this.nickname = nickname;
        this.clientID = clientID;
        this.isFirstPlayer = false;
        selectItems = new ArrayList<>();

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
        private List<Item> pickItems(){
        }
        private void putItemInShelf(){
        }
    }

    /**
     * Method setPersonalObjCard recives a personalObjCard as a parameter and use it as the attribute myPersonalObjCard
     */
    public void setPersonalObjCard(PersonalObjCard card){
        this.myPersonalObjCard = card;
        return;
    }
}



