package me.kodysimpson.vaulteconomy.modules.heal;

import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

public class HealCommand implements CommandExecutor {

    private final FileConfiguration config;
    private final PvpManager pvpManager;

    public HealCommand(FileConfiguration config, PvpManager pvpManager) {
        this.config = config;
        this.pvpManager = pvpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Только игроки могут использовать эту команду.");
            return true;
        }

        // ❌ БЛОК В PvP
        if (pvpManager.isInPvp(player)) {
            player.sendMessage("§c⛔ Нельзя лечиться во время PvP!");
            return true;
        }

        if (!player.hasPermission("vaulteconomy.heal")) {
            player.sendMessage("§cУ вас нет прав для использования этой команды.");
            return true;
        }

        player.setHealth(player.getMaxHealth());
        player.sendMessage(config.getString(
                "messages.heal-success",
                "§aВы успешно вылечены."
        ));

        return true;
    }
}