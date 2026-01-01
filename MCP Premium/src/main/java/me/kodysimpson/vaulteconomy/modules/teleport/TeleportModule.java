package me.kodysimpson.vaulteconomy.modules.teleport;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;

public class TeleportModule implements Listener {

    private final Map<Player, BukkitRunnable> teleportRequests = new HashMap<>();
    private final long teleportDelay = VaultEconomy.getInstance().getConfig().getLong("teleport.delay", 100L);  // Получаем задержку из конфигурации

    public void sendTpRequest(Player player, Player target) {
        target.sendMessage(ChatColor.GREEN + player.getName() + " хочет телепортироваться к вам. Напишите /tpaaccept для принятия.");
        player.sendMessage(ChatColor.GREEN + "Запрос на телепортацию отправлен игроку " + target.getName());
    }

    public void teleportPlayerWithDelay(Player player, Player target) {
        player.sendMessage(ChatColor.GREEN + "Телепортация к игроку " + target.getName() + " через 5 секунд...");

        BukkitRunnable teleportTask = new BukkitRunnable() {
            @Override
            public void run() {
                teleportPlayer(player, target);
            }
        };

        teleportRequests.put(player, teleportTask);
        teleportTask.runTaskLater(VaultEconomy.getInstance(), teleportDelay);  // Задержка между телепортациями
    }

    public void teleportPlayer(Player player, Player target) {
        player.teleport(target.getLocation());
        player.sendMessage(ChatColor.GREEN + "Вы телепортировались к " + target.getName());
        target.sendMessage(ChatColor.GREEN + "Игрок " + player.getName() + " телепортировался к вам");

        // Визуальные эффекты (портал)
        player.getWorld().spawnParticle(Particle.PORTAL, player.getLocation(), 50);
    }

    public void teleportHere(Player player, Player target) {
        target.teleport(player.getLocation());
        target.sendMessage(ChatColor.GREEN + "Вы телепортировались к " + player.getName());
        player.sendMessage(ChatColor.GREEN + "Игрок " + target.getName() + " телепортировался к вам");

        // Визуальные эффекты (портал)
        target.getWorld().spawnParticle(Particle.PORTAL, target.getLocation(), 50);
    }

    public void acceptTpaRequest(Player player) {
        if (teleportRequests.containsKey(player)) {
            teleportRequests.get(player).run();
            teleportRequests.remove(player);
            player.sendMessage(ChatColor.GREEN + "Вы приняли запрос на телепортацию.");
        } else {
            player.sendMessage(ChatColor.RED + "У вас нет активного запроса на телепортацию.");
        }
    }

    public void denyTpaRequest(Player player) {
        if (teleportRequests.containsKey(player)) {
            teleportRequests.remove(player);
            player.sendMessage(ChatColor.RED + "Запрос на телепортацию отклонен.");
        } else {
            player.sendMessage(ChatColor.RED + "У вас нет активного запроса на телепортацию.");
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            BukkitRunnable teleportTask = teleportRequests.get(player);
            if (teleportTask != null) {
                teleportTask.cancel();
                teleportRequests.remove(player);
                player.sendMessage(ChatColor.RED + "Телепортация отменена из-за получения урона.");
            }
        }
    }
}