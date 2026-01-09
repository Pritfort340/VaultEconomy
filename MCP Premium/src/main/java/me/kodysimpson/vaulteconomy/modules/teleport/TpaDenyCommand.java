package me.kodysimpson.vaulteconomy.modules.teleport;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TpaDenyCommand implements CommandExecutor {

    private final TeleportModule teleportModule;

    public TpaDenyCommand(TeleportModule teleportModule) {
        this.teleportModule = teleportModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            teleportModule.deny(player);
        }
        return true;
    }
}