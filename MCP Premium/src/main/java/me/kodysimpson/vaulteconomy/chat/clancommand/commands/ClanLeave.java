package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanLeave {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    public ClanLeave(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    public void execute(Player player) {

        /* ===================== PvP BLOCK ===================== */
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя покинуть клан во время PvP!");
            return;
        }

        /* ===================== CLAN CHECK ===================== */
        UUID uuid = player.getUniqueId();

        if (!clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(uuid);
        if (clan == null) {
            player.sendMessage("§cОшибка: клан не найден.");
            return;
        }

        /* ===================== LEADER CHECK ===================== */
        if (clan.getLeader().equals(uuid)) {
            player.sendMessage("§cЛидер не может покинуть клан.");
            player.sendMessage("§7Используй §e/clan disband §7для удаления клана.");
            return;
        }

        /* ===================== LEAVE ===================== */
        clanManager.removeMember(clan, uuid);

        player.sendMessage("§aТы покинул клан §f" + clan.getName());

        /* ===================== NOTIFY MEMBERS ===================== */
        clan.getMembers().forEach(memberUuid -> {
            Player member = org.bukkit.Bukkit.getPlayer(memberUuid);
            if (member != null && member.isOnline()) {
                member.sendMessage("§e" + player.getName() + " §7покинул клан.");
            }
        });
    }
}