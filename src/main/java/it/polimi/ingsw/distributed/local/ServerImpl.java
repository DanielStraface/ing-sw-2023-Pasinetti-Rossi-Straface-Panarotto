package it.polimi.ingsw.distributed.local;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class ServerImpl implements Server {
    private Controller controller;
    private Game game;
    private static final int PLAYERS_NUMB = 3;

    @Override
    public void startGame(){
        this.controller.chooseFirstPlayer();
    }

    @Override
    public void register(Client client) {
        try{
            this.game = new Game(PLAYERS_NUMB);
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
        this.controller = new Controller(game, client);
        this.game.addListener(client);
        for(Player player : this.game.getPlayers()){
            player.addListenerForPlayer(client);
        }
    }

    @Override
    public void update(Client client, Integer column) {
        this.controller.update(client, column);
    }

    @Override
    public void update(Client client, String nickname) {
        this.controller.update(client, nickname);
    }

    @Override
    public void update(Client client, List<int[]> coords) {
        this.controller.update(client, coords);
    }
}