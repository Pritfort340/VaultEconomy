package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.PrefixManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PexCommand implements CommandExecutor {

    private final VaultEconomy plugin;
    private final PrefixManager prefixManager;

    public PexCommand(VaultEconomy plugin, PrefixManager prefixManager) {
        this.plugin = plugin;
        this.prefixManager = prefixManager;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("vaulteconomy.pex")) {
            sender.sendMessage(color("&cУ вас нет прав на эту команду."));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(color("&cИспользование: /pex <игрок> <set|default> [префикс]"));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target.getName() == null) {
            sender.sendMessage(color("&cИгрок не найден."));
            return true;
        }

        String action = args[1].toLowerCase();

        if (action.equals("default")) {
            prefixManager.resetPrefix(target);
            sender.sendMessage(color("&aПрефикс игрока &e" + target.getName() + " &aсброшен на стандартный."));
            return true;
        }

        if (action.equals("set")) {
            if (args.length < 3) {
                sender.sendMessage(color("&cИспользование: /pex <игрок> set <префикс>"));
                return true;
            }

            String prefix = args[2];

            // Ограничения: максимум 8 символов, без пробелов
            if (prefix.length() > 8) {
                sender.sendMessage(color("&cПрефикс не может быть длиннее 8 символов."));
                return true;
            }
            if (prefix.contains(" ")) {
                sender.sendMessage(color("&cПрефикс не может содержать пробелы."));
                return true;
            }

            prefixManager.setPrefix(target, prefix);
            sender.sendMessage(color("&aПрефикс игрока &e" + target.getName()
                    + " &aустановлен: &r" + color(prefix)));
            return true;
        }

        sender.sendMessage(color("&cНеизвестное действие. Используйте /pex <игрок> <set|default>"));
        return true;
    }
}