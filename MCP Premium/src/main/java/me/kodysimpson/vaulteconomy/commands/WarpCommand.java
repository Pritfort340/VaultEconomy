package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.modules.teleport.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    private final WarpManager warpManager;

    public WarpCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("Команда только для игроков.");
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(color("&cИспользование: &e/warp <название>"));
            return true;
        }

        String name = args[0];
        Location loc = warpManager.getWarp(name);
        if (loc == null) {
            p.sendMessage(color("&cВарп &e" + name + " &cне найден."));
            return true;
        }
        p.teleport(loc);
        p.sendMessage(color("&aВы телепортированы на варп &e" + name + "&a."));
        return true;
    }
}