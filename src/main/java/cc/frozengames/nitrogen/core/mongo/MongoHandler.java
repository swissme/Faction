package cc.frozengames.nitrogen.core.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

/**
 * @author Swiss (swiss@frozengames.cc)
 * 9/19/2021 / 7:35 PM
 * Nitrogen / cc.frozengames.nitrogen.core.mongo
 */
public class MongoHandler {

    @Getter
    private static MongoCollection<Document> factions;
    @Getter
    private static MongoCollection<Document> profiles;

    public MongoHandler() {
        MongoClient mongoClient;
        mongoClient = new MongoClient("localhost", 27017);

        MongoDatabase mongoDatabase = mongoClient.getDatabase("Nitrogen");
        factions = mongoDatabase.getCollection("factions");
        profiles = mongoDatabase.getCollection("profiles");
    }

}
