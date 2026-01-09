package me.kodysimpson.vaulteconomy.modules.fly;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FlyModule implements Listener {

    private final VaultEconomy plugin;
    private final PvpManager pvpManager;

    private final Map<UUID, Long> flyCooldowns = new HashMap<>();
    private static final long FLY_COOLDOWN_TICKS = 20L * 30; // 30 секунд

    public FlyModule(VaultEconomy plugin) {
        this.plugin = plugin;
        this.pvpManager = plugin.getPvpManager();
    }

    /* =====================================================
                         PUBLIC API
       ===================================================== */

    public void toggleFly(Player player) {

        // ❌ В PvP нельзя
        if (pvpManager.isInPvp(player)) {
            player.sendMessage("§c❌ Нельзя включать полёт во время PvP!");
            player.playSound(player.getLocation(),
                    Sound.ENTITY_VILLAGER_NO, 1f, 0.8f);
            return;
        }

        // ❌ Кулдаун
        if (flyCooldowns.containsKey(player.getUniqueId())) {
            player.sendMessage("§c❌ Полёт временно заблокирован после боя!");
            player.playSound(player.getLocation(),
                    Sound.ENTITY_VILLAGER_NO, 1f, 0.8f);
            return;
        }

        // 🔄 Toggle
        if (player.getAllowFlight()) {
            disableFlyOnly(player);
            player.sendMessage("§c✈ Полёт выключен");
        } else {
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("§a✈ Полёт включен");
            player.playSound(player.getLocation(),
                    Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.2f);
        }
    }

    /* =====================================================
                         EVENTS
       ===================================================== */

    // ❌ Любой урон → выключаем полёт
    @EventHandler
    public void onAnyDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!(event instanceof EntityDamageByEntityEvent)) {
            if (player.getAllowFlight()) {
                disableFlyOnly(player);
            }
        }
    }

    // 🩸 PvP урон
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {

        if (!(event.getEntity() instanceof Player victim)) return;

        Player attacker = getAttackingPlayer(event.getDamager());
        if (attacker == null) return;

        pvpManager.startPvp(victim);
        pvpManager.startPvp(attacker);

        if (victim.getAllowFlight()) {
            disableFlyWithCooldown(victim);
        }
        if (attacker.getAllowFlight()) {
            disableFlyWithCooldown(attacker);
        }
    }

    // ☠ Смерть
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player p = event.getEntity();
        flyCooldowns.remove(p.getUniqueId());
        disableFlyOnly(p);
    }

    /* =====================================================
                         INTERNAL
       ===================================================== */

    private Player getAttackingPlayer(Entity damager) {
        if (damager instanceof Player p) return p;

        if (damager instanceof Projectile proj) {
            if (proj.getShooter() instanceof Player p) return p;
        }
        return null;
    }

    private void disableFlyOnly(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
        player.playSound(player.getLocation(),
                Sound.BLOCK_NOTE_BLOCK_BASS, 0.5f, 0.5f);
    }

    private void disableFlyWithCooldown(Player player) {

        disableFlyOnly(player);

        player.sendTitle(
                ChatColor.RED + "§l✈ ПОЛЁТ ОТКЛЮЧЕН",
                "§cПосле боя: 30 секунд",
                5, 40, 5
        );

        UUID id = player.getUniqueId();
        flyCooldowns.put(id, System.currentTimeMillis());

        new BukkitRunnable() {
            @Override
            public void run() {
                flyCooldowns.remove(id);
            }
        }.runTaskLater(plugin, FLY_COOLDOWN_TICKS);
    }
}