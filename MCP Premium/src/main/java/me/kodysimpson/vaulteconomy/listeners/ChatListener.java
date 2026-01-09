package me.kodysimpson.vaulteconomy.listeners;

import me.kodysimpson.vaulteconomy.chat.IgnoreManager;
import me.kodysimpson.vaulteconomy.chat.MarriageManager;
import me.kodysimpson.vaulteconomy.chat.PrefixManager;
import me.kodysimpson.vaulteconomy.chat.MarriageManager.MarriageRole;
import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class ChatListener implements Listener {

    private final PrefixManager prefixManager;
    private final IgnoreManager ignoreManager;
    private final MarriageManager marriageManager;
    private final ClanManager clanManager; // <<< ДОБАВЛЕНО

    public ChatListener(PrefixManager prefixManager,
                        IgnoreManager ignoreManager,
                        MarriageManager marriageManager,
                        ClanManager clanManager) { // <<< ДОБАВЛЕНО
        this.prefixManager = prefixManager;
        this.ignoreManager = ignoreManager;
        this.marriageManager = marriageManager;
        this.clanManager = clanManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        // ===== СООБЩЕНИЕ =====
        String message = event.getMessage();

        // ===== СТАТУС БРАКА =====
        String marriedTag = "";

        try {
            if (marriageManager != null && marriageManager.isChatVisible(uuid)) {

                if (marriageManager.isMarried(uuid)) {
                    MarriageRole role = marriageManager.getRole(uuid);

                    if (role == MarriageRole.HUSBAND) {
                        marriedTag = "§d[Женатый] ";
                    } else if (role == MarriageRole.WIFE) {
                        marriedTag = "§d[Жената] ";
                    } else {
                        marriedTag = "§d[В браке] ";
                    }
                } else {
                    marriedTag = "§7[Один] ";
                }
            }
        } catch (Exception ignored) {
        }

        // ===== КЛАН =====
        String clanTag = "";

        try {
            if (clanManager != null && clanManager.hasClan(uuid)) {
                Clan clan = clanManager.getClan(uuid);
                if (clan != null && clan.getChatPrefix() != null) {
                    clanTag = ChatColor.translateAlternateColorCodes('&',
                            clan.getChatPrefix()) + " ";
                }
            }
        } catch (Exception ignored) {
        }

        // ===== ПРЕФИКС =====
        String rawPrefix = "&7[Игрок]";

        try {
            if (prefixManager != null) {
                String savedPrefix = prefixManager.getPrefix(player);
                if (savedPrefix != null && !savedPrefix.trim().isEmpty()) {
                    rawPrefix = savedPrefix;
                }
            }
        } catch (Exception ignored) {
        }

        String prefix = ChatColor.translateAlternateColorCodes('&', rawPrefix);

        // ===== ФОРМАТ ЧАТА =====
        String format =
                marriedTag +
                        clanTag +
                        prefix + " " +
                        ChatColor.WHITE + player.getName() +
                        ChatColor.GRAY + " » " +
                        ChatColor.WHITE + message;

        event.setFormat(format);

        // ===== IGNORE =====
        try {
            event.getRecipients().removeIf(recipient ->
                    ignoreManager != null &&
                            ignoreManager.isIgnoring(recipient.getUniqueId(), uuid)
            );
        } catch (Exception ignored) {
        }
    }
}