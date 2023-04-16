package it.polimi.ingsw.distributed.socket.middleware;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
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

    public ServerStub(String ip, int port){
        this.ip = ip;
        this.port = port;
    }
    @Override
    public void startGame() throws RemoteException {
        try{
            oos.writeObject("START GAME");
        } catch (IOException e) {
            throw new RemoteException("Error while starting the game method", e);
        }
    }

    @Override
    public void register(Client client, String nickname) throws RemoteException {
        createConnection();
    }

    @Override
    public void register(Client client, int typeOfMatch, String nickname) throws RemoteException {
        createConnection();
        try{
            oos.writeObject(client);
            oos.writeObject(typeOfMatch);
            oos.writeObject(nickname);
        } catch (IOException e) {
            throw new RemoteException("Error while register the client on the server", e);
        }
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

    private void createConnection() throws RemoteException{
        try {
            this.socket = new Socket(ip, port);
            try{
                this.oos = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e){
                throw  new RemoteException("Cannot create output stream: ", e);
            }
            try{
                this.ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                throw  new RemoteException("Cannot create input stream: ", e);
            }
        } catch (IOException e) {
            throw new RemoteException("Error while connecting to server: ", e);
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
