package exceptions;

public class FileAlreadyUploadedException extends Exception {

    public FileAlreadyUploadedException() {
        super("File il present on the tracker ");
    }
}