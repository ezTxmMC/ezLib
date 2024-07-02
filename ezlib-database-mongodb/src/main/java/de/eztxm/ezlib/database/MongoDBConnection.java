package de.eztxm.ezlib.database;

import com.mongodb.client.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.concurrent.atomic.AtomicBoolean;

public class MongoDBConnection {
    private final MongoDatabase mongoDatabase;

    public MongoDBConnection(String host, int port, String username, String password, String database) {
        String uri = "mongodb://" + username + ":" + password + "@" + host + ":" + port;
        MongoClient mongoClient = MongoClients.create(uri);
        mongoDatabase = mongoClient.getDatabase(database);
    }

    public boolean createCollection(String collection) {
        if (!isCollectionExists(collection)) return false;
        mongoDatabase.createCollection(collection);
        return true;
    }

    public boolean isCollectionExists(String collection) {
        AtomicBoolean exists = new AtomicBoolean(false);
        mongoDatabase.listCollectionNames().forEach(string -> {
            if (!string.equals(collection)) {
                return;
            }
            exists.set(true);
        });
        return exists.get();
    }

    public MongoCollection<Document> getCollection(String name) {
        return mongoDatabase.getCollection(name);
    }

    public Document find(String collection, Bson filter) {
        return getCollection(collection).find(filter).first();
    }

    public FindIterable<Document> findMultiple(String collection, Bson filter) {
        return getCollection(collection).find(filter);
    }

    public void insert(String collection, Document insert) {
        getCollection(collection).insertOne(insert);
    }

    public void update(String collection, Bson filter, Document update) {
        getCollection(collection).updateOne(filter, update);
    }

    public void replace(String collection, Bson filter, Document update) {
        getCollection(collection).replaceOne(filter, update);
    }

    public void delete(String collection, Bson filter) {
        getCollection(collection).deleteOne(filter);
    }
}
