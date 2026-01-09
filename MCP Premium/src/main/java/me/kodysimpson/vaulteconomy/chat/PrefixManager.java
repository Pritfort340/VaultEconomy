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
    private YamlConfiguration data;

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

        String stored = data.getString("prefixes." + uuid);
        if (stored != null && !stored.isEmpty()) {
            return stored;
        }

        return plugin.getConfig().getString(
                "chat.default-prefix",
                "&7[Игрок]"
        );
    }

    public void setPrefix(OfflinePlayer player, String prefix) {
        data.set("prefixes." + player.getUniqueId(), prefix);
        save();
    }

    public void resetPrefix(OfflinePlayer player) {
        data.set("prefixes." + player.getUniqueId(), null);
        save();
    }

    private void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить prefixes.yml");
        }
    }

    /* ===================== RELOAD ===================== */

    public void reload() {
        // перезагружаем config.yml (для default-prefix)
        plugin.reloadConfig();

        // перечитываем prefixes.yml
        this.data = YamlConfiguration.loadConfiguration(file);

        plugin.getLogger().info("PrefixManager перезагружен");
    }
}