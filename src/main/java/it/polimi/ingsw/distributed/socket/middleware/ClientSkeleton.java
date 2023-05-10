package it.polimi.ingsw.distributed.socket.middleware;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.exceptions.NotMessageFromClientYet;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.server.AppServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.List;

public class ClientSkeleton implements Client {

    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    private int clientID;
    private List<int[]> auxiliaryCoords;
    private Integer auxiliaryColumn;

    public ClientSkeleton(Socket socket) throws RemoteException {
        try {
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
        } catch (IOException e) {
            throw new RemoteException("Cannot create output stream", e);
        }
        try {
            this.ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException("Cannot create input stream", e);
        }
    }

    @Override
    public void update(GameView game) throws RemoteException {
        try{
            oos.writeObject(game);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send gameview and clientID event: " + e.getMessage());
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

    public String receiveNicknameToLog() throws RemoteException{
        String nickname;
        try{
            nickname = (String) ois.readObject();
            return nickname;
        } catch (IOException e) {
            throw new RemoteException("Cannot receive the client while understand which match it is " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot cast the client while understand which match it is " + e.getMessage());
        }
    }

    public void sendLogginResult(Boolean result) throws RemoteException{
        try{
            oos.writeObject(result);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send clientID event: " + e.getMessage());
        }
    }

    public AppServer.typeOfMatch setupMatch() throws RemoteException {
        try{
            Object o = ois.readObject();
            if(o instanceof AppServer.typeOfMatch) return (AppServer.typeOfMatch) o;
            else throw new RemoteException();
        } catch (IOException e) {
            throw new RemoteException("Cannot receive the client while understand which match it is " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot cast the client while understand which match it is " + e.getMessage());
        }
    }

    public void sendMatchServer(Boolean value) throws RemoteException{
        try{
            flushAndReset(oos);
            oos.writeObject(value);
            oos.flush();
        } catch (IOException e) {
            throw new RemoteException("Cannot send clientID event: " + e.getMessage());
        }
    }

    public synchronized void receive(Server server) throws RemoteException, NotMessageFromClientYet {
        Client client = null;
        Object o;
        Integer column = null;
        String msg;
        List<int[]> coords = null;

        try{
            o = ois.readObject();
            if(o instanceof List<?>) coords = (List<int[]>) o;
            if(o instanceof Integer) column = (Integer) o;
            if(o instanceof String) {
                msg = (String) o;
                if(msg.equals("START GAME")) {
                    server.startGame();
                }
                else System.err.println("Discarding notification " + msg + "as string in clientSkeleton " +
                        this.clientID + " clientID number");
            }
        } catch (IOException e) {
            return;
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot find the object class correctly in ClientSkeleton: " + e.getMessage());
        }

        if(coords != null){
            if(this.auxiliaryColumn != null) server.update(this, coords, this.auxiliaryColumn);
            else this.auxiliaryCoords = coords;
        }
        if(column != null){
            if(this.auxiliaryCoords != null) server.update(this, this.auxiliaryCoords, column);
            else this.auxiliaryColumn = column;
        }
    }

    private void flushAndReset(ObjectOutputStream o) throws IOException {
        o.flush();
        o.reset();
    }
}
