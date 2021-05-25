package interfaces;

import exceptions.FileAlreadyUploadedException;

import javax.ejb.Remote;
import java.io.IOException;

@Remote
public interface ISingletonErlangBean {
    public boolean verifyPreviousUploads(String filename, String pid_user) throws IOException, FileAlreadyUploadedException;

    boolean addUsertoTracker(String filename, String username, String pid, String address) throws IOException;
}