package exceptions;

public class NotSuccededInsertion extends Exception {

    public NotSuccededInsertion() {
        super("File is present on the tracker ");
    }
}