package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.modules.teleport.WarpManager;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final WarpManager warpManager;
    private final PvpManager pvpManager;

    public SpawnCommand(WarpManager warpManager, PvpManager pvpManager) {
        this.warpManager = warpManager;
        this.pvpManager = pvpManager;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cКоманда только для игроков.");
            return true;
        }

        // ❌ запрет во время PvP
        if (pvpManager.isInPvp(p) && !p.hasPermission("vaulteconomy.spawn.pvp.bypass")) {
            p.sendMessage(color("&c❌ Нельзя использовать &e/spawn &cво время PvP!"));
            return true;
        }

        Location spawn = warpManager.getSpawn();
        if (spawn == null) {
            p.sendMessage(color("&c❌ Спавн еще не установлен."));
            return true;
        }

        p.teleport(spawn);
        p.sendMessage(color("&a✔ Вы телепортированы на спавн."));
        return true;
    }
}