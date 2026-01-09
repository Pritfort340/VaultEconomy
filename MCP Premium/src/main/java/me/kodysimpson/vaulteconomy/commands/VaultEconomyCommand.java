package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.news.command.auction.AuctionMessages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class VaultEconomyCommand implements CommandExecutor {

    private final VaultEconomy plugin;

    public VaultEconomyCommand(VaultEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        /* ===================== PERMISSION ===================== */

        if (!sender.hasPermission("vaulteconomy.admin")) {
            sender.sendMessage(color("&c❌ У вас нет прав."));
            return true;
        }

        /* ===================== ARGUMENTS ===================== */

        if (args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(color("&cИспользование: &f/ve reload"));
            return true;
        }

        long start = System.currentTimeMillis();

        /* ===================== RELOAD ===================== */

        try {
            // основной reload
            plugin.reloadAll();

            // auction-messages.yml
            AuctionMessages.reload(plugin);

        } catch (Exception e) {
            sender.sendMessage(color("&c❌ Ошибка при перезагрузке! Проверь консоль."));
            e.printStackTrace();
            return true;
        }

        long time = System.currentTimeMillis() - start;

        /* ===================== SUCCESS ===================== */

        sender.sendMessage(color("&a✔ VaultEconomy полностью перезагружен!"));
        sender.sendMessage(color("&7Время: &e" + time + " мс"));

        return true;
    }

    /* ===================== COLOR ===================== */

    private String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}