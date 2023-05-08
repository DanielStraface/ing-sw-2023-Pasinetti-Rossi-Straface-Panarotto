package it.polimi.ingsw.distributed;

import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.ShelfView;
import it.polimi.ingsw.view.TextualUI;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements Client, Serializable {
    transient TextualUI view = new TextualUI();
    transient String nickname;
    private int clientID;

    public ClientImpl(Server server, String nickname) throws RemoteException {
        super();
        this.nickname = nickname;
        server.register(this, nickname);
        this.view.addListener(server);
        //server.startGame();
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
    public void update(GameView game) throws RemoteException {
        if(game.getGameOverFinalMessage() != null){
            this.view.update(game.getGameOverFinalMessage());

        }
        if(game.getCurrentPlayer().getClientID() == this.clientID){
            this.view.run(game);
        } else {
            if(game.getPrevClientID() == this.clientID){
                ShelfView sh = game.getPlayers().stream().filter(p -> p.getClientID() == game.getPrevClientID())
                        .findFirst()
                        .get().getMyShelf();
                this.view.update("Your turn is finished! Please wait for the other players turn");
                this.view.update(sh);
            }
            this.view.update(game.getGameBoard());
            this.view.update(game.getCurrentPlayer().getNickname() + " is playing.");
        }
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
        } else this.view.update(msg);
    }

    @Override
    public void update(int clientID) throws RemoteException {
        this.clientID = clientID;
        this.view.setReferenceClient(this);
    }

    @Override
    public String getNickname(){return this.nickname;}

    @Override
    public int getClientID() throws RemoteException {return this.clientID;}
}