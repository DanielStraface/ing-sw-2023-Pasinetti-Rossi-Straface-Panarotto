package it.polimi.ingsw.exceptions;

/**
 * AnotherClientRMITimeoutException extends ClientRMITimeoutException, is thrown if client RMI has disconnected
 * during setup situations
 */
public class AnotherClientRMITimeoutException extends ClientRMITimeoutException {
    public AnotherClientRMITimeoutException(){super();}
}
