package me.kodysimpson.vaulteconomy.modules.fly;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

public class FlyCommand implements CommandExecutor {

    private final FileConfiguration config;
    private final FlyModule flyModule;

    public FlyCommand(FileConfiguration config, FlyModule flyModule) {  // Добавил flyModule в конструктор
        this.config = config;
        this.flyModule = flyModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Эту команду может использовать только игрок.");
            return true;  // true вместо false
        }

        Player player = (Player) sender;

        if (!player.hasPermission("vaulteconomy.fly")) {
            player.sendMessage(ChatColor.RED + config.getString("fly.messages.no-permission", "У вас нет прав!"));
            return true;
        }

        flyModule.toggleFly(player);
        return true;
    }
}