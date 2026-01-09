package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.modules.teleport.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {

    private final WarpManager warpManager;

    public HomeCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Команда только для игроков.");
            return true;
        }
        Player player = (Player) sender;

        String homeName = args.length == 0 ? "home" : args[0].toLowerCase();

        Location home = warpManager.getHome(player, homeName);
        if (home == null) {
            player.sendMessage(ChatColor.RED + "Дом '" + homeName + "' не найден.");
            return true;
        }

        player.teleport(home);
        player.sendMessage(ChatColor.GREEN + "Вы телепортированы к дому '" + homeName + "'.");
        return true;
    }
}