package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Player;

public class TurnChecker {

    public boolean manageCheck(Player player){
        boolean isLastTurn;
        commonObjCardCheck();
        personalObjCardCheck();
        isLastTurn = lastTurnCheck();
        refillGameBoardCheck();
        return isLastTurn;
    }

    private void commonObjCardCheck(){}
    private void personalObjCardCheck(){}
    private void refillGameBoardCheck(){}
    private boolean lastTurnCheck(){return false;}

}
