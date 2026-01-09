package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.chat.PrefixManager;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class PrefixCommand implements CommandExecutor {

    private final PrefixManager prefixManager;

    public PrefixCommand(PrefixManager prefixManager) {
        this.prefixManager = prefixManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда доступна только игрокам.");
            return true;
        }

        if (!player.hasPermission("vaulteconomy.prefix")) {
            player.sendMessage("§cУ вас нет прав.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§e/prefix set <префикс>");
            player.sendMessage("§e/prefix reset");
            return true;
        }

        // ===== RESET =====
        if (args[0].equalsIgnoreCase("reset")) {
            prefixManager.resetPrefix(player);
            player.sendMessage("§aПрефикс сброшен.");
            return true;
        }

        // ===== SET =====
        if (args[0].equalsIgnoreCase("set")) {

            if (args.length < 2) {
                player.sendMessage("§cВведите префикс.");
                return true;
            }

            String prefix = ChatColor.translateAlternateColorCodes('&', args[1]);

            if (ChatColor.stripColor(prefix).length() > 8) {
                player.sendMessage("§cПрефикс не может быть длиннее 8 символов.");
                return true;
            }

            if (prefix.contains(" ")) {
                player.sendMessage("§cПрефикс не может содержать пробелы.");
                return true;
            }

            prefixManager.setPrefix(player, prefix);
            player.sendMessage("§aПрефикс установлен: §r" + prefix);
            return true;
        }

        return true;
    }
}