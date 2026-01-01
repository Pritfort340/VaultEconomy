package me.kodysimpson.vaulteconomy.modules.teleport;

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
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return false;
        }

        if (args.length != 1) {
            player.sendMessage("Использование: /tpahere <игрок>");
            return false;
        }

        Player target = player.getServer().getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("Игрок не найден.");
            return false;
        }

        teleportModule.teleportHere(player, target);
        return true;
    }
}