package me.kodysimpson.vaulteconomy.chat;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PrefixManager {

    private final VaultEconomy plugin;
    private final File file;
    private final YamlConfiguration data;

    // стандартный префикс
    private final String defaultPrefix = "[Игрок]";

    public PrefixManager(VaultEconomy plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "prefixes.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось создать prefixes.yml");
            }
        }

        this.data = YamlConfiguration.loadConfiguration(file);
    }

    public String getPrefix(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        String key = "prefixes." + uuid;
        return data.getString(key, defaultPrefix);
    }

    public void setPrefix(OfflinePlayer player, String prefix) {
        UUID uuid = player.getUniqueId();
        data.set("prefixes." + uuid, prefix);
        save();
    }

    public void resetPrefix(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        data.set("prefixes." + uuid, null);
        save();
    }

    private void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить prefixes.yml");
        }
    }
}