package cc.frozengames.nitrogen.core.cooldown;

import cc.frozengames.flib.time.TimeUtils;
import cc.frozengames.nitrogen.Nitrogen;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

/**
 * @author Swiss (swiss@frozengames.cc)
 * 9/14/2021 / 4:45 PM
 * Zinc / cc.frozengames.zinc.cooldown
 */

@RequiredArgsConstructor
public class CooldownManager {

    private final Nitrogen nitrogen;

    private HashMap<String, Cooldown> cooldownHashMap = new HashMap<>();

    public Cooldown getByName(String cooldownName) {
        for (Cooldown cooldown : cooldownHashMap.values()) {
            if(cooldown.getName().equalsIgnoreCase(cooldownName))
                return cooldown;
        }
        Cooldown cooldown = new Cooldown(cooldownName);
        cooldownHashMap.put(cooldownName, cooldown);
        return cooldown;
    }

    public Cooldown getInviteCooldown() {
        if (!cooldownHashMap.containsKey("InviteCooldown")) {
            Cooldown cooldown = new Cooldown("InviteCooldown");
            cooldown.setTime(TimeUtils.parseTime(nitrogen.getFactionSettings().getString("SETTINGS.INVITE-COOLDOWN")));
            cooldownHashMap.put(cooldown.getName(), cooldown);
            return cooldown;
        }
        return cooldownHashMap.get("InviteCooldown");
    }

}
