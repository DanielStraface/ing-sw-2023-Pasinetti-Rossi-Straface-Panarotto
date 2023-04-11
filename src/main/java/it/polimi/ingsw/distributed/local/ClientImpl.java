package it.polimi.ingsw.distributed.local;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.ShelfView;
import it.polimi.ingsw.view.TextualUI;

public class ClientImpl implements Client, Runnable {
    TextualUI view = new TextualUI();

    public ClientImpl(Server server) {
        server.register(this);
        this.view.addListener(server);
        //this.view.addListener(server);
        //this.view.addListener((o, arg) -> server.update(this, arg));
    }

    @Override
    public void update(GameBoardView gb) {
        this.view.update(gb);
    }

    @Override
    public void update(GameView game) {
        this.view.update(game);
    }

    @Override
    public void update(Item[][] gameGrid) {
        this.view.update(gameGrid);
    }

    @Override
    public void update(ShelfView shelf) {
        this.view.update(shelf);
    }

    @Override
    public void update(Integer column) {
        this.view.update(column);
    }

    @Override
    public void run() {
        this.view.run();
    }
}