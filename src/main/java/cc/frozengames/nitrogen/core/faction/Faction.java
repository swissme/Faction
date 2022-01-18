package cc.frozengames.nitrogen.core.faction;

import cc.frozengames.nitrogen.core.mongo.MongoHandler;
import com.mongodb.client.model.Filters;
import lombok.Data;
import org.bson.Document;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Swiss (swiss@frozengames.cc)
 * 9/19/2021 / 7:32 PM
 * Nitrogen / cc.frozengames.nitrogen.core.faction
 */

@Data
public class Faction {

    private static HashMap<String, Faction> factions = new HashMap<>();

    private String name;
    private String lowercase;
    private UUID leader;
    private List<UUID> members = new ArrayList<>();

    public Faction(String name) {
        this.name = name;

        this.load();
    }

    public void load() {
        Document document = MongoHandler.getFactions().find(Filters.eq("name", this.name)).first();
        if (document != null) {
            this.name = document.getString("name");
            this.lowercase = document.getString("lowercase");
            this.leader = UUID.fromString(document.getString("leader"));
            this.members = document.getList("members", String.class).stream().map(UUID::fromString).collect(Collectors.toList());
            factions.put(this.name, this);
            return;
        }

        document = new Document();
        document.append("name", this.name);
        document.append("lowercase", this.name.toLowerCase());
        document.append("members", this.members);
        MongoHandler.getFactions().insertOne(document);
        factions.put(this.name, this);
    }

    public void save() {
        Document document = new Document();
        document.append("name", this.name);
        document.append("lowercase", this.name.toLowerCase());
        document.append("leader", this.leader.toString());
        document.append("members", this.members.stream().map(UUID::toString).collect(Collectors.toList()));
        MongoHandler.getFactions().findOneAndReplace(Filters.eq("name", this.name), document);
    }

    public static Faction getByName(String name) {
        return factions.getOrDefault(name, new Faction(name));
    }

    public static boolean exists(String name) {
        return factions.containsKey(name);
    }

    public static void deleteFaction(Faction faction) {
        factions.remove(faction.getName(), faction);
        MongoHandler.getFactions().deleteOne(Filters.eq("name", faction.getName()));
    }

}
