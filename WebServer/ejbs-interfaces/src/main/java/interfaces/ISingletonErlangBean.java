package interfaces;

import exceptions.FileAlreadyUploadedException;
import exceptions.FileNotAddedException;
import exceptions.UserNotFoundException;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Remote;
import java.io.IOException;

@Remote
public interface ISingletonErlangBean {

    @Lock(LockType.READ)
    boolean verifyPreviousUploads(String filename, String pid, String token) throws IOException, FileAlreadyUploadedException;

    boolean addUsertoTracker(String filename, String username, String pid, String address, String token) throws IOException;

    void assignToTrackerAndInsert(String filename, String username, String pid, String address, String size, String token) throws IOException, FileNotAddedException;
}