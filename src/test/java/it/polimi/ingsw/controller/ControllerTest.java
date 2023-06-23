package it.polimi.ingsw.controller;

import it.polimi.ingsw.client.CLI.AppClient;
import it.polimi.ingsw.client.GUI.GUI;
import it.polimi.ingsw.distributed.Client;
import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.Server;
import it.polimi.ingsw.distributed.ServerImpl;
import it.polimi.ingsw.exceptions.InvalidNumberOfPlayersException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.AppServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Class ControllerTest tests Controller class
 * @see Controller
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControllerTest {
    private static final int PLAYERS_NUMBER = 4;
    private Game game;
    private TurnHandler turnHandler;
    private Controller controller;
    private List<Client> clients;

    /**
     * Setup method for all tests
     * @throws RemoteException if the execution of a remote method call goes wrong
     * @throws InvalidNumberOfPlayersException if the number of players int given is invalid
     */
    @BeforeEach
    public void setup() throws RemoteException, InvalidNumberOfPlayersException {
        this.game = new Game(PLAYERS_NUMBER);
        this.turnHandler = new TurnHandler(this.game);
        this.controller = new Controller(this.game);
        this.clients = new ArrayList<>();
    }

    @Test
    /**
     * Test if given a client it is correctly added to the clients list
     */
    public void addClientTest(){
        try {
            Server server = new ServerImpl(AppServer.typeOfMatch.newTwoPlayersGame);
            Client client = new ClientImpl(server, "clientTest", AppClient.UIType.CLI, null);
            this.clients.add(client);
            controller.addClient(client);
            assertEquals(controller.getClients().get(0), this.clients.get(0), "the client added is not the same");
            this.clients.clear();
            controller.getClients().clear();
            Client newClient = new ClientImpl(server, "newClientTest", AppClient.UIType.GUI, new GUI());
            this.clients.add(newClient);
            controller.addClient(newClient);
            assertEquals(controller.getClients().get(0), this.clients.get(0), "the client added is not the same");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Tests if a random player is chosen correctly
     * @throws RemoteException if the execution of a remote method goes wrong
     */
    @Test
    public void chooseFirstPlayerTest() throws RemoteException {
        int c = 0;
        game.getPlayers().get(0).setNicknameAndClientID("player1",0);
        game.getPlayers().get(1).setNicknameAndClientID("player2",10);
        game.getPlayers().get(2).setNicknameAndClientID("player3",20);
        game.getPlayers().get(3).setNicknameAndClientID("player4",30);
        controller.chooseFirstPlayer();
        List<Player> players = game.getPlayers();
        for(Player p: players){
            if(p.getIsFirstPlayer()) c++;
        }
        assertSame(1,c,"The firstPlayer hasn't been correctly set");
    }

    /**
     * Tests if a previously saved Game is correctly loaded
     * @throws InvalidNumberOfPlayersException if the number of players int given is invalid
     * @throws RemoteException if the execution of a remote method goes wrong
     */
    @Test
    void LoadGameTest() throws InvalidNumberOfPlayersException, RemoteException {
        Game expectedGame = new Game(4);
        Controller controller1 = new Controller(expectedGame);
        String fileName = "match0.ser";
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(expectedGame);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Game loadedGame = null;
        try {
            loadedGame = controller1.loadGame(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Assertions.assertNotNull(loadedGame);
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Tests if the game attribute in controller class has been correctly substituted
     * @throws InvalidNumberOfPlayersException if the number of players int given is invalid
     * @throws RemoteException if the execution of a remote method call goes wrong
     */
    @Test
    public void substituteGameModelTest() throws InvalidNumberOfPlayersException, RemoteException {
        Game game2 = new Game(4);
        controller.substituteGameModel(game2);
        assertSame(controller.getGame(),game2,"The game hasn't been substituted correctly");
    }

    /**
     * setMatchID set method test
     */
    @Test
    public void setMatchIDTest(){
        int matchID = 40;
        controller.setMatchID(matchID);
        assertSame(controller.getMatchID(),matchID);
    }

    /**
     * getGame get method test
     */
    @Test
    public void getGameTest(){
        Game game3 = controller.getGame();
        assertSame(controller.getGame(),game3,"The gave given isn't the same");
    }

    /**
     * getClients get method test
     */
    @Test
    public void getClientsTest(){
        List<Client> clients = controller.getClients();
        assertSame(controller.getClients(),clients,"The client list given isn't the same");
    }

    /**
     * getGameOver get method test
     */
    @Test
    public void getGameOverTest(){
        boolean gameOver = controller.getGameOver();
        assertSame(controller.getGameOver(),gameOver,"The GameOver flag given isn't the same");
    }

    /**
     * getMatchID get method test
     */
    @Test
    public void getMatchIDTest(){
        int matchID = controller.getMatchID();
        assertSame(controller.getMatchID(),matchID,"The matchID int given isn't the same");
    }

}