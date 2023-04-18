package it.polimi.ingsw.distributed;

import it.polimi.ingsw.AppServerImpl;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.socket.middleware.ClientSkeleton;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.exceptions.NoSavingPointException;
import it.polimi.ingsw.exceptions.SaveFileNotFoundException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

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

    private boolean findAPreviousGame() throws RemoteException {
        String fileName;
        for(int i=0;i<100;i++){
            try{
                boolean marker = true;
                fileName = "match" + i;
                Game gameLoaded = this.controller.loadGame(fileName);
                for(Player pFromGameLoaded : gameLoaded.getPlayers()){
                    for(Player pFromThisGame : this.game.getPlayers()){
                        if(!(pFromThisGame.getNickname().equals(pFromGameLoaded.getNickname()))){
                            marker = false;
                        }
                    }
                }
                if(marker){
                    this.game = gameLoaded;
                    System.out.println("Server load a previous version of this match");
                    for(Client client : this.controller.getViews()) {
                        client.update("Previous match found for these player! The match resume from that point");
                    }
                    return true;
                }
            } catch (SaveFileNotFoundException e) {}
        }
        return false;

        /*try{
            Controller.loadGame(this.controller.)
        }*/
    }

    public int getPlayersGameNumber(){return this.game.getPlayers().size();}
    public boolean getGameOver(){return this.controller.getGameOver();}

    @Override
    public void startGame() throws RemoteException{
        if(this.controller.getViews().size() == this.controller.getGame().getPlayers().size()){
            for(Client client : this.controller.getViews())
                client.update("The match is starting...Extraction of the first player is running");
            //*********************************
            if(!findAPreviousGame()) this.controller.chooseFirstPlayer();
            else this.controller.continuePreviousMatch();
            //*********************************
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
    public void register(Client client, String nickname) throws RemoteException {
        /*if(this.game == null){
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
                client.update(i*10);
                break;
            }
        }
        System.out.println("Register client " + client + " for a " + this.game.getPlayers().size() + " players match");
        System.out.println("There are " + this.game.countObservers() + " game listeners");*/
        /*for(int i=0;i<this.toConnect.length;i++){
            if(this.toConnect[i] == false)
                return false;
        }
        notifyAll();
        return true;*/
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
            //this.controller = new Controller(game, client);
            this.controller.addClientView(client);
            this.game.addListener(client);
            for(int i=0;i<toConnect.length;i++){
                if(this.toConnect[i] == false){
                    this.toConnect[i] = true;
                    this.game.getPlayers().get(i).addListenerForPlayer(client);
                    this.game.getPlayers().get(i).setNicknameAndClientID(nickname, i*10);
                    client.update(i*10);
                    break;
                }
            }
        } else {
            try{
                this.game = new Game(numOfPlayers);
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
            try{
                this.controller = new Controller(game, client, AppServerImpl.getNumberOfMatch(this));
            } catch (NoSavingPointException e) {
                System.err.println("Controller not create correctly: " + e.getMessage() +"\nClosing...");
                System.exit(5);
            }
            //this.controller = new Controller(game, client, AppServerImpl.getNumberOfMatch(this));
            this.game.addListener(client);
            this.toConnect = new boolean[this.game.getPlayers().size()];
            for(int i=0;i<toConnect.length;i++){
                if(this.toConnect[i] == false){
                    this.toConnect[i] = true;
                    this.game.getPlayers().get(i).addListenerForPlayer(client);
                    this.game.getPlayers().get(i).setNicknameAndClientID(nickname, i*10);
                    break;
                }
            }
        }
        System.out.println("Register client " + client + " for a " + this.game.getPlayers().size() + " players match");
        if(fromSocket == true){
            this.startGame();
        }
        //startGame();
        /*for(Player player : this.game.getPlayers()){
            player.addListenerForPlayer(client);
        }*/
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

    @Override
    public boolean equals(Object o){
        if(!(o instanceof ServerImpl)){
            return false;
        } else {
            if(o.toString().equals(this.toString())) return true;
            else return false;
        }
    }
}