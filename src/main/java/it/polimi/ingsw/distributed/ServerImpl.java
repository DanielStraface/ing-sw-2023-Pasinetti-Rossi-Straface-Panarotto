package it.polimi.ingsw.distributed;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.AppServer;
import it.polimi.ingsw.server.AppServerImpl;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements Server {
    private static final int NO_PLAYER = 0;
    public int connectedClient;
    private Controller controller;
    private Game game = null;
    private boolean[] toConnect;

    public ServerImpl(AppServer.typeOfMatch numOfPlayers) throws RemoteException {
        super();
        try{
            this.game = new Game(numOfPlayers.ordinal() + 1); //create the game model for this numOfPlayerMatch
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
        this.controller = new Controller(game);
        this.toConnect = new boolean[this.game.getPlayers().size()];
    }

    public ServerImpl(int port) throws RemoteException {
        super(port);
    }

    public ServerImpl(int port, RMIClientSocketFactory csf, RMIServerSocketFactory ssf) throws RemoteException {
        super(port, csf, ssf);
    }

    public int getPlayersGameNumber(){
        return this.game.getPlayersNumber();
    }
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
            System.out.println("Match of " + this.game.getPlayersNumber());
            System.out.println("Number of clients: " + this.controller.getClients().size());
            this.controller.getGame().getPlayers().forEach(c -> {
                System.out.println(
                        c.getNickname() + " @number: " + c.getClientID());
            });
            this.controller.getClients().forEach(c -> {
                try {
                    System.out.println("clientID := " + c.getClientID());
                } catch (RemoteException e) {
                    System.err.println("KEK");
                }
            });
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
    public void register(Client client, String nickname) throws RemoteException {
        this.controller.addClient(client); //add this client in the client list of controller
        this.game.addListener(client); // add this client as model listener
        for(int i=0;i<toConnect.length;i++){
            if(!this.toConnect[i]){
                this.toConnect[i] = true;
                //this.game.getPlayers().get(i).addListenerForPlayer(client);
                this.game.getPlayers().get(i).setNicknameAndClientID(nickname, i*10);
                client.update(i*10);
                break;
            }
        }
        System.out.println("Register client " + client + "\nwith clientID := " + client.getClientID() +
                "for a " + this.game.getPlayers().size() + " players match");
        this.startGame();
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