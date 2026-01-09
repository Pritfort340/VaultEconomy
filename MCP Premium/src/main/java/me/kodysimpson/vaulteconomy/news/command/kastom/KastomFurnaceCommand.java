package me.kodysimpson.vaulteconomy.news.command.kastom;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KastomFurnaceCommand implements CommandExecutor {

    private final KastomFurnace kastomFurnace;

    public KastomFurnaceCommand(KastomFurnace kastomFurnace) {
        this.kastomFurnace = kastomFurnace;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cТолько для игроков.");
            return true;
        }

        if (!player.hasPermission("vaulteconomy.kastom")) {
            player.sendMessage("§cНет прав.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("§eИспользование: §f/kostoms <1|2|3>");
            return true;
        }

        int level;
        try {
            level = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            player.sendMessage("§cУровень должен быть 1, 2 или 3.");
            return true;
        }

        if (level < 1 || level > 3) {
            player.sendMessage("§cДоступны уровни: 1, 2, 3.");
            return true;
        }

        player.getInventory().addItem(kastomFurnace.createFurnace(level));
        player.sendMessage("§aВы получили печку уровня §e" + level);

        return true;
    }
}