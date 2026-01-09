package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Хранит все приглашения в кланы
 * invitedPlayer -> Clan
 */
public class ClanRequests {

    private final Map<UUID, Clan> invites = new HashMap<>();

    /* ===================== ADD INVITE ===================== */

    public void invite(Player target, Clan clan) {
        invites.put(target.getUniqueId(), clan);
    }

    /* ===================== HAS INVITE ===================== */

    public boolean hasInvite(Player player) {
        return invites.containsKey(player.getUniqueId());
    }

    /* ===================== GET CLAN ===================== */

    public Clan getClan(Player player) {
        return invites.get(player.getUniqueId());
    }

    /* ===================== ACCEPT ===================== */

    public Clan accept(Player player) {
        return invites.remove(player.getUniqueId());
    }

    /* ===================== DENY ===================== */

    public Clan deny(Player player) {
        return invites.remove(player.getUniqueId());
    }

    /* ===================== CLEAR ===================== */

    public void clear(Player player) {
        invites.remove(player.getUniqueId());
    }
}