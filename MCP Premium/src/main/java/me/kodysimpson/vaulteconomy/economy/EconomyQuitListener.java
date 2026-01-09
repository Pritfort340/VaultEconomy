package me.kodysimpson.vaulteconomy.economy;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EconomyQuitListener implements Listener {

    private final CustomEconomy economy;

    public EconomyQuitListener(CustomEconomy economy) {
        this.economy = economy;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        economy.saveBalances();
    }
}