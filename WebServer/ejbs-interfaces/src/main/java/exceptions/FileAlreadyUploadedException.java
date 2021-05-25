package exceptions;

public class FileAlreadyUploadedException extends Exception {

    public FileAlreadyUploadedException() {
        super("File is present on the tracker ");
    }
}