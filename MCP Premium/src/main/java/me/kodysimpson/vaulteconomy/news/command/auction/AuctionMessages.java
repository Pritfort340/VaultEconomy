package me.kodysimpson.vaulteconomy.news.command.auction;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class AuctionMessages {

    private static File file;
    private static FileConfiguration config;

    /* ===================== INIT ===================== */

    public static void init(VaultEconomy plugin) {
        file = new File(plugin.getDataFolder(), "auction-messages.yml");

        if (!file.exists()) {
            plugin.saveResource("auction-messages.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    /* ===================== RELOAD ===================== */

    public static void reload(VaultEconomy plugin) {
        if (file == null) {
            init(plugin);
            return;
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    /* ===================== GET SIMPLE ===================== */

    public static String get(String key) {
        if (config == null) {
            return ChatColor.RED + "AuctionMessages не инициализирован!";
        }

        if (!config.contains(key)) {
            return ChatColor.RED + "Сообщение не найдено: " + key;
        }

        return color(config.getString(key));
    }

    /* ===================== GET WITH PLACEHOLDERS ===================== */

    public static String get(String key, String... placeholders) {
        String message = get(key);

        if (placeholders.length % 2 != 0) {
            return message; // защита от ошибок
        }

        for (int i = 0; i < placeholders.length; i += 2) {
            message = message.replace(
                    "%" + placeholders[i] + "%",
                    placeholders[i + 1]
            );
        }

        return message;
    }

    /* ===================== UTILS ===================== */

    private static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}