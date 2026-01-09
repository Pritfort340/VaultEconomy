package me.kodysimpson.vaulteconomy.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearChatCommand implements CommandExecutor {

    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("vaulteconomy.clearchat")){
            sender.sendMessage(color("&cУ вас нет прав."));
            return true;
        }

        // 100 пустых строк для всех игроков
        for (int i = 0; i < 100; i++){
            for (Player p : Bukkit.getOnlinePlayers()){
                p.sendMessage(" ");
            }
        }

        Bukkit.broadcastMessage(color("&7Чат был очищен игроком &e" + sender.getName()));
        return true;
    }
}