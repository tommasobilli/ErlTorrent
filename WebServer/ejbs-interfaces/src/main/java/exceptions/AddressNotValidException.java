package exceptions;

public class AddressNotValidException extends Exception {

    public AddressNotValidException() {
        super("User did not enter the correct address");
    }
}