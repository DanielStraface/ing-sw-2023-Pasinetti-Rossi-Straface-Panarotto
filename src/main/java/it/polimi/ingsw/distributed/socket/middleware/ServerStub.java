package it.polimi.ingsw.distributed.socket.middleware;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.ShelfView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;

public class ServerStub implements Server {

    private final String ip;
    private final int port;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Game game;
    private Controller controller;
    private boolean[] toConnect;

    public ServerStub(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void startGame() throws RemoteException {
        if(this.controller.getViews().size() == this.controller.getGame().getPlayers().size()){
            for(Client client : this.controller.getViews())
                client.update("The match is starting...Extraction of the first player is running");
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
    public void register(Client client, String nickname) throws RemoteException {
        createConnection();
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
                client.update(i*10);
                break;
            }
        }
    }

    @Override
    public void register(Client client, int typeOfMatch, String nickname) throws RemoteException {
        createConnection();
        try{
            this.game = new Game(typeOfMatch);
        } catch (Exception e){
            System.err.println(e.getMessage());
        }
        this.controller = new Controller(game, client);
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
        /*try{
            oos.writeObject(coords);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event: " + e.getMessage());
        }*/
    }

    @Override
    public void update(Client client, Integer column) throws RemoteException {
        try{
            oos.writeObject(column);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event: " + e.getMessage());
        }
    }

    @Override
    public void update(Client client, String nickname) throws RemoteException {
        try{
            oos.writeObject(nickname);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event: " + e.getMessage());
        }
    }

    @Override
    public void update(Client client, List<int[]> coords) throws RemoteException {
        try{
            oos.writeObject(coords);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event: " + e.getMessage());
        }
    }

    public void receive(Client client) throws RemoteException{
        Object o;
        GameView gmv;
        GameBoardView gb;
        ShelfView sh;
        Item[][] gg;
        Integer integer; //clientID and column in 3 methods
        String msg;
        try{
            o = ois.readObject();
        } catch (IOException e) {
            throw new RemoteException("Cannot receive event: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot cast event: " + e.getMessage());
        }
        //--------------------------------------------------------------------------------------------------------------
        //gameboard --> update(GameBoardView)
        //--------------------------------------------------------------------------------------------------------------
        if(o instanceof GameBoardView){
            gb = (GameBoardView) o;
            client.update(gb);
        }
        //--------------------------------------------------------------------------------------------------------------
        //shelf --> update(ShelfView)
        //--------------------------------------------------------------------------------------------------------------
        if(o instanceof ShelfView){
            sh = (ShelfView) o;
            client.update(sh);
        }
        //--------------------------------------------------------------------------------------------------------------
        //gamegrid --> update(Item[][])
        //--------------------------------------------------------------------------------------------------------------
        if(o instanceof Item[][]){
            gg = (Item[][]) o;
            client.update(gg);
        }
        //--------------------------------------------------------------------------------------------------------------
        //msg --> update(String)
        //--------------------------------------------------------------------------------------------------------------
        if(o instanceof String){
            msg = (String) o;
            client.update(msg);
        }
        //--------------------------------------------------------------------------------------------------------------
        //game --> readObject() = integer --> update(GameBoardView, int)
        //--------------------------------------------------------------------------------------------------------------
        if(o instanceof GameView){
            gmv = (GameView) o;
            try{
                integer = (Integer) ois.readObject();
                client.update(gmv, integer.intValue());
            } catch (IOException e) {
                throw new RemoteException("Cannot receive event: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                throw new RemoteException("Cannot cast event: " + e.getMessage());
            }
        }
        //--------------------------------------------------------------------------------------------------------------
        //integer --> update(Integer - column) or update(int - clientID)
        //--------------------------------------------------------------------------------------------------------------
        if(o instanceof Integer){
            integer = (Integer) o;
            if(integer >= 10)
                client.update(integer.intValue());
            if(integer > 0 && integer < 6)
                client.update(integer);
        }
    }

    public void createConnection() throws RemoteException{
        try {
            this.socket = new Socket(ip, port);
            try{
                this.oos = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new RemoteException("Cannot create output stream: " + e.getMessage());
            }
            try{
                this.ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                throw new RemoteException("Cannot create input stream: " + e.getMessage());
            }
        } catch (IOException e) {
            throw new RemoteException("Error while connecting to server: " + e.getMessage());
        }
    }

    public void close() throws RemoteException{
        try{
            socket.close();
        } catch (IOException e) {
            throw new RemoteException("Cannot close socket", e);
        }
    }
}