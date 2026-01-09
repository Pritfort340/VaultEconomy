package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.UUID;

public class ClanUpdater extends BukkitRunnable {

    private final ClanManager clanManager;


    public ClanUpdater(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    @Override
    public void run() {

        /* ===================== ПРОХОД ПО КЛАНАМ ===================== */
        Iterator<Clan> iterator = clanManager.getAllClans().iterator();

        while (iterator.hasNext()) {
            Clan clan = iterator.next();

            /* ===== Удаление пустых кланов ===== */
            if (clan.getMembers().isEmpty()) {
                clanManager.deleteClan(clan);
                continue;
            }

            /* ===== Проверка лидера ===== */
            if (!clan.getMembers().contains(clan.getLeader())) {
                UUID newLeader = clan.getMembers().iterator().next();
                clan.setLeader(newLeader);

                Player player = Bukkit.getPlayer(newLeader);
                if (player != null) {
                    player.sendMessage("§6Ты стал новым лидером клана §f" + clan.getName());
                }
            }

            /* ===== ВОЙНЫ: автоочистка ===== */
            clan.getWars().removeIf(
                    warClanName -> clanManager.getClanByName(warClanName) == null
            );
        }
    }

}