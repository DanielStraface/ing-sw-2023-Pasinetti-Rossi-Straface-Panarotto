package it.polimi.ingsw.distributed;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.listeners.MatchLog;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.server.AppServer;
import it.polimi.ingsw.server.AppServerImpl;

import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ServerImpl extends UnicastRemoteObject implements Server {
    public int connectedClient;
    private Controller controller;
    private Game game = null;
    private boolean[] toConnect;

    public ServerImpl(AppServer.typeOfMatch numOfPlayers) throws RemoteException {
        super();
        try{
            System.out.println("QUI");
            this.game = new Game(numOfPlayers.ordinal() + 1); //create the game model for this numOfPlayerMatch
            System.out.println("HELLO");
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
            this.game.addListener(new MatchLog(this.controller.getMatchID()));
            for(Client client : this.controller.getClients()) {
                client.update("Correct number of players reached!" +
                        "\nThe match is starting...Extraction of the first player is running");
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
        //this.startGame();
    }

    @Override
    public void update(Client client, List<int[]> coords, Integer column) throws RemoteException {
        this.game.informLog(client, coords, column);
        this.controller.update(client, coords, column);
    }
}