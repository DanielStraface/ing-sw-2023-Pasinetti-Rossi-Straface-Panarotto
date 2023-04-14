package it.polimi.ingsw.distributed.rmi;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.ShelfView;
import it.polimi.ingsw.view.TextualUI;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements Client, Runnable {
    TextualUI view = new TextualUI();
    String nickname;
    private int clientID;

    public ClientImpl(Server server, String nickname) throws RemoteException {
        super();
        this.nickname = nickname;
        server.register(this, nickname);
        initialize(server);
        server.startGame();
    }

    public ClientImpl(Server server, int decision, String nickname) throws RemoteException {
        super();
        this.nickname = nickname;
        server.register(this, decision, nickname);
        initialize(server);
        server.startGame();
    }

    public ClientImpl(Server server, int port) throws RemoteException {
        super(port);
        initialize(server);
    }

    public ClientImpl(Server server, int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf)
            throws RemoteException {
        super(port, csf, ssf);
        initialize(server);
    }

    private void initialize(Server server) throws RemoteException{
        this.view.addListener(server);
        //per Damiani e\' diverso
    }

    @Override
    public void update(GameBoardView gb) {
        this.view.update(gb);
    }

    @Override
    public void update(GameView game, int clientID) throws RemoteException{
        if(clientID == this.clientID){
            this.view.update(game);
            this.view.run(this);
        } else {
            this.view.update(game.getGameBoard());
        }
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
        if(column == 10)
            System.exit(100);
        this.view.update(column);
    }

    @Override
    public void update(String msg) throws RemoteException {
        this.view.update(msg);
    }

    @Override
    public void update(int clientID) throws RemoteException {
        this.clientID = clientID;
    }

    @Override
    public void run() {
        try{
            this.view.run(this);
        } catch (RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public String getNickname(){return this.nickname;}
}