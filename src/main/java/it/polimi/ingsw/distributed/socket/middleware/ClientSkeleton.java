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
    private String nickname;

    /**
     * Constructor method
     * @throws RemoteException
     */
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

    /**
     * Writes GameView to the ObjectOutputStream, then flushes and resets
     * @param game GameView
     * @throws RemoteException
     */
    @Override
    public void update(GameView game) throws RemoteException {
        try{
            oos.writeObject(game);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send gameview and clientID event: " + e.getMessage());
        }
    }

    /**
     * Writes a String containing a message to the ObjectOutputStream, then flushes and resets
     * @param msg String -> message
     * @throws RemoteException
     */
    @Override
    public void update(String msg) throws RemoteException {
        try{
            oos.writeObject(msg);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send string event: " + e.getMessage());
        }
    }

    /**
     * Writes a clientID to the ObjectOutputStream, then flushes and resets
     * @param clientID clientID
     * @throws RemoteException
     */
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
    public void update(List<Object> notificationList) throws RemoteException {
        try {
            oos.writeObject(notificationList);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send quitState event: " + e.getMessage());
        }
    }

    /**
     * Get method for a nickname string
     * @return nickname String
     * @throws RemoteException
     */
    @Override
    public String getNickname() throws RemoteException {
        return this.nickname;
    }

    /**
     * Get method for a clientID
     * @return int -> clientID
     * @throws RemoteException
     */
    @Override
    public int getClientID() throws RemoteException {
        return this.clientID;
    }

    /**
     * Reads a nickname String from Object Input Stream and returns it
     * @return nickname String
     * @throws RemoteException
     */
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

    /**
     * Set method for Nickname
     * @param nickname String
     */
    public void setNickname(String nickname){
        this.nickname = nickname;
    }

    /**
     * Writes a boolean with a positive(true) or negative(false) result to the ObjectOutputStream, then flushes and resets
     * @param result Boolean
     * @throws RemoteException
     */
    public void sendLoginResult(Boolean result) throws RemoteException{
        try{
            oos.writeObject(result);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send clientID event: " + e.getMessage());
        }
    }

    /**
     * Reads an AppServer.TypeOfMatch (enum) from Object Input Stream and returns it
     * @return Match Type enum
     * @throws RemoteException
     */
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

    /**
     * Writes a boolean (true if the server has been successfully created)
     * to the ObjectOutputStream after flushing and resetting, then flushes again
     * @param value Boolean
     * @throws RemoteException
     */
    public void sendMatchServer(Boolean value) throws RemoteException{
        try{
            flushAndReset(oos);
            oos.writeObject(value);
            oos.flush();
        } catch (IOException e) {
            throw new RemoteException("Cannot send clientID event: " + e.getMessage());
        }
    }

    /**
     * Method that reads any type of object from the Object Input Stream received from the server and acts accordingly
     * based on the object received:
     *        List<int[]>: is assigned to the "coords" List
     *        Integer: is assigned to the "column" Integer
     *        String: Starts a game if it is equal to "START GAME"
     * It also updates invoking the "update" method in the server with the coordinates or column given
     * @param server Server from which the object is received
     * @throws RemoteException
     * @throws NotMessageFromClientYet
     */
    public synchronized void receive(Server server) throws RemoteException, NotMessageFromClientYet {
        Object o;
        Integer column = null;
        String msg;
        List<int[]> coords = null;
        List<Object> notificationList = null;

        try{
            o = ois.readObject();
            System.out.println("prima if");
            if(o instanceof List<?>) {
                System.out.println("dopo if");
                notificationList = (List<Object>) o;
                if(notificationList.get(0) instanceof int[]){
                    System.out.println("non devo entrare");
                    coords = (List<int[]>) o;
                }
                if(notificationList.get(0) instanceof String){
                    System.out.println("TERZO ter");
                    server.update(notificationList);
                }
            }
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
            if(this.auxiliaryColumn != null) {
                List<int[]> finalCoords = coords;
                new Thread(() -> {
                    try {
                        server.update(this, finalCoords, this.auxiliaryColumn);
                    } catch (RemoteException e) {
                        System.err.println("Cannot reached the server in receive coords: " + e.getMessage());
                    }
                }).start();
            }
            else this.auxiliaryCoords = coords;
        }
        if(column != null){
            if(this.auxiliaryCoords != null) {
                Integer finalColumn = column;
                new Thread(() -> {
                    try {
                        server.update(this, this.auxiliaryCoords, finalColumn);
                    } catch (RemoteException e) {
                        System.err.println("Cannot reached the server in receive column: " + e.getMessage());
                    }
                }).start();
            }
            else this.auxiliaryColumn = column;
        }
    }

    /**
     * Method used to flush and reset an Object Output Stream
     * @param o Object Output Stream
     * @throws IOException
     */
    private void flushAndReset(ObjectOutputStream o) throws IOException {
        o.flush();
        o.reset();
    }
}
