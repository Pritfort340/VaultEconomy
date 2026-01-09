package me.kodysimpson.vaulteconomy.modules.gamemode;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements CommandExecutor {

    private final GamemodeModule gamemodeModule;

    public GamemodeCommand(GamemodeModule gamemodeModule) {
        this.gamemodeModule = gamemodeModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(ChatColor.RED + "Только игроки могут использовать эту команду.");
            return false;
        }

        if (args.length == 1) {
            String mode = args[0];
            gamemodeModule.setGamemode(player, mode);
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "Использование: /gamemode <0/1/2/3>");
            return false;
        }
    }
}
