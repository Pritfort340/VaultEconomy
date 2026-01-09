package me.kodysimpson.vaulteconomy.pvp;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PvpManager {

    private final VaultEconomy plugin;

    // игрок → время окончания PvP
    private final Map<UUID, Long> pvpEnd = new HashMap<>();

    // игрок → BossBar
    private final Map<UUID, BossBar> bossBars = new HashMap<>();

    private int duration; // секунды

    public PvpManager(VaultEconomy plugin) {
        this.plugin = plugin;
        loadSettings();
    }

    /* =====================================================
     *                    LOAD CONFIG
     * ===================================================== */

    private void loadSettings() {
        this.duration = plugin.getPvpConfig().getInt("pvp.duration-seconds", 30);
    }

    /* =====================================================
     *                    START PVP
     * ===================================================== */

    public void startPvp(Player player) {

        // bypass
        if (player.hasPermission("vaulteconomy.pvp.bypass")) return;

        UUID uuid = player.getUniqueId();

        boolean alreadyInPvp = pvpEnd.containsKey(uuid);

        // продлеваем PvP
        long endTime = System.currentTimeMillis() + duration * 1000L;
        pvpEnd.put(uuid, endTime);

        // если уже был в PvP — НИЧЕГО больше не делаем
        if (alreadyInPvp) return;

        // ===== ВХОД В PvP (1 РАЗ) =====

        BossBar bar = Bukkit.createBossBar(
                color(plugin.getPvpConfig().getString("pvp.bossbar.text")),
                BarColor.RED,
                BarStyle.SOLID
        );
        bar.addPlayer(player);
        bossBars.put(uuid, bar);

        // 🔊 звук ВХОДА
        player.playSound(player.getLocation(),
                Sound.ENTITY_WITHER_SPAWN, 1f, 1f);

        // 💬 сообщение ВХОДА
        player.sendMessage(color(
                plugin.getPvpConfig().getString("pvp.messages.start"))
        );

        startBossBarUpdater(player);
    }

    /* =====================================================
     *                    BOSSBAR UPDATE
     * ===================================================== */

    private void startBossBarUpdater(Player player) {

        UUID uuid = player.getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {

                if (!player.isOnline()) {
                    stopPvp(player);
                    cancel();
                    return;
                }

                Long end = pvpEnd.get(uuid);
                if (end == null) {
                    cancel();
                    return;
                }

                long timeLeft = (end - System.currentTimeMillis()) / 1000L;

                if (timeLeft <= 0) {
                    stopPvp(player);
                    cancel();
                    return;
                }

                BossBar bar = bossBars.get(uuid);
                if (bar == null) return;

                double progress = Math.max(0.0,
                        timeLeft / (double) duration);

                bar.setProgress(progress);

                bar.setTitle(color(
                        plugin.getPvpConfig()
                                .getString("pvp.bossbar.text")
                                .replace("{time}", String.valueOf(timeLeft))
                ));
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    /* =====================================================
     *                    CHECK PVP
     * ===================================================== */

    public boolean isInPvp(Player player) {

        Long end = pvpEnd.get(player.getUniqueId());
        if (end == null) return false;

        if (System.currentTimeMillis() > end) {
            stopPvp(player);
            return false;
        }
        return true;
    }

    /* =====================================================
     *                    STOP PVP
     * ===================================================== */

    public void stopPvp(Player player) {

        UUID uuid = player.getUniqueId();

        if (!pvpEnd.containsKey(uuid)) return;

        pvpEnd.remove(uuid);

        BossBar bar = bossBars.remove(uuid);
        if (bar != null) {
            bar.removeAll();
        }

        // 🔊 звук ВЫХОДА
        player.playSound(player.getLocation(),
                Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        // 💬 сообщение ВЫХОДА
        player.sendMessage(color(
                plugin.getPvpConfig().getString("pvp.messages.end"))
        );
    }

    /* =====================================================
     *                    RELOAD
     * ===================================================== */

    public void reload() {

        loadSettings();

        for (UUID uuid : bossBars.keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;

            BossBar bar = bossBars.get(uuid);
            if (bar != null) {
                bar.setTitle(color(
                        plugin.getPvpConfig().getString("pvp.bossbar.text"))
                );
            }
        }

        plugin.getLogger().info("PvpManager перезагружен");
    }

    /* =====================================================
     *                    UTIL
     * ===================================================== */

    private String color(String s) {
        return s == null ? "" : s.replace("&", "§");
    }
}