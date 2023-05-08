package it.polimi.ingsw.distributed.socket.middleware;

import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.exceptions.NoMatchException;
import it.polimi.ingsw.exceptions.NotMessageFromServerYet;
import it.polimi.ingsw.exceptions.NotSupportedMatchesException;
import it.polimi.ingsw.exceptions.TooManyMatchesException;
import it.polimi.ingsw.listeners.MatchLog;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.server.AppServer;

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
    private static final String NO_GAME_ON_SERVER = "NO_GAME";

    public ServerStub(String ip, int port){
        this.ip = ip;
        this.port = port;
        try {
            createConnection();
        } catch (RemoteException e) {
            System.err.println("Cannot establish the socket connection: " + e.getMessage());
        }
    }
    @Override
    public void startGame() throws RemoteException {
        try{
            oos.writeObject("START GAME");
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Error while starting the game method", e);
        }
    }

    @Override
    public void register(Client client, String nickname) throws RemoteException {
        this.startGame();
        /*try{
            oos.writeObject("START GAME");
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Error while register the client on the server", e);
        }*/
    }

    @Override
    public void update(Client client, Integer column) throws RemoteException {
        try{
            oos.writeObject(client);
            flushAndReset(oos);
            oos.writeObject(column);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event: " + e.getMessage());
        }
    }

    @Override
    public void update(Client client, String nickname) throws RemoteException {
        try{
            oos.writeObject(client);
            flushAndReset(oos);
            oos.writeObject(nickname);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event: " + e.getMessage());
        }
    }

    @Override
    public void update(Client client, List<int[]> coords) throws RemoteException {
        try{
            oos.writeObject(client);
            flushAndReset(oos);
            oos.writeObject(coords);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event: " + e.getMessage());
        }
    }

    @Override
    public void update(Client client, List<int[]> coords, Integer column) throws RemoteException {
        try{
            oos.writeObject(coords);
            flushAndReset(oos);
            oos.writeObject(column);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event: " + e.getMessage());
        }
    }

    public void receive(Client client) throws RemoteException, NotMessageFromServerYet {
        Object o;
        GameView gmv;
        String msg;
        Integer id;
        try{
            o = ois.readObject();
        } catch (IOException e) {
            throw new NotMessageFromServerYet();
            //throw new RemoteException("Cannot receive event: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RemoteException("Cannot cast event: " + e.getMessage());
        }
        if(o instanceof String){
            msg = (String) o;
            boolean toTerminate = false;
            if(msg.equals(NO_GAME_ON_SERVER)){
                try{
                    msg = (String) ois.readObject();
                    toTerminate = true;
                } catch (IOException e) {
                    throw new RemoteException("Cannot receive event: " + e.getMessage());
                } catch (ClassNotFoundException e) {
                    throw new RemoteException("Cannot cast event: " + e.getMessage());
                }
            }
            client.update(msg);
            if(toTerminate) System.exit(3);
        }
        //--------------------------------------------------------------------------------------------------------------
        //game --> readObject() = integer --> update(GameBoardView, int)
        //--------------------------------------------------------------------------------------------------------------
        if(o instanceof GameView){
            gmv = (GameView) o;
            if(client instanceof MatchLog) System.out.println("RERER");
            client.update(gmv);
        }
        if(o instanceof Integer){
            id = (Integer) o;
            client.update(id);
        }
    }

    private void createConnection() throws RemoteException{
        try {
            this.socket = new Socket(ip, port);
            try{
                this.oos = new ObjectOutputStream(socket.getOutputStream());
                oos.flush();
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

    private void flushAndReset(ObjectOutputStream o) throws IOException {
        o.flush();
        o.reset();
    }

    public void close() throws RemoteException{
        try{
            socket.close();
        } catch (IOException e) {
            throw new RemoteException("Cannot close socket", e);
        }
    }

    public boolean log(String nickname) throws RemoteException{
        Boolean result;
        try{
            oos.writeObject(nickname);
            flushAndReset(oos);
            while(true){
                try{
                    result = (Boolean) ois.readObject();
                    break;
                } catch (IOException | ClassNotFoundException ignored) {
                }
            }
            return result;
        } catch (IOException e) {
            throw new RemoteException("Cannot send event: " + e.getMessage());
        }
    }

    public void removeLoggedUser(String nickname) throws RemoteException{
        try{
            oos.writeObject(nickname);
            flushAndReset(oos);
        } catch (IOException e) {
            throw new RemoteException("Cannot send event: " + e.getMessage());
        }
    }

    public void connect(AppServer.typeOfMatch type) throws RemoteException, NotSupportedMatchesException {
        Object o;
        boolean result;
        try{
            oos.writeObject(type);
            flushAndReset(oos);
            while(true){
                try{
                    o = ois.readObject();
                    if(o instanceof Boolean){
                        result = (Boolean) o;
                        if(!result){
                            if(type != AppServer.typeOfMatch.existingGame) throw new TooManyMatchesException();
                            else throw new NoMatchException();
                        }
                        break;
                    }
                } catch (IOException | ClassNotFoundException ignored) {
                }
            }
        } catch (IOException e) {
            throw new RemoteException("Cannot send event: " + e.getMessage());
        }
    }
}
