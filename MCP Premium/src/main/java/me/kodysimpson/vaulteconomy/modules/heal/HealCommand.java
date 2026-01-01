package me.kodysimpson.vaulteconomy.modules.heal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

public class HealCommand implements CommandExecutor {

    private final FileConfiguration config;

    public HealCommand(FileConfiguration config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return false;
        }

        if (!player.hasPermission("vaulteconomy.heal")) {
            player.sendMessage("У вас нет прав для использования этой команды.");
            return false;
        }

        // Лечение игрока
        player.setHealth(player.getMaxHealth());
        player.sendMessage(config.getString("messages.heal-success", "Вы успешно вылечены."));

        return true;
    }
}