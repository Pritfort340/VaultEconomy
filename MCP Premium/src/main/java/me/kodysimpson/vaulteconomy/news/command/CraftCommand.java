package me.kodysimpson.vaulteconomy.news.command;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CraftCommand implements CommandExecutor {

    private final VaultEconomy plugin;

    public CraftCommand(VaultEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Команда только для игроков!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("vaulteconomy.craft")) {
            player.sendMessage(ChatColor.RED + "У вас нет прав!");
            return true;
        }

        // ✅ ПРОСТОЙ И НАДЕЖНЫЙ СПОСОБ - 3 ряда по 9 = 27 слотов
        // Верхние 2 ряда (18 слотов) = crafting grid 3x3
        // Нижний ряд (9 слотов) = результат + инвентарь игрока
        player.openWorkbench(null, true);

        String msg = plugin.getConfig().getString("utilities.craft.opened", "&a✅ Открыт виртуальный верстак!");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        return true;
    }
}