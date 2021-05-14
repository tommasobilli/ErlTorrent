package com.example.WebServer;

import java.util.List;
import java.util.ArrayList;

import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.TransactionBody;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.ClientSession;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class Dao {

    private MongoCollection<Document> files;
    private MongoCollection<Document> trackers;
    private MongoCollection<Document> users;

    private Dao() {
        try {
            MongoClient mongoClient = MongoClients.create(
                    "mongodb+srv://studenti:Dido1996@cluster0.pcezh.mongodb.net/myFirstDatabase?authSource=admin&replicaSet=Cluster0-shard-0&w=majority&readPreference=primary&retryWrites=true&ssl=true");
            MongoDatabase database = mongoClient.getDatabase("WebTorrent");
            files = database.getCollection("files");
            trackers = database.getCollection("trackers");
            users = database.getCollection("users");
        } catch (Exception e) {
            System.err.println("Unable to connect to the database!");
            System.exit(-1);
        }
    }

    private static class MongoSingleton {
        private static final Dao INSTANCE = new Dao();
    }

    public static Dao getInstance() {
        return MongoSingleton.INSTANCE;
    }

    public Document getTrackerInfo(String filename) {
        try (MongoCursor<Document> cursor = trackers
                .find(Filters.eq("files", filename)).projection(Projections.fields(Projections
                        .include("address", "port"), Projections.exclude("_id")))
                .iterator()) {
            if (cursor.hasNext()) {
                return cursor.next();
            } else {
                return null;
            }
        }
    }

    public List<Document> searchFile(String query) {
        List<Document> result = new ArrayList<>();
        try (MongoCursor<Document> cursor = files
                .find(Filters.text(query)).projection(Projections.fields(Projections
                        .include("filename", "size"), Projections.exclude("_id")))
                .iterator()) {
            while (cursor.hasNext()) {
                result.add(cursor.next());
            }
            return result;
        }
    }


    public static void main(String[] args) {
        Dao dao = Dao.getInstance();
        Document result = dao.getTrackerInfo("Pulp Fiction");
        System.out.println(result.get("address"));
        List<Document> result2 = dao.searchFile("Pulp");
        for (Document d : result2) {
            System.out.println(d.get("size"));
        }
    }

}
