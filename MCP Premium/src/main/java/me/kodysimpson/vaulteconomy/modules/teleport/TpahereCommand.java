package me.kodysimpson.vaulteconomy.modules.teleport;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpahereCommand implements CommandExecutor {

    private final TeleportModule teleportModule;

    public TpahereCommand(TeleportModule teleportModule) {
        this.teleportModule = teleportModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // ❌ только игрок
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Команда доступна только игрокам.");
            return true;
        }

        // ❌ аргументы
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Использование: /tpahere <игрок>");
            return true;
        }

        // 🎯 цель
        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Игрок не найден.");
            return true;
        }

        // ❌ сам себе
        if (target.equals(player)) {
            player.sendMessage(ChatColor.RED + "Нельзя телепортировать себя к себе.");
            return true;
        }

        // ✅ отправляем запрос
        teleportModule.sendTpahere(player, target);
        return true;
    }
}