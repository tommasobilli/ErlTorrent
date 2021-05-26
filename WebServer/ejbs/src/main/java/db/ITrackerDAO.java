package db;

import java.io.IOException;

public interface ITrackerDAO {
    String getTracker(String filename);

    boolean insertNewUserForNewFile(String filename, String port, String pid, String address, String size) throws IOException;
}
