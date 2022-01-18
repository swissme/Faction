package cc.frozengames.nitrogen.core.cooldown;

import cc.frozengames.flib.time.TimeUtils;
import lombok.Data;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Swiss (swiss@frozengames.cc)
 * 9/14/2021 / 4:46 PM
 * Zinc / cc.frozengames.zinc.cooldown
 */

@Data
public class Cooldown {
    private HashMap<UUID, Long> cooldownMap = new HashMap<>();

    private final String name;
    private long time;

    public Cooldown(String name) {
        this.name = name;
    }

    public void applyCooldown(Player player) {
        cooldownMap.put(player.getUniqueId(), System.currentTimeMillis() + (time * 1000));
    }

    public boolean isCooldown(Player player) {
        return cooldownMap.containsKey(player.getUniqueId()) && (cooldownMap.get(player.getUniqueId()) >= System.currentTimeMillis());
    }

    public String getRemainingCooldown(Player player) {
        return TimeUtils.formatTime(cooldownMap.get(player.getUniqueId()) - System.currentTimeMillis());
    }

    public void removeCooldown(Player player) {
        cooldownMap.remove(player.getUniqueId());
    }
}
