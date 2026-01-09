package me.kodysimpson.vaulteconomy.news.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AnvilCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Команда только для игроков!");
            return true;
        }

        Player player = (Player) sender;
        player.openAnvil(player.getLocation(), true);
        player.sendMessage(ChatColor.GREEN + "🔨 Открыта виртуальная наковальня!");
        return true;
    }
}