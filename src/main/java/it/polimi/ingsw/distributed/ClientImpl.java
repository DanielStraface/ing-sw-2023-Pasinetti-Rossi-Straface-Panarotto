package it.polimi.ingsw.distributed;

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

    public ClientImpl(Server server, String nickname) throws RemoteException {
        super();
        this.nickname = nickname;
        server.register(this, nickname);
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
        this.view.addListener(server); //add the match server as this client view observer
        //per Damiani e\' diverso
    }

    @Override
    public void update(GameBoardView gb) {this.view.update(gb);}

    @Override
    public void update(GameView game, int clientID) throws RemoteException {
        this.view.update(game);
        this.view.run(this);
    }

    @Override
    public void update(Item[][] gameGrid) {
        this.view.update(gameGrid);
        try {
            this.view.gameActionOnShelf();
        } catch (RemoteException e) {
            System.err.println("Error while invoking gameActionOnShelf in ClientImpl: " + e.getMessage());
        }
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
            this.view.update(0);
        } else {
            this.view.update(msg);
            if(msg.contains("Extraction")){
                System.out.println("I am " + this.nickname + " @ " + this.clientID);
            }
        }
    }

    @Override
    public void update(int clientID) throws RemoteException {
        this.clientID = clientID;
    }

    @Override
    public void run() {
        //empty
    }

    @Override
    public String getNickname(){return this.nickname;}

    @Override
    public int getClientID() throws RemoteException {return this.clientID;}
}