package cc.frozengames.nitrogen.core.commands;

import cc.frozengames.flib.chat.Message;
import cc.frozengames.flib.command.schema.annotations.Command;
import cc.frozengames.flib.command.schema.annotations.parameter.Param;
import cc.frozengames.flib.time.TimeUtils;
import cc.frozengames.nitrogen.Nitrogen;
import cc.frozengames.nitrogen.core.cooldown.Cooldown;
import cc.frozengames.nitrogen.core.faction.Faction;
import cc.frozengames.nitrogen.core.player.Profile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Swiss (swiss@frozengames.cc)
 * 9/19/2021 / 8:39 PM
 * Nitrogen / cc.frozengames.nitrogen.core.commands
 */
public class FactionCommands {

    private Nitrogen nitrogen;

    public Cooldown createCooldown(String name, String seconds) {
        Cooldown cooldown = nitrogen.getCooldownManager().getByName(name);
        cooldown.setTime(TimeUtils.parseTime(seconds));
        return cooldown;
    }

    @Command(labels = { "faction create" }, description = "Creates a faction", async = true)
    public void onFactionCreate(Player player, @Param("faction-name") String factionName) {
        Profile profile = Profile.getProfileByUUID(player.getUniqueId());
        if(profile.hasFaction()) {
            new Message("&cYou already have a faction!").send(player);
            return;
        }

        if(Faction.exists(factionName)) {
            new Message("&cThere is already a faction named &c&o" + factionName).send(player);
            return;
        }

        Faction faction = Faction.getByName(factionName);
        faction.setLeader(player.getUniqueId());
        faction.save();

        profile.setFaction(faction);
        profile.save();

        new Message("&aCreated a new faction named &a&o" + factionName).send(player);

    }

    @Command(labels = { "faction disband" }, description = "Disbands a faction", async = true)
    public void onFactionDisband(Player player) {
        Profile profile = Profile.getProfileByUUID(player.getUniqueId());
        if(!profile.hasFaction()) {
            new Message("&cYou are currently not in a faction").send(player);
            return;
        }

        if(profile.getFaction().getLeader() != player.getUniqueId()) {
            new Message("&cYou are not a leader of this faction").send(player);
            return;
        }

        new Message("&aSuccessfully disbanded your clan called &a&o" + profile.getFaction().getName()).send(player);
        Faction.deleteFaction(profile.getFaction());
        profile.setFaction(null);
        profile.save();
    }

    public Cooldown getInviteCooldown() {
        return createCooldown("InviteCooldown", nitrogen.getFactionSettings().getString("FACTIONS.INVITE-COOLDOWN"));
    }

    @Command(labels = { "faction invite" }, description = "Invites a member to a faction", async = true)
    public void onFactionInvite(Player player, @Param("player") String target) {
        Profile profile = Profile.getProfileByUUID(player.getUniqueId());
        Profile profileTarget = Profile.getProfileByUUID(Bukkit.getPlayer(target).getUniqueId());
        Player playerTarget = Bukkit.getPlayer(target);

        if(!profile.hasFaction() && (profile.getFaction().getLeader() != player.getUniqueId())) {
            new Message("&cYou do not own a faction.").send(player);
            return;
        }

        if(target.equals(player.getName())) {
            new Message("&cYou cannot invite yourself to the faction").send(player);
            return;
        }

        if(profileTarget.hasFaction()) {
            new Message("&c" + target + " &cis in a faction").send(player);
            return;
        }

        if(getInviteCooldown().isCooldown(playerTarget)) {
            new Message("&c" + target + " &cis already invited to a faction").send(player);
            return;
        }

        getInviteCooldown().applyCooldown(playerTarget);

        new Message("&aYou have successfully invited &a&o" + target + " &ato your faction!").send(player);

    }

    @Command(labels = { "faction join" }, description = "Joins a faction based on an invite", async = true)
    public void onFactionJoin(Player player, @Param("faction") String faction) {
        Profile profile = Profile.getProfileByUUID(player.getUniqueId());

        Faction factionObject = Faction.getByName(faction);
        factionObject.getMembers().add(player.getUniqueId());
        factionObject.save();

        profile.setFaction(factionObject);
        profile.save();
    }

}
