package me.kodysimpson.vaulteconomy.modules.fly;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

    private final VaultEconomy plugin;
    private final FlyModule flyModule;

    public FlyCommand(VaultEconomy plugin, FlyModule flyModule) {
        this.plugin = plugin;
        this.flyModule = flyModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cТолько игроки!");
            return true;
        }

        if (!player.hasPermission("vaulteconomy.fly")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    plugin.getConfig().getString("fly.no-permission", "&cНет прав!")));
            return true;
        }

        flyModule.toggleFly(player);
        return true;
    }
}