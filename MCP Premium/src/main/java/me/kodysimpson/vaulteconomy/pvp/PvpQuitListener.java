package me.kodysimpson.vaulteconomy.pvp;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

public class PvpQuitListener implements Listener {

    private final PvpManager pvpManager;

    public PvpQuitListener(PvpManager pvpManager) {
        this.pvpManager = pvpManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (!pvpManager.isInPvp(player)) return;

        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }

        for (ItemStack item : player.getInventory().getArmorContents()) {
            if (item != null) {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        Date unbanDate = new Date(System.currentTimeMillis() + 2L * 60 * 60 * 1000);

        Bukkit.getBanList(BanList.Type.NAME).addBan(
                player.getName(),
                "§c3.2 Лив с PvP",
                unbanDate,
                null
        );

        pvpManager.stopPvp(player);
    }
}