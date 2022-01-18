package cc.frozengames.nitrogen.core.player;

import cc.frozengames.nitrogen.core.faction.Faction;
import cc.frozengames.nitrogen.core.mongo.MongoHandler;
import com.mongodb.client.model.Filters;
import lombok.Data;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author Swiss (swiss@frozengames.cc)
 * 9/19/2021 / 8:01 PM
 * Nitrogen / cc.frozengames.nitrogen.core.player
 */

@Data
public class Profile {

    private static HashMap<UUID, Profile> profiles = new HashMap<>();

    private UUID uuid;
    private Faction faction;

    public Profile(UUID uuid) {
        this.uuid = uuid;

        this.load();
    }

    public void load() {
        Document document = MongoHandler.getProfiles().find(Filters.eq("uuid", this.uuid.toString())).first();
        if( document != null ) {
            this.uuid = UUID.fromString(document.getString("uuid"));
            if(document.getString("faction") != null) {
                this.faction = Faction.getByName(document.getString("faction"));
                System.out.println("fda");
            }
            profiles.put(this.uuid, this);
            return;
        }

        document = new Document();
        document.append("uuid", this.uuid.toString());
        document.append("faction", null);
        MongoHandler.getProfiles().insertOne(document);
        profiles.put(this.uuid, this);
    }

    public void save() {
        Document document = new Document();
        document.append("uuid", this.uuid.toString());
        document.append("faction", this.faction == null ? null : this.faction.getName());
        MongoHandler.getProfiles().findOneAndReplace(Filters.eq("uuid", this.uuid.toString()), document);
    }

    public static Profile getProfileByUUID(UUID uuid) {
        if(profiles.containsKey(uuid)) {
            return profiles.get(uuid);
        }
        return new Profile(uuid);
    }

    public boolean hasFaction() {
        return this.faction != null;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public Faction getFaction() {
        return this.faction;
    }

}
