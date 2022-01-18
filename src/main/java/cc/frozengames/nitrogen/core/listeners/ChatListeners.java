package cc.frozengames.nitrogen.core.listeners;

import cc.frozengames.flib.chat.CC;
import cc.frozengames.flib.chat.Message;
import cc.frozengames.nitrogen.core.faction.Faction;
import cc.frozengames.nitrogen.core.mongo.MongoHandler;
import cc.frozengames.nitrogen.core.player.Profile;
import com.mongodb.client.model.Filters;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Swiss (swiss@frozengames.cc)
 * 9/19/2021 / 8:22 PM
 * Nitrogen / cc.frozengames.nitrogen.core.listeners
 */
public class ChatListeners implements Listener {

    @EventHandler
    public void onAsyncChatEvent(AsyncPlayerChatEvent event) {
        Profile profile = Profile.getProfileByUUID(event.getPlayer().getUniqueId());
        event.setFormat(CC.translate(
                        (profile.hasFaction() ? "&8[&c" + profile.getFaction().getName() + "&8] " : "&8[&c*&8] ")
                        + "&f" + event.getPlayer().getName() + "&7: &f" + event.getMessage()));
    }
}
