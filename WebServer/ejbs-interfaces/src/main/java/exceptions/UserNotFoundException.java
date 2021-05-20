package exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String username) {
        super("User with username: " + username + " Not Found");
    }

}
