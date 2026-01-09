package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanRole;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanDelete {

    private final ClanManager clanManager;

    public ClanDelete(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void execute(Player player) {

        UUID uuid = player.getUniqueId();

        // ===== Проверка клана =====
        if (!clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(uuid);

        // ===== Проверка прав =====
        ClanRole role = clanManager.getRole(uuid);
        if (role != ClanRole.LEADER) {
            player.sendMessage("§cТолько лидер может удалить клан.");
            return;
        }

        // ===== Удаление =====
        clanManager.deleteClan(clan);

        player.sendMessage("§cКлан §f" + clan.getName() + " §cбыл удалён.");
    }
}