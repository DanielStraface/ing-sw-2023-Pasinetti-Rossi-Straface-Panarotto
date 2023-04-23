package it.polimi.ingsw;

import it.polimi.ingsw.distributed.ClientImpl;
import it.polimi.ingsw.distributed.socket.middleware.ServerStub;
import it.polimi.ingsw.exceptions.NotMessageFromServerYet;

import java.rmi.RemoteException;

public class AppClientSocket {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 1234;
    private static final int TYPE_OF_MATCH_POSITION = 0;
    private static final int NUMBER_OF_PLAYER_POSITION = 1;
    private static final int NICKNAME_POSITION = 2;
    private static final Integer JOINING_EXISTING_GAME = 0;
    private static final int QUIT_IN_APPLCLIENTSOCKET_ERROR = 5;
    public static void main(String[] args) throws RemoteException {
        String nickname = args[NICKNAME_POSITION];
        ServerStub serverStub = new ServerStub(SERVER_ADDRESS, SERVER_PORT);
        ClientImpl client = null;
        System.err.println(args[TYPE_OF_MATCH_POSITION]);
        switch (args[TYPE_OF_MATCH_POSITION]) {
            case "1" -> {
                client = new ClientImpl(serverStub,
                        Integer.parseInt(args[NUMBER_OF_PLAYER_POSITION]), nickname);
            }
            case "2" -> {
                client = new ClientImpl(serverStub, JOINING_EXISTING_GAME, nickname);
            }
            default -> {
                System.exit(QUIT_IN_APPLCLIENTSOCKET_ERROR);
            }
        }
        ClientImpl finalClient = client;
        new Thread(() -> {
            while (true) {
                try {
                    serverStub.receive(finalClient);
                } catch (NotMessageFromServerYet e) {
                } catch (RemoteException e) {
                }
            }
        }).start();
    }
}
