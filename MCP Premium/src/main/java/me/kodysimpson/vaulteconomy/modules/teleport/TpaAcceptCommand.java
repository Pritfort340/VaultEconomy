package me.kodysimpson.vaulteconomy.modules.teleport;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

public class TpaAcceptCommand implements CommandExecutor {

    private final TeleportModule teleportModule;

    public TpaAcceptCommand(TeleportModule teleportModule) {
        this.teleportModule = teleportModule;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            teleportModule.accept(player);
        }
        return true;
    }
}