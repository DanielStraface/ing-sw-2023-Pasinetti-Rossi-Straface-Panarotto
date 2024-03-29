package it.polimi.ingsw.distributed;

import it.polimi.ingsw.client.CLI.AppClient;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.modelview.GameView;
import it.polimi.ingsw.modelview.PlayerView;
import it.polimi.ingsw.modelview.ShelfView;
import it.polimi.ingsw.client.CLI.TextualUI;
import it.polimi.ingsw.client.UI;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The ClientImpl class is an implementation of the Client Interface. It represents a client connected with the server.
 * it contains method to handle game updates, messages and client's information.
 */
public class ClientImpl extends UnicastRemoteObject implements Client, Serializable {
    /**
     * The ClientState represents the state in which the client could be.
     *  LAUNCH - the client is in the setup phase
     *  WAITING_IN_LOBBY - the client is waiting for other players
     *  PLAYING - the client is playing is match
     *  GAMEOVER - the match is over
     */
    public enum ClientState {
        LAUNCH, WAITING_IN_LOBBY, PLAYING, GAMEOVER
    }
    private transient ClientState clientState;
    transient UI view;
    transient String nickname;
    private int clientID;
    private transient boolean gameOverPointTokenFlag = false;

    /**
     * Constructor method
     * @param server Server to connect to
     * @param nickname Player's nickname
     * @param uiType TUI/GUI
     * @param guiReference used if GUI is chosen
     * @throws RemoteException if the server is unreachable
     */
    public ClientImpl(Server server, String nickname, AppClient.UIType uiType, Object guiReference)
            throws RemoteException {
        super();
        this.clientState = ClientState.LAUNCH;
        this.nickname = nickname;
        if(uiType == AppClient.UIType.CLI) this.view = new TextualUI();
        if(uiType == AppClient.UIType.GUI) this.view = (GUI) guiReference;
        server.register(this, nickname);
        this.view.addListener(server);
    }

    /**
     * If the gameOver state is triggered, various updates are invoked from the UI:
     *                 - printing a gameOver message
     *                 - printing the gameBoard
     *                 - printing the player's shelf
     * Otherwise through various updates also invoked from the UI the turn is changed to the next player
     * by changing the ClientState (enum) accordingly
     *
     * @param game GameView
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public void update(GameView game) throws RemoteException {
        ShelfView sh;
        if(game.getGameOverFinalMessage() != null){
            this.view.update("THE MATCH IS FINISHED, THE GAMEOVER IS REACHED!\n" +
                    "The final game board is reported below:");
            this.view.update(game.getGameBoard());
            this.view.update("Your final shelf is reported below: ");
            sh = game.getPlayers().stream()
                    .filter(p -> {
                        try {
                            return p.getClientID() == this.getClientID();
                        } catch (RemoteException e) {
                            System.err.println("Cannot obtain the clientID from this Client");
                        }
                        return false;
                    })
                    .findFirst()
                    .get().getMyShelf();
            this.view.update(sh);
            this.view.update(game.getGameOverFinalMessage());
            if(this.view instanceof GUI) ((GUI) this.view).adjustFinalScore(game);
            this.clientState = ClientState.GAMEOVER;
            return;
        }
        if(game.getGameOverPointToken() && !gameOverPointTokenFlag){
            this.view.gameOverPointTokenHandler(game, game.getGameOverPointPlayerNickname());
            try{
                TimeUnit.SECONDS.sleep(3);
                gameOverPointTokenFlag = false;
            } catch (InterruptedException e) {
                System.err.println("Cannot sleep in gameOverPointToken handler: " + e.getMessage());
            }
        }
        if(this.getClientState() != ClientState.PLAYING) this.clientState = ClientState.PLAYING;
        if(game.getCurrentPlayer().getClientID() == this.clientID){
            this.view.run(game);
        } else {
            if(game.getPrevClientID() == this.clientID){
                sh = game.getPlayers().stream().filter(p -> p.getClientID() == game.getPrevClientID())
                        .findFirst()
                        .get().getMyShelf();
                this.view.update("Your turn is finished!\nPlease wait for the other players turn");
                this.view.update(sh);
            }
            PlayerView ply = game.getPlayers().stream()
                    .filter(p -> {
                        try {
                            return p.getClientID() == this.getClientID();
                        } catch (RemoteException e) {
                            System.err.println("Cannot obtain the clientID from this Client");
                        }
                        return false;
                    })
                    .findFirst()
                    .get();
            this.view.displayInfo(game, ply);
            this.view.update(game.getGameBoard());
            this.view.update(ply.getMyShelf());
            this.view.update(game.getCurrentPlayer().getNickname() + " is playing.");
        }
    }

    /**
     * Invokes an update method in the UI containing a message String
     * @param msg String
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public void update(String msg) throws RemoteException {
        this.view.update(msg);
        if(msg.contains("disconnected")){
            System.exit(-5);
        }
    }

    /**
     * Invokes an update method in the UI containing the player's ID, sets the Client that invoked this method
     * as reference and changes its state to WAITING_IN_LOBBY (enum)
     * @param clientID int playerID
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public void update(int clientID) throws RemoteException {
        this.clientID = clientID;
        this.view.setReferenceClient(this);
        this.clientState = ClientState.WAITING_IN_LOBBY;
    }

    /**
     * update method
     * @param notificationList the object list that contains the disconnection information
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public synchronized void update(List<Object> notificationList) throws RemoteException {
        QuitState quitState = (QuitState) notificationList.get(0);
        String msg = (String) notificationList.get(1);
        if(quitState == QuitState.QUIT || quitState == QuitState.EMPTY_BAG) this.view.update(msg);
    }

    /**
     * Get method for a player's Nickname
     * @return nickname String
     */
    @Override
    public String getNickname(){return this.nickname;}

    /**
     * Get method for a clientID
     * @return int clientID
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Override
    public int getClientID() throws RemoteException {return this.clientID;}

    /**
     * Get method for a ClientState (enum)
     * @return ClientState enum
     */
    public ClientState getClientState(){return this.clientState;}
}