package dev.eztxm.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dev.eztxm.database.util.IConnection;
import org.bson.Document;

import java.util.concurrent.atomic.AtomicBoolean;

public class MongoDBConnection implements IConnection {
    private final MongoDatabase mongoDatabase;

    public MongoDBConnection(String host, int port, String username, String password, String database) {
        String uri = "mongodb://" + username + ":" + password + "@" + host + ":" + port;
        MongoClient mongoClient = MongoClients.create(uri);
        mongoDatabase = mongoClient.getDatabase(database);
    }

    public boolean createCollection(String name) {
        if (!isCollectionExists(name)) return false;
        mongoDatabase.createCollection(name);
        return true;
    }

    public boolean isCollectionExists(String name) {
        AtomicBoolean exists = new AtomicBoolean(false);
        mongoDatabase.listCollectionNames().forEach(string -> {
            if (!string.equals(name)) {
                return;
            }
            exists.set(true);
        });
        return exists.get();
    }

    public MongoCollection<Document> getCollection(String name) {
        return mongoDatabase.getCollection(name);
    }

    public void set(String collection) {

    }
}
