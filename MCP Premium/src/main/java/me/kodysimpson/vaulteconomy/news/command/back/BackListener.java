package me.kodysimpson.vaulteconomy.news.command.back;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackListener implements Listener {

    private static final Map<UUID, Location> lastLocations = new HashMap<>();

    // 📍 СОХРАНЯЕМ ПРИ ТЕЛЕПОРТАХ
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getFrom() == null) return;
        lastLocations.put(event.getPlayer().getUniqueId(), event.getFrom());
    }

    // ☠ СОХРАНЯЕМ ПРИ СМЕРТИ
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        lastLocations.put(p.getUniqueId(), p.getLocation());
    }

    public static Location getLastLocation(Player p) {
        return lastLocations.get(p.getUniqueId());
    }
}
