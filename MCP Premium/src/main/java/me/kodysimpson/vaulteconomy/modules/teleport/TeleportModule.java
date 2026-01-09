package me.kodysimpson.vaulteconomy.modules.teleport;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TeleportModule implements Listener {

    private final VaultEconomy plugin;
    private final PvpManager pvpManager;

    private final Map<UUID, TeleportRequest> requests = new HashMap<>();
    private final Map<UUID, BukkitRunnable> activeTeleports = new HashMap<>();

    private static final long TELEPORT_DELAY = 100L; // 5 секунд

    public TeleportModule(VaultEconomy plugin, PvpManager pvpManager) {
        this.plugin = plugin;
        this.pvpManager = pvpManager;
    }

    /* ===================== REQUESTS ===================== */

    public void sendTpa(Player from, Player to) {
        if (blocked(from, to)) return;

        requests.put(to.getUniqueId(),
                new TeleportRequest(from.getUniqueId(), to.getUniqueId(), TeleportRequest.Type.TPA));

        to.sendMessage("§e" + from.getName() + " §aхочет телепортироваться к вам (§e/tpaaccept§a)");
        from.sendMessage("§aЗапрос /tpa отправлен игроку " + to.getName());
    }

    public void sendTpahere(Player from, Player to) {
        if (blocked(from, to)) return;

        requests.put(to.getUniqueId(),
                new TeleportRequest(from.getUniqueId(), to.getUniqueId(), TeleportRequest.Type.TPAHERE));

        to.sendMessage("§e" + from.getName() + " §aхочет телепортировать вас к себе (§e/tpaaccept§a)");
        from.sendMessage("§aЗапрос /tpahere отправлен игроку " + to.getName());
    }

    /* ===================== ACCEPT / DENY ===================== */

    public void accept(Player target) {
        TeleportRequest req = requests.remove(target.getUniqueId());
        if (req == null) {
            target.sendMessage("§cНет активных запросов.");
            return;
        }

        Player requester = Bukkit.getPlayer(req.getRequester());
        if (requester == null || !requester.isOnline()) {
            target.sendMessage("§cИгрок оффлайн.");
            return;
        }

        if (blocked(requester, target)) return;

        Player from;
        Player to;

        if (req.getType() == TeleportRequest.Type.TPA) {
            from = requester;
            to = target;
        } else {
            from = target;
            to = requester;
        }

        startTeleport(from, to);
    }

    public void deny(Player target) {
        if (requests.remove(target.getUniqueId()) != null) {
            target.sendMessage("§cЗапрос отклонён.");
        } else {
            target.sendMessage("§cНет активных запросов.");
        }
    }

    /* ===================== INSTANT TP ===================== */

    public void instantTeleport(Player from, Player to) {
        if (blocked(from, to)) return;

        from.teleport(to.getLocation());
        from.sendMessage("§aТелепорт выполнен.");
    }

    /* ===================== TELEPORT CORE ===================== */

    private void startTeleport(Player from, Player to) {
        from.sendMessage("§eТелепорт через 5 секунд. §cНе двигайтесь!");

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                activeTeleports.remove(from.getUniqueId());

                if (!from.isOnline() || !to.isOnline()) return;

                if (pvpManager.isInPvp(from) || pvpManager.isInPvp(to)) {
                    from.sendMessage("§cТелепорт отменён — PvP!");
                    return;
                }

                from.teleport(to.getLocation());
                from.sendMessage("§aТелепорт завершён!");
                to.sendMessage("§aИгрок §e" + from.getName() + " §aтелепортировался к вам.");

                from.getWorld().spawnParticle(Particle.PORTAL, from.getLocation(), 50);
            }
        };

        activeTeleports.put(from.getUniqueId(), task);
        task.runTaskLater(plugin, TELEPORT_DELAY);
    }

    /* ===================== CANCEL ===================== */

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!activeTeleports.containsKey(player.getUniqueId())) return;

        if (event.getFrom().distance(event.getTo()) > 0.1) {
            cancelTeleport(player, "§cТелепорт отменён — вы двигались!");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            cancelTeleport(player, "§cТелепорт отменён — получен урон!");
        }
    }

    private void cancelTeleport(Player player, String msg) {
        BukkitRunnable task = activeTeleports.remove(player.getUniqueId());
        if (task != null) {
            task.cancel();
            player.sendMessage(msg);
        }
    }

    /* ===================== UTIL ===================== */

    private boolean blocked(Player a, Player b) {
        if (pvpManager.isInPvp(a) || pvpManager.isInPvp(b)) {
            a.sendMessage("§cТелепортация запрещена во время PvP!");
            return true;
        }
        return false;
    }
}