package it.polimi.ingsw.distributed;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.ShelfView;
import it.polimi.ingsw.view.TextualUI;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements Client, Serializable, Runnable {
    transient TextualUI view = new TextualUI();
    transient String nickname;
    private int clientID;

    public ClientImpl(Server server, Integer decision, String nickname) throws RemoteException {
        super();
        String temp;
        if(server instanceof ServerStub){
            temp = nickname + "%%%";
        } else {
            temp = nickname;
        }
        this.nickname = nickname;
        server.register(this, decision.intValue(), temp);
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
            this.view.update(game.getGameBoard().getGameGrid());
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
        this.view.update(column);
    }

    @Override
    public void update(String msg) throws RemoteException {
        if(msg.contains("%")){
            msg = msg.substring(1);
            this.view.update(msg);
            this.view.update("FINE PARTITA");
            System.exit(100);
        }
        if(msg.equals("WRONG_COL")){
            this.view.update("Error, invalid column selection. Please choose another one.");
            this.view.askColumn("Repeat");
        } else {
            this.view.update(msg);
        }

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

    @Override
    public int getClientID() throws RemoteException {return this.clientID;}
}