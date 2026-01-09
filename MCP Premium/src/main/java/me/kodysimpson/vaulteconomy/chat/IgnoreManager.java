package me.kodysimpson.vaulteconomy.chat;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class IgnoreManager {

    private final VaultEconomy plugin;
    private final File file;
    private YamlConfiguration data;

    // owner -> ignored players
    private final Map<UUID, Set<UUID>> ignores = new HashMap<>();

    public IgnoreManager(VaultEconomy plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "ignores.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось создать ignores.yml");
            }
        }

        this.data = YamlConfiguration.loadConfiguration(file);
        load();
    }

    /* ===================== CORE ===================== */

    public void setIgnore(UUID owner, UUID target, boolean ignore) {
        ignores.putIfAbsent(owner, new HashSet<>());

        if (ignore) {
            ignores.get(owner).add(target);
        } else {
            ignores.get(owner).remove(target);
        }

        save();
    }

    public boolean isIgnoring(UUID owner, UUID target) {
        return ignores.getOrDefault(owner, Collections.emptySet()).contains(target);
    }

    /* ===================== LOAD / SAVE ===================== */

    private void load() {
        ignores.clear();

        if (!data.contains("ignores")) return;

        for (String ownerKey : data.getConfigurationSection("ignores").getKeys(false)) {
            UUID owner = UUID.fromString(ownerKey);
            List<String> list = data.getStringList("ignores." + ownerKey);

            Set<UUID> targets = new HashSet<>();
            for (String s : list) {
                targets.add(UUID.fromString(s));
            }

            ignores.put(owner, targets);
        }
    }

    private void save() {
        data.set("ignores", null);

        for (Map.Entry<UUID, Set<UUID>> entry : ignores.entrySet()) {
            List<String> list = new ArrayList<>();
            for (UUID uuid : entry.getValue()) {
                list.add(uuid.toString());
            }
            data.set("ignores." + entry.getKey(), list);
        }

        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить ignores.yml");
        }
    }

    /* ===================== RELOAD ===================== */

    public void reload() {
        this.data = YamlConfiguration.loadConfiguration(file);
        load();
        plugin.getLogger().info("IgnoreManager перезагружен");
    }
}