package it.polimi.ingsw.distributed;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.socket.middleware.ClientSkeleton;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.AppServerImpl;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerImpl extends UnicastRemoteObject implements Server {
    public int connectedClient;
    private Controller controller;
    private Game game = null;
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

    public int getPlayersGameNumber(){return this.game.getPlayers().size();}
    public boolean getGameOver(){return this.controller.getGameOver();}

    @Override
    public void startGame() throws RemoteException{
        if(this.controller.getViews().size() == this.controller.getGame().getPlayers().size()){
            this.controller.setMatchID(AppServerImpl.getMatchID(this));
            for(Client client : this.controller.getViews()) {
                client.update("The match is starting...Extraction of the first player is running");
            }
            this.controller.chooseFirstPlayer();
        } else {
            int temp = 0;
            for(int i=0;i<this.toConnect.length;i++){
                if(this.toConnect[i]==false)
                    temp++;
            }
            for(Client client : this.controller.getViews()){
                 client.update("Waiting for players\nSearching for " + temp + " other players");
            }
        }
    }

    @Override
    public void register(Client client, int numOfPlayers, String nickname) throws RemoteException {
        boolean fromSocket = false;
        if(nickname.contains("%%%")){
            fromSocket = true;
            nickname = nickname.substring(0, nickname.length() - 3);
        }
        System.out.println("The server is " + this.toString());
        if(numOfPlayers == 0){
            if(this.game == null){
                client.update("There are no match at this moment for you..\nPlease, reboot application and" +
                        " choose 'to Start a new game'. Note: the server will not respond you more!");
                System.err.println("No match found\nSomeone must create a new one");
                System.exit(3);
            }
            if(this.controller.getViews().size() == this.game.getPlayers().size()){
                System.err.println("The lobby is full...");
                return;
            }
            this.controller.addClientView(client);
            this.game.addListener(client);
        } else {
            try{
                this.game = new Game(numOfPlayers);
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
            this.controller = new Controller(game, client);
            this.game.addListener(client);
            this.toConnect = new boolean[this.game.getPlayers().size()];
        }
        for(int i=0;i<toConnect.length;i++){
            if(this.toConnect[i] == false){
                this.toConnect[i] = true;
                this.game.getPlayers().get(i).addListenerForPlayer(client);
                this.game.getPlayers().get(i).setNicknameAndClientID(nickname, i*10);
                client.update(i*10);
                break;
            }
        }
        System.out.println("Register client " + client + "\nwith clientID := " + client.getClientID() +
                "for a " + this.game.getPlayers().size() + " players match");
        if(fromSocket == true){
            this.startGame();
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