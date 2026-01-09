package me.kodysimpson.vaulteconomy.modules.teleport;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class VisualEffects {

    // Эффекты при телепортации
    public static void showTeleportEffect(Player player) {
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 50);
    }
}