package db;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.Objects;


public class dbConnector {

    private static volatile dbConnector instance;
    private final MongoClient client;
    private final MongoDatabase database;
    private final CodecRegistry pojoCodecRegistry;
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";

    private dbConnector() {
        this.pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(
                        PojoCodecProvider.builder().automatic(true).build()
                )
        );

        this.client = MongoClients.create(CONNECTION_STRING);
        this.database = this.client.getDatabase("WebTorrent").withCodecRegistry(pojoCodecRegistry);
    }

    public static dbConnector getInstance() {
        if (instance == null) {
            synchronized (dbConnector.class) {
                if (instance == null) {
                    instance = new dbConnector();
                }
            }
        }
        return instance;
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}
