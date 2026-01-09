package me.kodysimpson.vaulteconomy.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FeedCommand implements CommandExecutor {

    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)){
            sender.sendMessage("Команда только для игроков.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("vaulteconomy.feed")){
            player.sendMessage(color("&cУ вас нет прав."));
            return true;
        }

        player.setFoodLevel(20);
        player.setSaturation(20f);
        player.sendMessage(color("&aВы были накормлены."));
        return true;
    }
}