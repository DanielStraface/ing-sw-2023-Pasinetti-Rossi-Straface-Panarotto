package it.polimi.ingsw.exceptions;

/**
 * NoMoreHeartbeatException extends ClientRMITimeoutException, is thrown if client RMI has disconnected
 * during setup situations
        */
public class NoMoreHeartbeatException extends ClientRMITimeoutException {
    /**
     * constructor for NoMoreHeartbeatException
     */
    public NoMoreHeartbeatException(){super();}
}
