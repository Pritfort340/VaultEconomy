package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanRole;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanSetHome {

    private final ClanManager clanManager;

    public ClanSetHome(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void execute(Player player) {

        UUID uuid = player.getUniqueId();

        if (!clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(uuid);

        ClanRole role = clanManager.getRole(uuid);
        if (role != ClanRole.LEADER && role != ClanRole.OFFICER) {
            player.sendMessage("§cТолько лидер или офицер может установить дом клана.");
            return;
        }

        clan.setHome(player.getLocation());
        player.sendMessage("§aДом клана успешно установлен!");
    }
}