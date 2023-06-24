package it.polimi.ingsw.exceptions;

import java.rmi.RemoteException;

/**
 * Class RMIClientDisconnectionException is thrown as RemoteException.
 */
public class RMIClientDisconnectionException extends RemoteException {
    /**
     * Constructor for RMIClientDisconnectionException class with the specific message of the error.
     */
    public RMIClientDisconnectionException(){super("RMI Client disconnection");}
}
