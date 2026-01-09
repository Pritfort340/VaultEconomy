package me.kodysimpson.vaulteconomy.chat.clancommand;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ClanChat implements Listener {

    private final ClanManager clanManager;
    private final Set<UUID> enabledPlayers = new HashSet<>();

    public ClanChat(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public boolean toggle(UUID uuid) {
        if (enabledPlayers.contains(uuid)) {
            enabledPlayers.remove(uuid);
            return false;
        }
        enabledPlayers.add(uuid);
        return true;
    }

    public boolean isEnabled(UUID uuid) {
        return enabledPlayers.contains(uuid);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (!isEnabled(uuid)) return;
        if (!clanManager.hasClan(uuid)) return;

        event.setCancelled(true);

        Clan clan = clanManager.getClan(uuid);
        sendToClan(clan, player, event.getMessage());
    }

    public void sendToClan(Clan clan, Player sender, String message) {
        String format = "§b[КЛАН] §f" + sender.getName() + " §7» §f" + message;

        for (UUID memberId : clan.getMembers()) {
            Player member = Bukkit.getPlayer(memberId);
            if (member != null && member.isOnline()) {
                member.sendMessage(format);
            }
        }
    }
}