package me.kodysimpson.vaulteconomy.pvp;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {

    private final PvpManager pvpManager;

    public PvpListener(PvpManager pvpManager) {
        this.pvpManager = pvpManager;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent e) {

        if (!(e.getEntity() instanceof Player victim)) return;

        Player attacker = null;

        if (e.getDamager() instanceof Player p) {
            attacker = p;
        } else if (e.getDamager() instanceof Projectile projectile &&
                projectile.getShooter() instanceof Player p) {
            attacker = p;
        }

        if (attacker == null) return;
        if (attacker.equals(victim)) return;

        if (!pvpManager.isInPvp(victim)) {
            pvpManager.startPvp(victim);
        }
        if (!pvpManager.isInPvp(attacker)) {
            pvpManager.startPvp(attacker);
        }
    }
}