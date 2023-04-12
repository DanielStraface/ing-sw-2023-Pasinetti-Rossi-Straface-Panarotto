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
    private Game game = null;
    private static final int PLAYERS_NUMB = 2;
    private boolean[] toConnect;

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
    public void register(Client client, String nickname) throws RemoteException {
        if(this.game == null){
            System.err.println("No match found\nClosing...");
            System.exit(3);
        }
        if(this.controller.getViews().size() == this.game.getPlayers().size()){
            System.err.println("The lobby is full...");
            return;
        }
        //this.controller = new Controller(game, client);
        this.controller.addClientView(client);
        this.game.addListener(client);
        for(int i=0;i<toConnect.length;i++){
            if(this.toConnect[i] == false){
                this.toConnect[i] = true;
                this.game.getPlayers().get(i).addListenerForPlayer(client);
                this.game.getPlayers().get(i).setNicknameAndClientID(nickname, i*10);
                break;
            }
        }
        /*for(int i=0;i<this.toConnect.length;i++){
            if(this.toConnect[i] == false)
                return false;
        }
        notifyAll();
        return true;*/
    }

    public void register(Client client, int numOfPlayers, String nickname) throws RemoteException {
        try{
            this.game = new Game(numOfPlayers);
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
        this.controller = new Controller(game, client);
        this.game.addListener(client);
        this.toConnect = new boolean[this.game.getPlayers().size()];
        /*for(int i=0;i<toConnect.length;i++){
            if(this.toConnect[i] == false){
                this.toConnect[i] = true;
                this.game.getPlayers().get(i).addListenerForPlayer(client);
                this.game.getPlayers().get(i).setNicknameAndClientID(nickname, i*10);
                break;
            }
        }*/
        for(Player player : this.game.getPlayers()){
            player.addListenerForPlayer(client);
        }
        /*for(int i=0;i<this.toConnect.length;i++){
            if(this.toConnect[i] == false)
                return false;
        }
        return true;*/
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