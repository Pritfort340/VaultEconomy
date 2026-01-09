package me.kodysimpson.vaulteconomy.modules.god;

import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;

public class GodModule {

    private final PvpManager pvpManager;

    public GodModule(PvpManager pvpManager) {
        this.pvpManager = pvpManager;
    }

    public void enableGodMode(Player player) {
        player.setInvulnerable(true);
    }

    public void disableGodMode(Player player) {
        player.setInvulnerable(false);
    }

    public void toggleGodMode(Player player) {

        // ❌ ЗАПРЕТ GOD ВО ВРЕМЯ PvP
        if (pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя включить God-режим во время PvP!");
            return;
        }

        if (player.isInvulnerable()) {
            disableGodMode(player);
            player.sendMessage("§cРежим Бога выключен.");
        } else {
            enableGodMode(player);
            player.sendMessage("§aРежим Бога включен.");
        }
    }
}