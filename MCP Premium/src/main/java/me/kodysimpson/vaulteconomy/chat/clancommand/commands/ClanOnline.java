package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.stream.Collectors;

public class ClanOnline {

    private final ClanManager clanManager;

    public ClanOnline(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void execute(Player player) {

        if (!clanManager.hasClan(player.getUniqueId())) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(player.getUniqueId());

        var online = clan.getMembers().stream()
                .map(Bukkit::getPlayer)
                .filter(p -> p != null && p.isOnline())
                .map(Player::getName)
                .collect(Collectors.toList());

        player.sendMessage("§6§lКлан: §e" + clan.getName());
        player.sendMessage("§7Онлайн §f(" + online.size() + "/" + clan.getMembers().size() + "):");

        if (online.isEmpty()) {
            player.sendMessage("§8— §7Нет игроков онлайн");
        } else {
            online.forEach(name ->
                    player.sendMessage(" §a✔ §f" + name)
            );
        }
    }
}
