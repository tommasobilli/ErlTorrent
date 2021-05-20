package exceptions;

public class IncorrectPasswordException extends Exception{
    public IncorrectPasswordException() {
        super();
    }

    public IncorrectPasswordException(String username) {
        super("User with username: " + username + " did not enter the correct password");
    }
}
