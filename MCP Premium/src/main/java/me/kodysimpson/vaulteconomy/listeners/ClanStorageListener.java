package me.kodysimpson.vaulteconomy.listeners;

import me.kodysimpson.vaulteconomy.chat.clancommand.ClanStorage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ClanStorageListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof ClanStorage storage)) return;
        storage.save();
    }
}