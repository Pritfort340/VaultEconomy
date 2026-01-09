package me.kodysimpson.vaulteconomy.chat.clancommand;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ClanConfig {

    private final JavaPlugin plugin;
    private File file;
    private FileConfiguration config;

    public ClanConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        load();
    }

    // =====================================================
    // ================= LOAD / RELOAD =====================
    // =====================================================

    public void load() {
        file = new File(plugin.getDataFolder(), "clan.yml");

        if (!file.exists()) {
            plugin.saveResource("clan.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // ================= MESSAGES ==========================
    // =====================================================

    public String getMessage(String path) {
        return color(config.getString("messages." + path, "§cMessage not found: " + path));
    }

    public List<String> getMessageList(String path) {
        return color(config.getStringList("messages." + path));
    }

    // =====================================================
    // ================= NUMBERS ===========================
    // =====================================================

    public int getInt(String path) {
        return config.getInt(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    // =====================================================
    // ================= BOOLEANS ==========================
    // =====================================================

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    // =====================================================
    // ================= STRINGS ===========================
    // =====================================================

    public String getString(String path) {
        return color(config.getString(path));
    }

    // =====================================================
    // ================= PAGINATION ========================
    // =====================================================

    public int getPageSize() {
        return config.getInt("pagination.page-size", 10);
    }

    public String getPageHeader() {
        return getString("pagination.header");
    }

    public String getPageFooter() {
        return getString("pagination.footer");
    }

    // =====================================================
    // ================= SYMBOLS ===========================
    // =====================================================

    public String getSymbol(String key) {
        return color(config.getString("symbols." + key, ""));
    }

    // =====================================================
    // ================= UTIL ==============================
    // =====================================================

    private String color(String text) {
        if (text == null) return "";
        return text.replace("&", "§");
    }

    private List<String> color(List<String> list) {
        list.replaceAll(this::color);
        return list;
    }

    public FileConfiguration getRaw() {
        return config;
    }
}