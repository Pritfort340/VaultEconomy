package me.kodysimpson.vaulteconomy.modules.god;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GodCommand implements CommandExecutor {

    private final GodModule godModule;

    public GodCommand(GodModule godModule) {
        this.godModule = godModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return false;
        }

        Player player = (Player) sender;
        if (args.length == 0) {
            godModule.toggleGodMode(player);  // Переключаем режим Бога
            return true;
        }

        return false;  // Можно добавить дополнительные проверки для администраторов или другие аргументы
    }
}