package it.polimi.ingsw.distributed.local;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;

import java.util.List;

public class ServerImpl implements Server {
    private Controller controller;
    private Game game;
    private static final int PLAYERS_NUMB = 2;

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
        this.controller.chooseFirstPlayer();
        //this.game.addListener(o -> client.update(new GameView(o)));
        /*this.game.addListener((o, arg) -> {});
        this.game.addListener((o, arg) -> client.update(this, arg));*/
        //this.game.addListener((GameView arg) -> client.update());

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