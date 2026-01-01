package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.modules.teleport.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHomeCommand implements CommandExecutor {

    private final WarpManager warpManager;
    private final VaultEconomy plugin;

    public SetHomeCommand(WarpManager warpManager, VaultEconomy plugin) {
        this.warpManager = warpManager;
        this.plugin = plugin;
    }

    private int getMaxHomes(Player player) {
        if (player.hasPermission("vaulteconomy.admin")) {
            return plugin.getConfig().getInt("homes.max-homes.admin", 999);
        }
        if (player.hasPermission("vaulteconomy.premium")) {
            return plugin.getConfig().getInt("homes.max-homes.premium", 8);
        }
        if (player.hasPermission("vaulteconomy.vip")) {
            return plugin.getConfig().getInt("homes.max-homes.vip", 5);
        }
        return plugin.getConfig().getInt("homes.max-homes.default", 2);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Команда только для игроков.");
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("vaulteconomy.sethome")) {
            player.sendMessage(ChatColor.RED + plugin.getConfig().getString("messages.no-permission", "&cУ вас нет прав"));
            return true;
        }

        String homeName = args.length == 0 ? "home" : args[0].toLowerCase();

        int maxHomes = getMaxHomes(player);
        List<String> homes = warpManager.getHomes(player);

        if (!homes.contains(homeName) && homes.size() >= maxHomes) {
            String msg = plugin.getConfig().getString("warp.messages.home-max-reached", "&cУ вас максимум домов ({max}). Удалите один через /delhome.")
                    .replace("{max}", String.valueOf(maxHomes));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        warpManager.setHome(player, homeName, player.getLocation());
        String msg = plugin.getConfig().getString("warp.messages.home-set", "&aДом '{name}' установлен.")
                .replace("{name}", homeName);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        return true;
    }
}