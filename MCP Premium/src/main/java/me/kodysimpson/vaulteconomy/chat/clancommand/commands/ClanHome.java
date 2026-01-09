package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanHome {

    private final ClanManager clanManager;

    public ClanHome(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void execute(Player player) {

        UUID uuid = player.getUniqueId();

        if (!clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(uuid);

        if (!clan.hasHome()) {
            player.sendMessage("§cУ клана не установлен дом.");
            return;
        }

        player.teleport(clan.getHome());
        player.sendMessage("§aТы телепортирован в дом клана.");
    }
}