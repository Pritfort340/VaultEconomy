package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanReload {

    private final VaultEconomy plugin;

    public ClanReload(VaultEconomy plugin) {
        this.plugin = plugin;
    }

    public void execute(CommandSender sender) {

        if (sender instanceof Player player && !player.hasPermission("clan.admin")) {
            player.sendMessage("§cУ тебя нет прав.");
            return;
        }

        plugin.reloadConfig();

        sender.sendMessage("§aКонфигурация кланов перезагружена.");
    }
}