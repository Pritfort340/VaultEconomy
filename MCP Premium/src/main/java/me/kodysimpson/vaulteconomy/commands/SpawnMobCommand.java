package me.kodysimpson.vaulteconomy.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class SpawnMobCommand implements CommandExecutor {

    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!sender.hasPermission("vaulteconomy.spawnmob")){
            sender.sendMessage(color("&cУ вас нет прав."));
            return true;
        }

        if (args.length < 1){
            sender.sendMessage(color("&eИспользование: /sp <mob> [x] [y] [z] [NBT/COMPONENT]"));
            return true;
        }

        EntityType type;
        try{
            type = EntityType.valueOf(args[0].toUpperCase());
        }catch (IllegalArgumentException ex){
            sender.sendMessage(color("&cНеизвестный моб: &e" + args[0]));
            return true;
        }

        Location loc;
        if (args.length >= 4){
            // координаты заданы вручную
            if (!(sender instanceof Player)){
                sender.sendMessage(color("&cКоординаты можно указывать только из игры."));
                return true;
            }
            Player p = (Player) sender;
            World w = p.getWorld();
            try{
                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);
                loc = new Location(w, x, y, z);
            }catch (NumberFormatException ex){
                sender.sendMessage(color("&cКоординаты должны быть числами."));
                return true;
            }
        }else{
            // без координат — под игроком
            if (!(sender instanceof Player)){
                sender.sendMessage(color("&cНужно указать координаты, если команда из консоли."));
                return true;
            }
            loc = ((Player) sender).getLocation();
        }

        // TODO: разбор NBT/COMPONENT из args[4+] по желанию

        loc.getWorld().spawnEntity(loc, type);
        sender.sendMessage(color("&aМоб &e" + type.name() + " &aзаспавнен."));

        return true;
    }
}