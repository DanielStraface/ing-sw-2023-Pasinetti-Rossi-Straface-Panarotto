package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class PlayerAction {
    private int[][] selectedCoords; //must take from view
    public void playerGameActionExecutor(Game game){
        try{
            //at this moment pickItems throws an exception. Manage selectedCoords initialization
            game.getCurrentPlayer().pickItems(null, game.getGameboard().getGameGrid(), game.getValidGrid());
            List<Item> selectedItems = game.getCurrentPlayer().getSelectItems();
            //selectedItems send to view for resorted. Receive selectedItems resorted from view
            //There will be from view a method that sorts the items picked by the player choice
            game.getCurrentPlayer().putItemInShelf(0);
        } catch (Exception e) {
            System.err.println("Error: invalid selection of items");
        }
        /*
        * 1) Selezione di tessere oggetto dalla plancia e immissione della stessa nella libreria del giocatore

        game.getPlayers().get(i).playerChoice(coordinates, game.getGameBoard().getGameGrid(), game.getGameBoard().getValidGrid(), selectedcol);

        EFFETTI COLLATERALI E VARI A FINE TURNO DEL GIOCATORE
        2) Metodo che controlla che dalla gameboard si possono prendere solo tessere singole
            Nel caso sia così, allora
                game.refillGameBoard();

        3) Controllo di carta obiettivo personale
        if(game.getPlayers().get(i).getMyPersonalObjCard().goalReached()){
            addedPoints; //si aggiungono i punti
        }

        4) Controllo della carta obiettivo comune
        game.getCommonObjCards.get(j).doCheck(game.getPlayers().get(i))

        5) Controllo di fine parta (più punto finale)
            Metodo che fa logiamente il controllo, nesessita boolean result = game.getPlayers().get(i).getMyShelf().ifFull()

        6) Passaggio di fine turno
            Metodo che necessita di game.getPlayers(), game.getCurrentPlayer()
                        poi chiama game.setCurrentPlayer(that_player);*/
    }
}
