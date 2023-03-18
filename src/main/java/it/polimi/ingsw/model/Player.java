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

    public Player(String nickname, int clientID, boolean isFirstPlayer) {
        this.nickname = nickname;
        this.clientID = clientID;
        this.isFirstPlayer = false;
        selectItems = new ArrayList<>();

        /*public String getNickname(){
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

        public boolean getFirstPlayer(){
            return this.isFirstPlayer;
        }
        private List<Item> pickItems(){
        }
        private void putItemInShelf(){
        }*/
    }

    public Shelf getMyShelf(){
        return myShelf;
    }

    /**
     * Method setPersonalObjCard receives a personalObjCard as a parameter and use it as the attribute myPersonalObjCard
     */
    public void setPersonalObjCard(PersonalObjCard card){
        this.myPersonalObjCard = card;
        return;
    }
}



