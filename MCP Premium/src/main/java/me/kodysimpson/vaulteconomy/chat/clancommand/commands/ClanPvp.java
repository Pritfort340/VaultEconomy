package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanPvp {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    public ClanPvp(ClanManager clanManager, PvpManager pvpManager) {
        this.clanManager = clanManager;
        this.pvpManager = pvpManager;
    }

    public void execute(Player player) {

        UUID uuid = player.getUniqueId();

        /* ===== CLAN CHECK ===== */
        if (!clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя менять PvP клана во время PvP!");
            return;
        }

        Clan clan = clanManager.getClan(uuid);
        ClanMember member = clanManager.getMember(uuid);

        if (member == null) {
            player.sendMessage("§cОшибка данных участника.");
            return;
        }

        /* ===== PERMISSIONS ===== */
        if (!member.isOfficer()) {
            player.sendMessage("§cТолько лидер или офицер может менять PvP клана.");
            return;
        }

        /* ===== TOGGLE ===== */
        boolean newState = !clan.isFriendlyFire();
        clan.setFriendlyFire(newState);

        String status = newState ? "§cВКЛЮЧЕНО" : "§aВЫКЛЮЧЕНО";

        for (UUID memberId : clan.getMembers()) {
            Player online = Bukkit.getPlayer(memberId);
            if (online != null) {
                online.sendMessage("§6PvP внутри клана: " + status);
            }
        }
    }
}