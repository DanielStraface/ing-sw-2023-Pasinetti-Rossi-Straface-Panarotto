package it.polimi.ingsw.distributed;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.AppServerImpl;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        if(this.controller.getClients().size() == this.controller.getGame().getPlayers().size()){
            this.controller.getClients().get(this.controller.getClients().size() - 1).update("Joining a lobby...");
            this.controller.setMatchID(AppServerImpl.getMatchID(this));
            for(Client client : this.controller.getClients()) {
                client.update("Correct number of players reached!" +
                        "\nThe match is starting...Extraction of the first player is running");
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.err.println("Error during the delay of one seconds...ignoring");
            }
            this.controller.chooseFirstPlayer();
        } else {
            int temp = 0;
            for (boolean b : this.toConnect) {
                if (!b)
                    temp++;
            }
            for(Client client : this.controller.getClients()){
                 client.update("Lobby joined but the match lobby is not full. Please wait..." +
                         "\nSearching for " + temp + " other players");
            }
        }
    }

    @Override
    public void register(Client client, int numOfPlayers, String nickname) throws RemoteException {
        if(numOfPlayers != 0){
            try{
                this.game = new Game(numOfPlayers);
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
            this.controller = new Controller(game, client);
            this.game.addListener(client);
            this.toConnect = new boolean[this.game.getPlayers().size()];
        } else {
            /* if there are no existing match will be displayed an error */
            if(this.game == null){
                System.err.println("No match found\nSomeone must create a new one");
                return;
            }
            /*Joining to a full match will be displayed an error */
            if(this.controller.getClients().size() == this.game.getPlayers().size()){
                System.err.println("The lobby is full...");
                return;
            }
            this.controller.addClient(client);
            this.game.addListener(client);
        }
        for(int i=0;i<toConnect.length;i++){
            if(!this.toConnect[i]){
                this.toConnect[i] = true;
                this.game.getPlayers().get(i).addListenerForPlayer(client);
                this.game.getPlayers().get(i).setNicknameAndClientID(nickname, i*10);
                client.update(i*10);
                break;
            }
        }
        System.out.println("Register client " + client + "\nwith clientID := " + client.getClientID() +
                "for a " + this.game.getPlayers().size() + " players match");
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