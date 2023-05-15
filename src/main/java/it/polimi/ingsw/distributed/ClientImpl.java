package it.polimi.ingsw.distributed;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;
import it.polimi.ingsw.view.GUI;
import it.polimi.ingsw.view.TextualUI;
import it.polimi.ingsw.view.UI;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

public class ClientImpl extends UnicastRemoteObject implements Client, Serializable {
    public enum ClientState {
        LAUNCH, WAITING_IN_LOBBY, PLAYING, GAMEOVER
    }
    private transient ClientState clientState;
    transient UI view;
    transient String nickname;
    private int clientID;

    public ClientImpl(Server server, String nickname, int guiChoice) throws RemoteException {
        super();
        this.clientState = ClientState.LAUNCH;
        this.nickname = nickname;
        if(guiChoice == 1) this.view = new TextualUI();
        else if(guiChoice == 2) this.view = new GUI();
        server.register(this, nickname);
        this.view.addListener(server);
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
        ShelfView sh;
        if(game.getGameOverFinalMessage() != null){
            this.view.update("THE MATCH IS FINISHED, THE GAMEOVER IS REACHED!\n" +
                    "The final game board is reported below:");
            this.view.update(game.getGameBoard());
            this.view.update("Your final shelf is reported below: ");
            sh = game.getPlayers().stream()
                    .filter(p -> {
                        try {
                            return p.getClientID() == this.getClientID();
                        } catch (RemoteException e) {
                            System.err.println("Cannot obtain the clientID from this Client");
                        }
                        return false;
                    })
                    .findFirst()
                    .get().getMyShelf();
            this.view.update(sh);
            this.view.update(game.getGameOverFinalMessage());
            this.clientState = ClientState.GAMEOVER;
            return;
        }
        if(this.getClientState() != ClientState.PLAYING) this.clientState = ClientState.PLAYING;
        if(game.getCurrentPlayer().getClientID() == this.clientID){
            this.view.run(game);
        } else {
            if(game.getPrevClientID() == this.clientID){
                sh = game.getPlayers().stream().filter(p -> p.getClientID() == game.getPrevClientID())
                        .findFirst()
                        .get().getMyShelf();
                this.view.update("Your turn is finished! Please wait for the other players turn");
                this.view.update(sh);
            }
            PlayerView ply = game.getPlayers().stream()
                    .filter(p -> {
                        try {
                            return p.getClientID() == this.getClientID();
                        } catch (RemoteException e) {
                            System.err.println("Cannot obtain the clientID from this Client");
                        }
                        return false;
                    })
                    .findFirst()
                    .get();
            this.view.displayInfo(game, ply);
            this.view.update(game.getGameBoard());
            this.view.update(ply.getMyShelf());
            this.view.update(game.getCurrentPlayer().getNickname() + " is playing.");
        }
    }

    @Override
    public void update(String msg) throws RemoteException {
        this.view.update(msg);
    }

    @Override
    public void update(int clientID) throws RemoteException {
        this.clientID = clientID;
        this.view.setReferenceClient(this);
        this.clientState = ClientState.WAITING_IN_LOBBY;
    }

    @Override
    public String getNickname(){return this.nickname;}

    @Override
    public int getClientID() throws RemoteException {return this.clientID;}

    public ClientState getClientState(){return this.clientState;}
}