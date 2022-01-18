package cc.frozengames.nitrogen;

import cc.frozengames.flib.command.CommandHandler;
import cc.frozengames.flib.files.Config;
import cc.frozengames.nitrogen.core.commands.FactionCommands;
import cc.frozengames.nitrogen.core.cooldown.CooldownManager;
import cc.frozengames.nitrogen.core.faction.Faction;
import cc.frozengames.nitrogen.core.listeners.ChatListeners;
import cc.frozengames.nitrogen.core.listeners.ProfileListeners;
import cc.frozengames.nitrogen.core.mongo.MongoHandler;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

/**
 * @author Swiss (swiss@frozengames.cc)
 * 9/19/2021 / 7:30 PM
 * Nitrogen / cc.frozengames.nitrogen
 */
public class Nitrogen extends JavaPlugin {

    private CommandHandler commandHandler;
    private MongoHandler mongoHandler;
    @Getter
    private CooldownManager cooldownManager;

    @Getter
    private Config factionSettings;

    @Override
    public void onEnable() {
        mongoHandler = new MongoHandler();
        cooldownManager = new CooldownManager(this);

        loadListeners();
        loadCommands();
        loadFactions();
        loadConfigs();
    }

    @Override
    public void onDisable() {

    }

    public void loadListeners() {
        Arrays.asList(
                new ChatListeners(),
                new ProfileListeners()
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    public void loadCommands() {
        commandHandler = new CommandHandler(this);
        Arrays.asList(
                new FactionCommands()
        ).forEach(commands -> commandHandler.registerCommands(commands));
    }

    public void loadFactions() {
        for (Document document : MongoHandler.getFactions().find()) {
            new Faction(document.getString("name"));
        }
    }

    public void loadConfigs() {
        factionSettings = new cc.frozengames.flib.files.Config(this, "factionSettings");
    }

}
