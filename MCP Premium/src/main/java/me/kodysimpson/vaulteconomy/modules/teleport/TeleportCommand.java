package me.kodysimpson.vaulteconomy.modules.teleport;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {

    private final TeleportModule teleportModule;

    public TeleportCommand(TeleportModule teleportModule) {
        this.teleportModule = teleportModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Команда доступна только игрокам.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Использование: /tp <игрок>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Игрок не найден.");
            return true;
        }

        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "Нельзя телепортироваться к себе.");
            return true;
        }

        teleportModule.instantTeleport(player, target);
        return true;
    }
}