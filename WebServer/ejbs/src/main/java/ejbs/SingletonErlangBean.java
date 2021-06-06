package ejbs;

import com.sun.security.auth.NTSidGroupPrincipal;
import db.DAO.TrackerDAO;
import db.DAO.UserDAO;
import db.HttpConnection;
import db.ITrackerDAO;
import db.IUserDAO;
import db.dbConnector;
import entities.User;
import exceptions.FileAlreadyUploadedException;
import exceptions.FileNotAddedException;
import exceptions.NotSuccededInsertion;
import exceptions.UserNotFoundException;
import interfaces.ISingletonErlangBean;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.ejb.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

//@Singleton(name = "SingletonErlangBeanEJB")
@ConcurrencyManagement (ConcurrencyManagementType.CONTAINER)
@Singleton
public class SingletonErlangBean implements ISingletonErlangBean {

    private final HttpConnection conn = new HttpConnection();
    private final ITrackerDAO iTrackerDAO = new TrackerDAO(dbConnector.getInstance());
    private final IUserDAO iUserDAO = new UserDAO(dbConnector.getInstance());
    //fare la stessa cosa per i files

    public SingletonErlangBean() throws IOException {
    }

    @Lock(LockType.WRITE)
    @Override
    public boolean verifyPreviousUploadsAndInsert(String filename, String pid, String API_token, String username, String address, String size) throws IOException, FileAlreadyUploadedException, NotSuccededInsertion, FileNotAddedException {
        Logger logger = Logger.getLogger(getClass().getName());
        String tracker_port = iTrackerDAO.getTracker(filename);
        boolean isPresent = false;
        if (tracker_port != "null") {
            logger.info("[DEBUG] " + tracker_port);
            JSONObject list = null;
            try {
                list = conn.make_GET_request(filename, tracker_port, API_token);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ArrayList<JSONObject> peer = (ArrayList<JSONObject>) list.get("peers");
            for (JSONObject item : peer) {
                logger.info("[DEBUG]" + item.get("pid"));
                if (item.get("pid").equals(pid)) {
                    throw new FileAlreadyUploadedException();
                }
            }
            isPresent = true;
        }
        if (isPresent) {
            logger.info("[DEBUG] File is present");
            boolean insertion = this.addUsertoTracker(filename, username, pid, address, API_token);
            if (!insertion) {
                throw new NotSuccededInsertion();
            } else return insertion;
        }
        else {
            logger.info("[DEBUG] File is not present");
            try {
                this.assignToTrackerAndInsert(filename, username, pid, address, size, API_token);
                logger.info("[DEBUG] File has been correctly inserted");
            } catch(FileNotAddedException e) {
                return false;
            }
        }
        return true;
    }

    @Lock(LockType.WRITE)
    @Override
    public boolean addUsertoTracker(String filename, String username, String pid, String address, String token) throws IOException {
        Logger logger = Logger.getLogger(getClass().getName());
        String port = iUserDAO.getUserPort(username);
        String tracker_port = iTrackerDAO.getTracker(filename);
        try {
            conn.make_POST_request(pid, filename, address, port, tracker_port, token);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Lock(LockType.WRITE)
    @Override
    public void assignToTrackerAndInsert(String filename, String username, String pid, String address, String size, String token) throws IOException, FileNotAddedException {
        String port = iUserDAO.getUserPort(username);
        int n = ThreadLocalRandom.current().nextInt(0, 2 + 1);
        String tracker_port = null;
        if (n == 0) tracker_port = "8081";
        else if (n == 1) tracker_port = "8082";
        else if (n == 2) tracker_port = "8083";
        //String tracker_port = "8081";
        String trackerAddressAndPort = "127.0.0.1:" + tracker_port;
        boolean result = iTrackerDAO.insertNewUserForNewFile(filename, port, pid, address, size, token, tracker_port);
        if (!result)
            throw new FileNotAddedException();
    }

    @Lock(LockType.READ)
    @Override
    public String getTrackerAddrAndPort(String filename) {
        String tracker_port = iTrackerDAO.getTracker(filename);
        String addressAndPort = "127.0.0.1:" + tracker_port;
        return addressAndPort;
    }
}
