package exceptions;

public class PortNotCorrectException extends Exception {
    public PortNotCorrectException() {
        super();
    }

    public PortNotCorrectException(String username) {
        super("User with username: " + username + " did not enter the correct listening port");
    }
}