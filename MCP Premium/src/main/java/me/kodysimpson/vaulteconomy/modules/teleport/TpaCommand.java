package me.kodysimpson.vaulteconomy.modules.teleport;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TpaCommand implements CommandExecutor {

    private final TeleportModule teleportModule;

    public TpaCommand(TeleportModule teleportModule) {
        this.teleportModule = teleportModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Только игроки могут использовать эту команду.");
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Использование: /tpa <игрок>");
            return false;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ChatColor.RED + "Игрок не найден.");
            return false;
        }

        teleportModule.sendTpRequest(player, target);
        return true;
    }
}