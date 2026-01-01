package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.modules.teleport.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    private final WarpManager warpManager;

    public SpawnCommand(WarpManager warpManager) {
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
            Location spawn = warpManager.getSpawn();
            if (spawn == null) {
                p.sendMessage(color("&cТочка спавна еще не установлена."));
                return true;
            }
            p.teleport(spawn);
            p.sendMessage(color("&aВы телепортированы на спавн."));
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("set")) {
            if (!p.hasPermission("vaulteconomy.spawn.set")) {
                p.sendMessage(color("&cУ вас нет прав на установку спавна."));
                return true;
            }
            warpManager.setSpawn(p.getLocation());
            p.sendMessage(color("&aСпавн установлен в вашей текущей позиции."));
            return true;
        }

        p.sendMessage(color("&cИспользование: &e/spawn &cили &e/spawn set"));
        return true;
    }
}