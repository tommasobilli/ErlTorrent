package exceptions;

public class PasswordNotMatchException extends Exception{

    public PasswordNotMatchException() {
        super();
    }

    public PasswordNotMatchException(String username) {
        super("User with username: " + username + " did not enter the confirm the password correctly");
    }

}

