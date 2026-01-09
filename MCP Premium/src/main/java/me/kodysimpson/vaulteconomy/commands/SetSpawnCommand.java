package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.modules.teleport.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCommand implements CommandExecutor {

    private final WarpManager warpManager;

    public SetSpawnCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    private String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // 🔒 Только игрок
        if (!(sender instanceof Player)) {
            sender.sendMessage(color("&cЭту команду может использовать только игрок."));
            return true;
        }

        Player player = (Player) sender;

        // 🔒 Права (СОВПАДАЮТ с plugin.yml)
        if (!player.hasPermission("vaulteconomy.spawn.set")) {
            player.sendMessage(color("&c❌ У вас нет прав на использование этой команды."));
            return true;
        }

        // 📍 Установка спавна
        warpManager.setSpawn(player.getLocation());

        // ✅ Успех
        player.sendMessage(color("&a✔ Спавн сервера успешно установлен!"));

        return true;
    }
}