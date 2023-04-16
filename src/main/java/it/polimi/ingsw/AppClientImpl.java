package it.polimi.ingsw;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class AppClientImpl extends AppClient{
    public static void startClient() {
        List<Integer> decisions = welcome();
        String[] elements = new String[3];
        elements[elements.length - 1] = AppClientImpl.nickname;
        elements[0] = decisions.get(0).toString();
        elements[1] = decisions.get(1).toString();
        switch(decisions.get(decisions.size() - 1)){
            case 1 -> {
                try {
                    AppClientRMI.main(elements);
                } catch (RemoteException e) {
                    System.err.println("Something went wrong with network connections: " + e.getMessage());
                } catch (NotBoundException e) {
                    System.err.println("Something went wrong with String[] args parameters: " + e.getMessage());
                }
            }
            case 2 -> {
                try {
                    AppClientSocket.main(elements);
                } catch (RemoteException e) {
                    System.err.println("Something went wrong with network connections: " + e.getMessage());
                }
            }
            default -> {
                System.out.println("Something went wrong! Closing...");
                System.err.println("TERMINATED");
                System.exit(1);
            }
        }
    }
}
