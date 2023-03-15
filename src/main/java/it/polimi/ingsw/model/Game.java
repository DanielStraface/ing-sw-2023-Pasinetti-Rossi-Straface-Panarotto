package it.polimi.ingsw.model;

import java.util.List;

class Game {

    private int playersNumber;
    private List<Player> players;
    private GameBoard gameboard;
    private int[][] validGrid;
    private Bag bag;
    private CommonObjCard commonObjCard;
    private Player currentPlayer;

    public Game (int playersNumber){this.playersNumber = playersNumber;}
    public List<Player> getPlayers(){return players;}
    public GameBoard getGameboard(){return gameboard;}
    public CommonObjCard getCommonObjCard(){return commonObjCard;}
    public Bag getBag(){return bag;}
    public Player getCurrentPlayer(){return currentPlayer;}


}