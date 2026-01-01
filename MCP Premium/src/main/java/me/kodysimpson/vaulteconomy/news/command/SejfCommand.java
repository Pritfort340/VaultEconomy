package me.kodysimpson.vaulteconomy.news.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.UUID;

public class SejfCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Команда только для игроков!");
            return true;
        }

        Player player = (Player) sender;
        Inventory safeInv = Bukkit.createInventory(null, 9 * 6, ChatColor.DARK_GREEN + "💰 Личный Сейф");
        player.openInventory(safeInv);
        player.sendMessage(ChatColor.GREEN + "💰 Открыт ваш личный сейф!");
        return true;
    }
}