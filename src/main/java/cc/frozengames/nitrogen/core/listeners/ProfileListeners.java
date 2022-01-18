package cc.frozengames.nitrogen.core.listeners;

import cc.frozengames.nitrogen.core.player.Profile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Swiss (swiss@frozengames.cc)
 * 9/19/2021 / 8:50 PM
 * Nitrogen / cc.frozengames.nitrogen.core.listeners
 */
public class ProfileListeners implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Profile profile = Profile.getProfileByUUID(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Profile profile = Profile.getProfileByUUID(event.getPlayer().getUniqueId());
        profile.save();
    }
}
