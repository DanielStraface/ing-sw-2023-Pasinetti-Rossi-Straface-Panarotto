package it.polimi.ingsw.exceptions;

/**
 * Class ClientDisconnectionException is thrown when server doesn't receive messages from client.
 */
public class ClientDisconnectionException extends Exception{
    /**
     * Constructor for ClientDisconnectionException.
     */
    public ClientDisconnectionException(){
        super();
    }
}
