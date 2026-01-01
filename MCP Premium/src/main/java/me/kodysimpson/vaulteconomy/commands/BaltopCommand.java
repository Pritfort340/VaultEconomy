package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.economy.CustomEconomy;
import me.kodysimpson.vaulteconomy.economy.PointManager;
import me.kodysimpson.vaulteconomy.economy.PointCurrency;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class BaltopCommand implements CommandExecutor {

    private final VaultEconomy plugin;
    private final CustomEconomy economy;
    private final PointManager pointManager;

    public BaltopCommand(VaultEconomy plugin) {
        this.plugin = plugin;
        this.economy = plugin.getEconomy();
        this.pointManager = plugin.getPointManager();
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("vaulteconomy.baltop")) {
            sender.sendMessage(color("&cУ вас нет прав для этой команды."));
            return true;
        }

        // /baltop - основная валюта
        if (args.length == 0) {
            showMainBaltop(sender);
            return true;
        }

        // /baltop <currency> - мультивалюта
        String currencyName = args[0].toLowerCase();
        PointCurrency currency = pointManager.getCurrency(currencyName);
        if (currency == null) {
            sender.sendMessage(color("&cВалюта &e" + currencyName + " &cне найдена."));
            return true;
        }
        showPointBaltop(sender, currency);
        return true;
    }

    private void showMainBaltop(CommandSender sender) {
        String symbol = plugin.getConfig().getString("currency.symbol", "⛁");

        // Получаем всех игроков и их балансы
        List<Map.Entry<OfflinePlayer, Double>> top = new ArrayList<>();
        for (OfflinePlayer p : plugin.getServer().getOfflinePlayers()) {
            double balance = economy.getBalance(p);
            top.add(new AbstractMap.SimpleEntry<>(p, balance));
        }

        // Сортируем по балансу (по убыванию)
        top.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        sender.sendMessage(color("&6&l===== &eТОП БАЛАНСОВ &6&l====="));
        sender.sendMessage(color("&7Всего игроков: &e" + top.size()));

        int limit = Math.min(10, top.size());
        for (int i = 0; i < limit; i++) {
            Map.Entry<OfflinePlayer, Double> entry = top.get(i);
            OfflinePlayer player = entry.getKey();
            double balance = entry.getValue();

            String place = getPlaceEmoji(i + 1);
            String name = player.getName() != null ? player.getName() : "Неизвестно";
            String balLine = String.format("%s &e%s &7- &6%.1f%s", place, name, balance, symbol);
            sender.sendMessage(color(balLine));
        }
    }

    private void showPointBaltop(CommandSender sender, PointCurrency currency) {
        sender.sendMessage(color("&6&l===== &eТОП " + currency.getDisplayName() + " &6&l====="));
        sender.sendMessage(color("&7Символ: &6" + currency.getSymbol() + " &7Всего игроков: &e" + currency.getBalances().size()));

        // Сортируем балансы по убыванию
        List<Map.Entry<String, Double>> top = new ArrayList<>(currency.getBalances().entrySet());
        top.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        int limit = Math.min(10, top.size());
        for (int i = 0; i < limit; i++) {
            Map.Entry<String, Double> entry = top.get(i);
            String playerKey = entry.getKey();
            double balance = entry.getValue();

            String place = getPlaceEmoji(i + 1);
            String name = getPlayerName(playerKey);
            String balLine = String.format("%s &e%s &7- &6%.1f%s", place, name, balance, currency.getSymbol());
            sender.sendMessage(color(balLine));
        }
    }

    private String getPlaceEmoji(int place) {
        return switch (place) {
            case 1 -> "&6🥇";
            case 2 -> "&7🥈";
            case 3 -> "&e🥉";
            default -> "&f" + place + ".&r";
        };
    }

    private String getPlayerName(String uuidOrName) {
        try {
            OfflinePlayer player = plugin.getServer().getOfflinePlayer(UUID.fromString(uuidOrName));
            return player.getName() != null ? player.getName() : uuidOrName;
        } catch (IllegalArgumentException e) {
            return uuidOrName; // если это уже имя
        }
    }
}