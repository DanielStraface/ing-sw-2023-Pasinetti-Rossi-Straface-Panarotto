package it.polimi.ingsw.distributed.rmi;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements Server {
    private Controller controller;
    private Game game;
    private static final int PLAYERS_NUMB = 3;

    public ServerImpl() throws RemoteException {
        super();
    }

    public ServerImpl(int port) throws RemoteException {
        super(port);
    }

    public ServerImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    @Override
    public void startGame() throws RemoteException{
        this.controller.chooseFirstPlayer();
    }

    @Override
    public void register(Client client) throws RemoteException {
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

    public void register(Client client, int numOfPlayers) throws RemoteException {
        try{
            this.game = new Game(numOfPlayers);
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
    public void update(Client client, Integer column) throws RemoteException {
        this.controller.update(client, column);
    }

    @Override
    public void update(Client client, String nickname) throws RemoteException {
        this.controller.update(client, nickname);
    }

    @Override
    public void update(Client client, List<int[]> coords) throws RemoteException {
        this.controller.update(client, coords);
    }
}