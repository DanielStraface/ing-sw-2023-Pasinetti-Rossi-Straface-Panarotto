package it.polimi.ingsw;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class AppClientImpl extends AppClient{
    private static final int TYPE_OF_MATCH_POSITION = 0;
    private static final int NUMBER_OF_PLAYERS_POSITION = 1;
    private static final int TYPE_OF_CONNECTION_POSITION = 2;
    private static final int RMI_NETWORK = 1;
    private static final int SOCKET_NETWORK = 2;
    private static final int QUIT_IN_APPLCLIENTIMPL_ERROR = 2;
    public static void startClient() {
        List<Integer> decisions = welcome();
        String[] elements = new String[decisions.size()];
        System.out.println(decisions.size());
        elements[elements.length - 1] = AppClientImpl.nickname;
        elements[TYPE_OF_MATCH_POSITION] = decisions.get(TYPE_OF_MATCH_POSITION).toString();
        elements[NUMBER_OF_PLAYERS_POSITION] = decisions.get(NUMBER_OF_PLAYERS_POSITION).toString();
        switch(decisions.get(TYPE_OF_CONNECTION_POSITION)){
            case RMI_NETWORK -> {
                try {
                    AppClientRMI.main(elements);
                } catch (RemoteException e) {
                    System.err.println("Something went wrong with network connections: " + e.getMessage());
                } catch (NotBoundException e) {
                    System.err.println("Something went wrong with String[] args parameters: " + e.getMessage());
                }
            }
            case SOCKET_NETWORK -> {
                try {
                    AppClientSocket.main(elements);
                } catch (RemoteException e) {
                    System.err.println("Something went wrong with network connections: " + e.getMessage());
                }
            }
            default -> {
                System.out.println("Something went wrong! Closing...\nTERMINATED");
                System.exit(QUIT_IN_APPLCLIENTIMPL_ERROR);
            }
        }
    }
}
