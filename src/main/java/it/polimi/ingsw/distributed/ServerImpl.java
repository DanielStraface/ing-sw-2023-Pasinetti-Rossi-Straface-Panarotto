package it.polimi.ingsw.distributed;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.listeners.MatchLog;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.AppServer;
import it.polimi.ingsw.server.AppServerImpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
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

    /**
     * Constructor method
     * @param numOfPlayers type of match chosen (enum) that tells the total number of players
     * @throws RemoteException
     */
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

    /**
     * Makes the player join a new match lobby and starts the game if all players are connected or it makes the player
     * join an unfinished match
     * @throws RemoteException
     */
    @Override
    public void startGame() throws RemoteException{
        if(this.controller.getClients().size() == this.controller.getGame().getPlayers().size()){
            this.controller.getClients().get(this.controller.getClients().size() - 1).update("Joining a lobby...");
            this.controller.setMatchID(AppServerImpl.getMatchID(this));
            MatchLog thisMatchLog = new MatchLog(this.controller.getMatchID());
            thisMatchLog.update(5);
            this.game.addListener(thisMatchLog);
            if(checkIfPrevGame()){
                for(Client c : this.controller.getClients())
                    c.update("Old unfinished match found!\nThe game will resume at that point. " +
                            "\nIf you want to join a new game consider to change your nickname");
                this.controller.substituteGameModel(this.game);
                this.game.setCurrentPlayer(this.game.getCurrentPlayer());
            } else {
                for(Client client : this.controller.getClients()) {
                    client.update("Correct number of players reached!" +
                            "\nThe match is starting...Extraction of the first player is running");
                }
                this.controller.chooseFirstPlayer();
            }
        } else {
            int temp = 0;
            for (boolean b : this.toConnect) {
                if (!b)
                    temp++;
            }
            String[] matchPlayersNumberStrings= {"TWO", "THREE", "FOUR"};
            for(Client client : this.controller.getClients()){
                client.update(matchPlayersNumberStrings[getPlayersGameNumber() - 2] + " PLAYERS MATCH" +
                 "\nLobby joined but the match lobby is not full.\nPlease wait..." +
                        "searching for " + temp + " other players");
            }
        }
    }

    /**
     * Registers a client with his nickname, adds the client to the controller's client List and as model listener,
     * if successful the player's ID is created (int on multiples of 10) and invokes an update on the player's client
     * with the just created ID
     * @param client client to register
     * @param nickname player's nickname
     * @throws RemoteException
     */
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
    }

    /**
     * Update method with all the player's choices (coordinates, column) to update the controller and the matchLog
     * @param client the choosing player
     * @param coords coordinates chosen
     * @param column the shelf column chosen
     * @throws RemoteException
     */
    @Override
    public void update(Client client, List<int[]> coords, Integer column) throws RemoteException {
        this.game.informLog(client, coords, column);
        this.controller.update(client, coords, column);
    }

    @Override
    public void update(List<String> notificationList) throws RemoteException {
        String nameDisconnectedClient = ( notificationList.get(0));
        String msg = "The user of player " + nameDisconnectedClient
                + " has disconnected! The game ends here...";
        AppServerImpl.forceGameRemove(this.controller.getMatchID());
        this.controller.getGame().notifyDisconnection(this.controller.getGame(), nameDisconnectedClient, msg);
    }

    /**
     * Method to check if an unfinished match can be loaded from its ".ser" file
     * @return boolean true if the match has been found, false otherwise
     */
    private boolean checkIfPrevGame() {
        Game game;
        for(int i=0;i<AppServerImpl.MAX_MATCHES_MANAGED;i++){
            try{
                game = Controller.loadGame("match" + i + ".ser");
                if(game == null) return false;
                else {
                    List<String> newGameNicknames = this.game.getPlayers().stream()
                            .map(Player::getNickname)
                            .toList();
                    List<String> prevGameNicknames = game.getPlayers().stream()
                            .map(Player::getNickname)
                            .toList();
                    if(newGameNicknames.size() != prevGameNicknames.size()) return false;
                    for(String nickname : prevGameNicknames)
                        if(!newGameNicknames.contains(nickname))
                            return false;
                    this.game = game;
                    this.game.deleteListeners();
                    this.controller.getClients()
                            .forEach(client -> this.game.addListener(client));
                    MatchLog thisMatchLog = new MatchLog(this.controller.getMatchID());
                    thisMatchLog.update(5);
                    this.game.addListener(thisMatchLog);
                    for(Client c : this.controller.getClients())
                        for(Player player : this.game.getPlayers())
                            if(player.getNickname().equals(c.getNickname()))
                                c.update(player.getClientID());
                    try {
                        Path path = FileSystems.getDefault().getPath("./match" + i + ".ser");
                        if(Files.deleteIfExists(path))
                            System.out.println("Previous saving file of match # " + i + " delete successfully" +
                                    "\nThe new saving file is match" + this.controller.getMatchID() + ".ser");
                        else System.err.println("Error while delete the previous saving file of match # " + i);
                    } catch (IOException e) {
                        System.err.println("Error while deleting the saving file of match # " + i + " during restore");
                    }
                    return true;
                }
            } catch (FileNotFoundException ignored) {
            } catch (RemoteException e) {
                System.err.println("Cannot reach the nickname: " + e.getMessage());
            }
        }
        return false;
    }

    /**
     * Get method for the total number of players in a match
     * @return int total number of players
     */
    public int getPlayersGameNumber(){
        return this.game.getPlayersNumber();
    }

    /**
     * Get method for the gameOver boolean (true if the game is on its last turn)
     * @return gameOver boolean
     */
    public boolean getGameOver(){return this.controller.getGameOver();}

    public List<String> getMatchNicknames(){return this.game.getPlayers().stream().map(Player::getNickname).toList();}
}