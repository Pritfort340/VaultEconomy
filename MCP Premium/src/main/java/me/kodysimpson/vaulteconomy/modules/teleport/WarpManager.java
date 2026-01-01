package me.kodysimpson.vaulteconomy.modules.teleport;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WarpManager {

    private final VaultEconomy plugin;
    private final File file;
    private final YamlConfiguration data;

    public WarpManager(VaultEconomy plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "warps.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось создать warps.yml");
            }
        }

        this.data = YamlConfiguration.loadConfiguration(file);
    }

    // ======= SPAWN =======
    public void setSpawn(Location loc) {
        setLocation("spawn", loc);
    }

    public Location getSpawn() {
        return getLocation("spawn");
    }

    // ======= WARP =======
    public void setWarp(String name, Location loc) {
        setLocation("warps." + name.toLowerCase(), loc);
    }

    public Location getWarp(String name) {
        return getLocation("warps." + name.toLowerCase());
    }

    public void delWarp(String name) {
        data.set("warps." + name.toLowerCase(), null);
        save();
    }

    public List<String> getWarps() {
        String path = "warps";
        if (!data.isConfigurationSection(path)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(data.getConfigurationSection(path).getKeys(false));
    }

    // ======= HOMES (несколько на игрока) =======
    // путь: homes.<uuid>.<homeName>....

    public void setHome(OfflinePlayer player, String homeName, Location loc) {
        UUID uuid = player.getUniqueId();
        setLocation("homes." + uuid + "." + homeName.toLowerCase(), loc);
    }

    public Location getHome(OfflinePlayer player, String homeName) {
        UUID uuid = player.getUniqueId();
        return getLocation("homes." + uuid + "." + homeName.toLowerCase());
    }

    public void delHome(OfflinePlayer player, String homeName) {
        UUID uuid = player.getUniqueId();
        data.set("homes." + uuid + "." + homeName.toLowerCase(), null);
        save();
    }

    public List<String> getHomes(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        String path = "homes." + uuid;
        if (!data.isConfigurationSection(path)) {
            return Collections.emptyList();
        }
        return new ArrayList<>(data.getConfigurationSection(path).getKeys(false));
    }

    // ======= ВСПОМОГАТЕЛЬНЫЕ =======
    private void setLocation(String path, Location loc) {
        if (loc == null || loc.getWorld() == null) return;

        data.set(path + ".world", loc.getWorld().getName());
        data.set(path + ".x", loc.getX());
        data.set(path + ".y", loc.getY());
        data.set(path + ".z", loc.getZ());
        data.set(path + ".yaw", loc.getYaw());
        data.set(path + ".pitch", loc.getPitch());
        save();
    }

    private Location getLocation(String path) {
        String worldName = data.getString(path + ".world");
        if (worldName == null) return null;

        var world = Bukkit.getWorld(worldName);
        if (world == null) return null;

        double x = data.getDouble(path + ".x");
        double y = data.getDouble(path + ".y");
        double z = data.getDouble(path + ".z");
        float yaw = (float) data.getDouble(path + ".yaw");
        float pitch = (float) data.getDouble(path + ".pitch");

        return new Location(world, x, y, z, yaw, pitch);
    }

    private void save() {
        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Не удалось сохранить warps.yml");
        }
    }
}