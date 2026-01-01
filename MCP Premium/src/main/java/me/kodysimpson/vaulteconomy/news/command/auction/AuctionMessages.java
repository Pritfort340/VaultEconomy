package me.kodysimpson.vaulteconomy.news.command.auction;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AuctionMessages {
    private static FileConfiguration config;
    private static File configFile;
    private static JavaPlugin plugin;

    public static void init(JavaPlugin p) {
        plugin = p;
        configFile = new File(plugin.getDataFolder(), "auction-messages.yml");
        if (!configFile.exists()) {
            plugin.saveResource("auction-messages.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static String get(String path) {
        return config.getString("messages." + path, "§cСообщение не найдено: " + path);
    }

    public static String get(String path, String... replacements) {
        String message = get(path);
        for (int i = 0; i < replacements.length; i += 2) {
            message = message.replace("{" + replacements[i] + "}", replacements[i + 1]);
        }
        return message;
    }

    public static void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить auction-messages.yml!");
        }
    }
}