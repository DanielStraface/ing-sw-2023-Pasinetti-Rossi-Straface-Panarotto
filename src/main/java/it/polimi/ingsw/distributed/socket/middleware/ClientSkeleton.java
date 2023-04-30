package it.polimi.ingsw.distributed.socket.middleware;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.exceptions.NotMessageFromClientYet;
import it.polimi.ingsw.model.Item;
import it.polimi.ingsw.modelview.GameBoardView;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.ShelfView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ClientSkeleton implements Client {

    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    private int status = 0;
    private int numberOfTurnRequest = 0;
    private int clientID;

    public ClientSkeleton(Socket socket) throws RemoteException {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RemoteException("Cannot create output stream", e);
        }
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException("Cannot create input stream", e);
        }
        System.out.println("This is clientSkeleton #" + this.toString());
    }

    @Override
    public void update(GameBoardView gb) throws RemoteException {
        try{
            oos.writeObject(gb);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send gb event: " + e.getMessage());
        }
    }

    @Override
    public void update(GameView game, int clientID) throws RemoteException {
        try{
            oos.writeObject(game);
            flushAndReset(oos);
            oos.writeObject(clientID);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send gameview and clientID event: " + e.getMessage());
        }
    }

    @Override
    public void update(Item[][] gameGrid) throws RemoteException {
        try{
            oos.writeObject(gameGrid);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send gameGrid event: " + e.getMessage());
        }
    }

    @Override
    public void update(ShelfView shelf) throws RemoteException {
        try{
            oos.writeObject(shelf);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send shelf event: " + e.getMessage());
        }
    }

    @Override
    public void update(Integer column) throws RemoteException {
        try{
            oos.writeObject(column);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send column event: " + e.getMessage());
        }
    }

    @Override
    public void update(String msg) throws RemoteException {
        try{
            oos.writeObject(msg);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send string event: " + e.getMessage());
        }
    }

    @Override
    public void update(int clientID) throws RemoteException {
        try{
            this.clientID = clientID;
            oos.writeObject(clientID);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send clientID event: " + e.getMessage());
        }
    }

    @Override
    public String getNickname() throws RemoteException {
        return null;
    }

    @Override
    public int getClientID() throws RemoteException {
        return this.clientID;
    }

    public synchronized void receive(Server server) throws RemoteException, NotMessageFromClientYet {
        Client client = null;
        Object o;
        Integer column = null;
        String msg;
        List<int[]> coords = null;

        try{
            o = ois.readObject();
            if(o instanceof Client){
                client = (Client) o;
            }
            if(o instanceof List<?>) coords = (List<int[]>) o;
            if(o instanceof Integer) column = (Integer) o;
            if(o instanceof String) {
                msg = (String) o;
                if(msg.equals("START GAME")) server.startGame();
                else System.err.println("Discarding notification " + msg + "as string in clientSkeleton " +
                        this.clientID + " clientID number");
            }
        } catch (IOException e) {
            return;
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot find the object class correctly in ClientSkeleton: " + e.getMessage());
        }

        if(client != null){
            numberOfTurnRequest++;
            if(this.status == 0){
                this.status = 1;
            } else {
                this.status = -1;
            }
        } else if(coords != null) {
            if (this.status == 1) {
                this.status = 0;
                try{
                    server.update(this, coords);
                } catch (RemoteException e) {
                    if(e.getMessage().contains("Try again")){
                        this.update(e.getMessage());
                    }
                }
            } else {
                this.status = -1;
                System.err.println("I have read coords before client. Status := " + this.status);
            }
        } else if(column != null){
            if(this.status == 1){
                this.status = 0;
                server.update(this, column);
            } else {
                this.status = -1;
                System.err.println("I have read column before client. Status := " + this.status);
            }
        } else {
            if(this.numberOfTurnRequest == 0){
                throw new NotMessageFromClientYet();
            } else {
                throw new RemoteException("Error while reading the client, i did not read the client!");
            }

        }
    }

    public List<Object> receive() throws RemoteException{
        Client client;
        Integer numberOfPlayer;
        String nickname;
        try{
            client = (Client) ois.readObject();
            numberOfPlayer = (Integer) ois.readObject();
            nickname = (String) ois.readObject();
        } catch (IOException e) {
            throw new RemoteException("Cannot receive the client while understand which match it is " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot cast the client while understand which match it is " + e.getMessage());
        }
        List<Object> information = new ArrayList<>();
        information.add(client);
        information.add(numberOfPlayer);
        information.add(nickname);
        return information;
    }

    private void flushAndReset(ObjectOutputStream o) throws IOException {
        o.flush();
        o.reset();
    }
}
