package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class BalanceCommand implements CommandExecutor {

    private final VaultEconomy plugin;

    public BalanceCommand(VaultEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        String symbol = plugin.getConfig().getString("currency.symbol", "⛁");

        // /bits
        if (args.length == 0) {
            if (!(sender instanceof Player p)) return true;

            double bal = plugin.getEconomy().getBalance(p);

            String msg = plugin.getConfig().getString("messages.balance", "&aБаланс: {balance}{symbol}")
                    .replace("{balance}", String.valueOf(bal))
                    .replace("{symbol}", symbol);

            p.sendMessage(color(msg));
            return true;
        }

        // /bits help
        if (args[0].equalsIgnoreCase("help")) {
            sender.sendMessage(color("&6===== VaultEconomy ====="));
            sender.sendMessage(color("&e/bits &7- баланс"));
            sender.sendMessage(color("&e/bits give <player> <amount>"));
            sender.sendMessage(color("&e/bits take <player> <amount>"));
            sender.sendMessage(color("&e/bits remove <player> <amount>"));
            sender.sendMessage(color("&e/bits set <player> <amount>"));
            sender.sendMessage(color("&e/bits reload"));
            sender.sendMessage(color("&7Алиасы: /bal /money"));
            return true;
        }

        // /bits reload
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("vaulteconomy.reload")) {
                sender.sendMessage(color(plugin.getConfig().getString("messages.no-permission")));
                return true;
            }
            plugin.reloadPluginConfig();
            sender.sendMessage(ChatColor.GREEN + "Конфиг перезагружен");
            return true;
        }

        // Админка
        if (!sender.hasPermission("vaulteconomy.admin")) {
            sender.sendMessage(color(plugin.getConfig().getString("messages.no-permission")));
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Использование: /bits <give|take|remove|set> <player> <amount>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
        if (target.getName() == null) {
            sender.sendMessage(color(plugin.getConfig().getString("messages.player-not-found")));
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(color(plugin.getConfig().getString("messages.not-a-number")));
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "give" -> {
                plugin.getEconomy().depositPlayer(target, amount);
                sender.sendMessage(color(
                        plugin.getConfig().getString("messages.admin-give")
                                .replace("{player}", target.getName())
                                .replace("{amount}", String.valueOf(amount))
                                .replace("{symbol}", symbol)
                ));
            }

            case "take", "remove" -> {
                EconomyResponse r = plugin.getEconomy().withdrawPlayer(target, amount);
                if (r.type == EconomyResponse.ResponseType.FAILURE) {
                    sender.sendMessage(color(plugin.getConfig().getString("messages.insufficient-funds")));
                    return true;
                }
                sender.sendMessage(color(
                        plugin.getConfig().getString("messages.admin-take")
                                .replace("{player}", target.getName())
                                .replace("{amount}", String.valueOf(amount))
                                .replace("{symbol}", symbol)
                ));
            }

            case "set" -> {
                double cur = plugin.getEconomy().getBalance(target);
                plugin.getEconomy().withdrawPlayer(target, cur);
                plugin.getEconomy().depositPlayer(target, amount);
                sender.sendMessage(ChatColor.GREEN +
                        "Баланс " + target.getName() + " установлен: " + amount + symbol);
            }

            default -> sender.sendMessage(ChatColor.RED + "Неизвестная команда. Используй /bits help");
        }

        return true;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}