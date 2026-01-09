package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.modules.teleport.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelHomeCommand implements CommandExecutor {

    private final WarpManager warpManager;

    public DelHomeCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Команда только для игроков.");
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "Использование: /delhome <имя>");
            return true;
        }

        String homeName = args[0].toLowerCase();

        Location home = warpManager.getHome(player, homeName);
        if (home == null) {
            player.sendMessage(ChatColor.RED + "Дом '" + homeName + "' не найден.");
            return true;
        }

        warpManager.delHome(player, homeName);
        player.sendMessage(ChatColor.GREEN + "Дом '" + homeName + "' удалён.");
        return true;
    }
}
