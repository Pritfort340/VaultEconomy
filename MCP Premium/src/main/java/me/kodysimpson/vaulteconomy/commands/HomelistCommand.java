package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.modules.teleport.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class HomelistCommand implements CommandExecutor {

    private final WarpManager warpManager;

    public HomelistCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Команда только для игроков.");
            return true;
        }
        Player player = (Player) sender;

        List<String> homes = warpManager.getHomes(player);
        if (homes.isEmpty()) {
            player.sendMessage(ChatColor.YELLOW + "У вас нет домов.");
            return true;
        }

        player.sendMessage(ChatColor.GREEN + "Ваши дома: " +
                ChatColor.YELLOW + String.join(", ", homes));
        return true;
    }
}