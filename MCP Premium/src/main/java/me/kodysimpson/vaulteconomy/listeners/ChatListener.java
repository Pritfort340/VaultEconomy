package me.kodysimpson.vaulteconomy.listeners;

import me.kodysimpson.vaulteconomy.chat.IgnoreManager;
import me.kodysimpson.vaulteconomy.chat.PrefixManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final PrefixManager prefixManager;
    private final IgnoreManager ignoreManager;

    public ChatListener(PrefixManager prefixManager, IgnoreManager ignoreManager) {
        this.prefixManager = prefixManager;
        this.ignoreManager = ignoreManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        // &-цвета в сообщении
        String msg = ChatColor.translateAlternateColorCodes('&', event.getMessage());

        // Префикс
        String rawPrefix = prefixManager.getPrefix(player);
        String coloredPrefix = ChatColor.translateAlternateColorCodes('&', rawPrefix);

        // Формат [Префикс] Ник: сообщение
        String format = coloredPrefix + ChatColor.RESET + " " +
                player.getDisplayName() + ChatColor.RESET + ": " + msg;

        event.setFormat(format);
        event.setMessage(msg);

        // Фильтр по /ignore: удаляем из получателей тех, кто игнорирует отправителя
        event.getRecipients().removeIf(recipient ->
                ignoreManager.isIgnoring(recipient.getUniqueId(), player.getUniqueId())
        );
    }
}