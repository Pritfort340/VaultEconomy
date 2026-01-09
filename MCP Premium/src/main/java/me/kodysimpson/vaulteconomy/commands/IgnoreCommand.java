package me.kodysimpson.vaulteconomy.commands;

import me.kodysimpson.vaulteconomy.chat.IgnoreManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IgnoreCommand implements CommandExecutor {

    private final IgnoreManager ignoreManager;

    public IgnoreCommand(IgnoreManager ignoreManager){
        this.ignoreManager = ignoreManager;
    }

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

        if (!player.hasPermission("vaulteconomy.ignore")){
            player.sendMessage(color("&cУ вас нет прав."));
            return true;
        }

        if (args.length != 2){
            player.sendMessage(color("&eИспользование: /ignore <игрок> <on|off>"));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null || ( !target.hasPlayedBefore() && !target.isOnline() )){
            player.sendMessage(color("&cИгрок не найден."));
            return true;
        }

        String mode = args[1].toLowerCase();
        if (!mode.equals("on") && !mode.equals("off")){
            player.sendMessage(color("&eИспользование: /ignore <игрок> <on|off>"));
            return true;
        }

        boolean ignore = mode.equals("on");
        ignoreManager.setIgnore(player.getUniqueId(), target.getUniqueId(), ignore);

        if (ignore){
            player.sendMessage(color("&aТеперь вы игнорируете сообщения от &e" + target.getName()));
        }else{
            player.sendMessage(color("&aВы больше не игнорируете &e" + target.getName()));
        }

        return true;
    }
}