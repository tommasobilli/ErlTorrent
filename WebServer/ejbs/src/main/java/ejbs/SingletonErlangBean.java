package ejbs;

import db.DAO.TrackerDAO;
import db.DAO.UserDAO;
import db.HttpConnection;
import db.ITrackerDAO;
import db.IUserDAO;
import db.dbConnector;
import exceptions.FileAlreadyUploadedException;
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
    public boolean verifyPreviousUploads (String filename, String user_pid) throws IOException, FileAlreadyUploadedException {
        Logger logger = Logger.getLogger(getClass().getName());
        String tracker_port = iTrackerDAO.getTracker(filename);
        if (tracker_port == "null") return false;
        logger.info("[DEBUG] " + tracker_port);
        JSONObject list = null;
        try {
            list = conn.make_GET_request(filename, tracker_port);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        logger.info("[DEBUG] ciaoooo!");
        ArrayList<JSONObject> peer= (ArrayList<JSONObject>) list.get("peers");
        for (JSONObject item : peer) {
            logger.info("[DEBUG]" + item.get("pid"));
            if (item.get("pid").equals(user_pid))
                throw new FileAlreadyUploadedException();
        }
        return true;
    }

    @Lock(LockType.WRITE)
    @Override
    public void addUsertoTracker(String filename, String username, String pid, String address) {
        Logger logger = Logger.getLogger(getClass().getName());
        logger.info("[DEBUG] entro");
        String port = iUserDAO.getUserPort(username);
        logger.info("[DEBUG]" + port);
    }
}
