package me.kodysimpson.vaulteconomy.chat;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MarriageManager {

    public enum MarriageRole {
        HUSBAND,
        WIFE
    }

    private final VaultEconomy plugin;
    private final File file;
    private YamlConfiguration data;

    // marriage.yml:
    // married.<uuid> = partnerUuid
    // role.<uuid> = HUSBAND / WIFE
    // chat-visible.<uuid> = true/false
    // daily-tp.<uuid> = usedToday (0-3)

    public MarriageManager(VaultEconomy plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "marriage.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось создать marriage.yml");
            }
        }

        this.data = YamlConfiguration.loadConfiguration(file);
    }

    /* ===================== БРАК ===================== */

    public boolean isMarried(UUID uuid) {
        return data.contains("married." + uuid);
    }

    public UUID getPartner(UUID uuid) {
        String s = data.getString("married." + uuid);
        if (s == null) return null;
        try {
            return UUID.fromString(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void marry(UUID husband, UUID wife) {
        data.set("married." + husband, wife.toString());
        data.set("married." + wife, husband.toString());

        data.set("role." + husband, MarriageRole.HUSBAND.name());
        data.set("role." + wife, MarriageRole.WIFE.name());

        save();
    }

    public void divorce(UUID a, UUID b) {
        data.set("married." + a, null);
        data.set("married." + b, null);

        data.set("role." + a, null);
        data.set("role." + b, null);

        save();
    }

    public MarriageRole getRole(UUID uuid) {
        String s = data.getString("role." + uuid);
        if (s == null) return null;
        try {
            return MarriageRole.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<UUID> getAllMarried() {
        List<UUID> list = new ArrayList<>();
        if (!data.isConfigurationSection("married")) return list;

        for (String key : data.getConfigurationSection("married").getKeys(false)) {
            try {
                list.add(UUID.fromString(key));
            } catch (IllegalArgumentException ignored) {}
        }
        return list;
    }

    /* ===================== ЧАТ ===================== */

    public boolean isChatVisible(UUID uuid) {
        return data.getBoolean("chat-visible." + uuid, true);
    }

    public void setChatVisible(UUID uuid, boolean visible) {
        data.set("chat-visible." + uuid, visible);
        save();
    }

    /* ===================== ТП ===================== */

    public int getDailyTpUsed(UUID uuid) {
        return data.getInt("daily-tp." + uuid, 0);
    }

    public void setDailyTpUsed(UUID uuid, int used) {
        data.set("daily-tp." + uuid, used);
        save();
    }

    /* ===================== SAVE ===================== */

    private void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить marriage.yml");
        }
    }

    /* ===================== RELOAD ===================== */

    public void reload() {
        this.data = YamlConfiguration.loadConfiguration(file);
        plugin.getLogger().info("MarriageManager перезагружен");
    }
}
