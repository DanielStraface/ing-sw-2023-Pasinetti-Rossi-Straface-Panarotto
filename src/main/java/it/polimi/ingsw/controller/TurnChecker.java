package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.comcard.CommonObjCard;
import it.polimi.ingsw.model.personcard.PersonalObjCard;

public class TurnChecker {

    private CommonObjCard commonObjCard;
    private PersonalObjCard personalObjCard;

    public boolean manageCheck(Player player, Game game){
        boolean isLastTurn;
        commonObjCardCheck(player, game);
        isLastTurn = lastTurnCheck();
        refillGameBoardCheck();
        return isLastTurn;
    }

    private void commonObjCardCheck(Player player,Game game){
        for(int i=0; i<game.getCommonObjCard().size(); i++){
            commonObjCard = game.getCommonObjCard().get(i);
            commonObjCard.doCheck(player);
        }
    }

    private void refillGameBoardCheck(){}
    private boolean lastTurnCheck(){return false;}

}
