package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.modules.teleport.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetWarpCommand implements CommandExecutor {

    private final WarpManager warpManager;

    public SetWarpCommand(WarpManager warpManager) {
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

        if (!p.hasPermission("vaulteconomy.setwarp")) {
            p.sendMessage(color("&cУ вас нет прав на установку варпов."));
            return true;
        }

        if (args.length == 0) {
            p.sendMessage(color("&cИспользование: &e/setwarp <название>"));
            return true;
        }

        String name = args[0];
        warpManager.setWarp(name, p.getLocation());
        p.sendMessage(color("&aВарп &e" + name + " &aустановлен."));
        return true;
    }
}