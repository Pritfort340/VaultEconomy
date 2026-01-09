package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanInfo {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    public ClanInfo(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    public void execute(Player player) {

        /* ===================== PvP BLOCK ===================== */
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя использовать информацию о клане во время PvP!");
            return;
        }

        /* ===================== CLAN CHECK ===================== */
        if (!clanManager.hasClan(player.getUniqueId())) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cОшибка: клан не найден.");
            return;
        }

        /* ===================== INFO ===================== */
        player.sendMessage("§8§m----------------------------------");
        player.sendMessage("§6Клан: §f" + clan.getName());
        player.sendMessage("§6Лидер: §f" + getName(clan.getLeader()));
        player.sendMessage("§6Баланс: §f" + clan.getBalance());
        player.sendMessage("§6Участники: §f" + clan.getMembers().size());

        int online = 0;
        for (UUID uuid : clan.getMembers()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null && p.isOnline()) {
                online++;
            }
        }

        player.sendMessage("§6Онлайн: §a" + online);
        player.sendMessage("§8§m----------------------------------");
    }

    private String getName(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) return player.getName();

        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}