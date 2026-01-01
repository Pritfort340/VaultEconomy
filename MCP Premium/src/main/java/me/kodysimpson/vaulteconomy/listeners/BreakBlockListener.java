package me.kodysimpson.vaulteconomy.listeners;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BreakBlockListener implements Listener {

    private final VaultEconomy plugin;

    public BreakBlockListener(VaultEconomy plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        double reward = plugin.getConfig().getDouble("block-break-reward", 5.0);
        plugin.getEconomy().depositPlayer(e.getPlayer(), reward);
    }
}