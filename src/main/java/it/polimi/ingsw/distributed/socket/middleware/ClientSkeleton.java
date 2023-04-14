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

public class ClientSkeleton implements Client {

    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;

    public ClientSkeleton(Socket socket) throws RemoteException {
        try{
            this.oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RemoteException("Cannot create output stream: " + e.getMessage());
        }
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException("Cannot create input stream: " + e.getMessage());
        }
    }

    @Override
    public void update(GameBoardView gb) throws RemoteException {
        try{
            oos.writeObject(gb);
        } catch (IOException e) {
            throw new RemoteException("Cannot send gameboard: " + e.getMessage());
        }
    }

    @Override
    public void update(GameView game, int clientID) throws RemoteException {
        try{
            oos.writeObject(game);
            oos.writeObject(clientID);
        } catch (IOException e) {
            throw new RemoteException("Cannot send game/clientID: " + e.getMessage());
        }
    }

    @Override
    public void update(Item[][] gameGrid) throws RemoteException {
        try{
            oos.writeObject(gameGrid);
        } catch (IOException e) {
            throw new RemoteException("Cannot send gameGrid: " + e.getMessage());
        }
    }

    @Override
    public void update(ShelfView shelf) throws RemoteException {
        try{
            oos.writeObject(shelf);
        } catch (IOException e) {
            throw new RemoteException("Cannot send shelf: " + e.getMessage());
        }
    }

    @Override
    public void update(Integer column) throws RemoteException {
        try{
            oos.writeObject(column);
        } catch (IOException e) {
            throw new RemoteException("Cannot send column: " + e.getMessage());
        }
    }

    @Override
    public void update(String msg) throws RemoteException {
        try{
            oos.writeObject(msg);
        } catch (IOException e) {
            throw new RemoteException("Cannot send msg: " + e.getMessage());
        }
    }

    @Override
    public void update(int clientID) throws RemoteException {
        try{
            oos.writeObject(clientID);
        } catch (IOException e) {
            throw new RemoteException("Cannot send clientID: " + e.getMessage());
        }
    }

    public void receive(Server server) throws RemoteException {
        Client client;
        Object o;
        Integer column;
        String nickname;
        List<int[]> coords;
        try{
            client = (Client) ois.readObject();
            o = (Object) ois.readObject();
        } catch (IOException e) {
            throw new RemoteException("Cannot receive event: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot cast event: " + e.getMessage());
        }
        if(o instanceof Integer){
            column = (Integer) o;
            server.update(client, column);
        }
        if(o instanceof String){
            nickname = (String) o;
            server.update(client, nickname);
        }
        if(o instanceof List<?>){
            coords = (List<int[]>) o;
            server.update(client, coords);
        }
    }

    @Override
    public String getNickname() throws RemoteException {
        return null;
    }
}
