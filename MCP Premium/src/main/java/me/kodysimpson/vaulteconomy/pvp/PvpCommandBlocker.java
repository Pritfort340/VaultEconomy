package me.kodysimpson.vaulteconomy.pvp;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class PvpCommandBlocker implements Listener {

    private final VaultEconomy plugin;
    private final PvpManager pvpManager;

    public PvpCommandBlocker(VaultEconomy plugin, PvpManager pvpManager) {
        this.plugin = plugin;
        this.pvpManager = pvpManager;
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {

        // 🔓 bypass
        if (e.getPlayer().hasPermission("vaulteconomy.pvp.command.bypass")) return;

        if (!pvpManager.isInPvp(e.getPlayer())) return;

        String cmd = e.getMessage().toLowerCase();
        List<String> blocked = plugin.getPvpConfig().getStringList("pvp.blocked-commands");

        for (String b : blocked) {
            if (cmd.startsWith("/" + b.toLowerCase())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(
                        plugin.getPvpConfig()
                                .getString("pvp.messages.command-blocked")
                                .replace("&", "§")
                );
                return;
            }
        }
    }
}