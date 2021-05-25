package db.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import db.ITrackerDAO;
import db.dbConnector;
import org.bson.Document;

public class TrackerDAO implements ITrackerDAO {

    private final MongoCollection<Document> collection;

    public TrackerDAO(dbConnector instance) {
        this.collection = instance.getDatabase().getCollection("trackers");
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
}
