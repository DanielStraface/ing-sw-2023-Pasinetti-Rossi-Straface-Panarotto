package it.polimi.ingsw.controller;

public class TurnHandler {
    private TurnChecker turnChecker;
    public TurnHandler(){
        turnChecker= new TurnChecker();
    }

    public void nextTurn(){};
    public void manageTurn(){};
}
