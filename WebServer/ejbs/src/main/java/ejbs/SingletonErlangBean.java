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
import exceptions.UserNotFoundException;
import interfaces.ISingletonErlangBean;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javax.ejb.*;
import java.io.IOException;
import java.util.ArrayList;
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

    @Lock(LockType.READ)
    @Override
    public boolean verifyPreviousUploads(String filename, String pid, String token) throws IOException, FileAlreadyUploadedException {
        Logger logger = Logger.getLogger(getClass().getName());
        String tracker_port = iTrackerDAO.getTracker(filename);
        if (tracker_port == "null") return false;
        logger.info("[DEBUG] " + tracker_port);
        JSONObject list = null;
        try {
            list = conn.make_GET_request(filename, tracker_port, token);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ArrayList<JSONObject> peer = (ArrayList<JSONObject>) list.get("peers");
        for (JSONObject item : peer) {
            logger.info("[DEBUG]" + item.get("pid"));
            if (item.get("pid").equals(pid))
                throw new FileAlreadyUploadedException();
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
    public String assignToTrackerAndInsert(String filename, String username, String pid, String address, String size, String token) throws IOException, FileNotAddedException {
        String port = iUserDAO.getUserPort(username);
        //int n = (int) (Math.random()*3);
        /*
        int n = ThreadLocalRandom.current().nextInt(0, 2 + 1);
        String tracker_port = null;
        if (n == 0) tracker_port = "8081";
        else if (n == 1) tracker_port = "8082";
        else if (n == 2) tracker_port = "8083";
        */
        String tracker_port = "8081";
        String trackerAddressAndPort = "127.0.0.1:" + tracker_port;
        boolean result = iTrackerDAO.insertNewUserForNewFile(filename, port, pid, address, size, token, tracker_port);
        if (result == false)
            throw new FileNotAddedException();
        return trackerAddressAndPort;
    }

    @Lock(LockType.WRITE)
    @Override
    public String getTrackerAddrAndPort(String filename) {
        String tracker_port = iTrackerDAO.getTracker(filename);
        String addressAndPort = "127.0.0.1:" + tracker_port;
        return addressAndPort;
    }
}
