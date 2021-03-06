package db.DAO;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import db.IFilesDAO;
import db.ITrackerDAO;
import db.DAO.FilesDAO;
import db.dbConnector;
import org.bson.Document;
import db.HttpConnection;

import javax.websocket.Session;
import java.io.IOException;
import java.net.ConnectException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

public class TrackerDAO implements ITrackerDAO {

    private final MongoCollection<Document> collection;
    private MongoClient client;
    private final HttpConnection conn = new HttpConnection();
    private final IFilesDAO iFilesDAO = new FilesDAO(dbConnector.getInstance());


    public TrackerDAO(dbConnector instance) throws IOException {
        this.collection = instance.getDatabase().getCollection("trackers");
        this.client = instance.getMongoClient();
    }
    @Override
    public String getTracker(String filename) {
        MongoCursor<Document> cursor = collection.find(Filters.eq("files", filename)).projection(Projections.fields(Projections.include("port"))).iterator();
        if (cursor.hasNext()) {
            Document doc = cursor.next();
            String port = doc.getString("port");
            return port;
        }
        return "null";
    }

    @Override
    public boolean insertNewUserForNewFile(String filename, String port, String pid, String address, String size, String token, String finalTracker_port) throws IOException {
        //scelta del tracker
        Logger logger = Logger.getLogger(getClass().getName());
        boolean result = false;
        final ClientSession clientSession = client.startSession();
        TransactionBody<Boolean> txnBody = new TransactionBody<Boolean>() {
            boolean check = true;

            public Boolean execute() {
                collection.updateOne(Filters.eq("port", finalTracker_port), Updates.addToSet("files", filename));
                iFilesDAO.insertFile(filename, size);

                try {
                    conn.make_POST_request(pid, filename, address, port, finalTracker_port, token);
                } catch (IOException ex) {
                    logger.info("[DEBUG] Unable to connect to Erlang server!");
                    check = false;
                }

                if (!check) {
                    collection.updateOne(Filters.eq("port", finalTracker_port), Updates.pull("files", filename));
                    iFilesDAO.deleteFile(filename);
                }
                return check;
            }
        };
        try {
            result = clientSession.withTransaction(txnBody);
        } finally {
            clientSession.close();
        }
        return result;
    }
}
