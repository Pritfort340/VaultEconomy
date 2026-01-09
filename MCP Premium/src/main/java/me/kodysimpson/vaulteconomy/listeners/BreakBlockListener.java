package me.kodysimpson.vaulteconomy.listeners;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.economy.CustomEconomy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
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
        Player player = e.getPlayer();

        // Только для игроков, не для консоли/мобов
        if (!player.hasPermission("vaulteconomy.blocks.reward")) {
            return; // нет прав — нет награды
        }

        // Награда из конфига
        double reward = plugin.getConfig().getDouble("block-break-reward", 0.5);

        CustomEconomy economy = plugin.getEconomy();
        if (economy != null) {
            economy.depositPlayer(player, reward);
            player.sendMessage(ChatColor.GREEN + "§a+⛁" + reward + " за ломание блока!");
        }
    }
}