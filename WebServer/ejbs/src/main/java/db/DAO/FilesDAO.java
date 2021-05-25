package db.DAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import db.IFilesDAO;
import db.dbConnector;
import org.bson.Document;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilesDAO implements IFilesDAO {

    private final MongoCollection<Document> collection;

    public FilesDAO(dbConnector instance) {
        this.collection = instance.getDatabase().getCollection("files");
    }

    @Override
    public Map<String, Map<String, String>> getFilesInfo(String search) {
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        try (MongoCursor<Document> cursor = collection
                .aggregate(Arrays.asList(Aggregates.match(Filters.text(search)),
                        Aggregates.lookup("trackers", "filename", "files", "tracker"),
                        Aggregates.project(Projections.fields(Projections
                                        .include("filename", "size", "tracker.address", "tracker.port"),
                                Projections.exclude("_id")))))
                .iterator()) {
            while (cursor.hasNext()) {
                Document result = cursor.next();
                Map<String, String> entry = new HashMap<>();

                List<Document> trackerInfo = result.getList("tracker", Document.class);
                String filename = result.getString("filename");
                String filesize = result.getString("size");
                String trackerAddress = trackerInfo.get(0).getString("address");
                String trackerPort = trackerInfo.get(0).getString("port");
                entry.put("filesize", filesize);
                entry.put("tracker_address", trackerAddress);
                entry.put("tracker_port", trackerPort);

                resultMap.put(filename, entry);
            }
            return resultMap;
        }
    }
}