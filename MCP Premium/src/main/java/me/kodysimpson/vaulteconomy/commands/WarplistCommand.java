package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.modules.teleport.WarpManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class WarplistCommand implements CommandExecutor {

    private final WarpManager warpManager;

    public WarplistCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        List<String> warps = warpManager.getWarps();
        if (warps.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Нет доступных варпов.");
            return true;
        }

        sender.sendMessage(ChatColor.GREEN + "Список варпов: " +
                ChatColor.YELLOW + String.join(", ", warps));
        return true;
    }
}
