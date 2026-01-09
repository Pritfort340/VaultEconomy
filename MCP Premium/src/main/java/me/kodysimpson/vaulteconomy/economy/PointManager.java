package me.kodysimpson.vaulteconomy.economy;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PointManager {

    private final VaultEconomy plugin;
    private final Map<String, PointCurrency> currencies = new HashMap<>();

    private final File file;
    private final YamlConfiguration data;

    public PointManager(VaultEconomy plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "points.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Ошибка создания points.yml");
            }
        }

        this.data = YamlConfiguration.loadConfiguration(file);
    }

    public boolean createCurrency(String name, String displayName, String symbol) {
        String id = name.toLowerCase();
        if (currencies.containsKey(id)) {
            return false;
        }
        currencies.put(id, new PointCurrency(id, displayName, symbol));
        return true;
    }

    public boolean removeCurrency(String name) {
        return currencies.remove(name.toLowerCase()) != null;
    }

    public PointCurrency getCurrency(String name) {
        return currencies.get(name.toLowerCase());
    }

    public Collection<PointCurrency> getCurrencies() {
        return currencies.values();
    }

    public void load() {
        currencies.clear();

        ConfigurationSection curSection = data.getConfigurationSection("currencies");
        if (curSection == null) return;

        for (String id : curSection.getKeys(false)) {
            String base = "currencies." + id;
            String display = data.getString(base + ".display", id);
            String symbol = data.getString(base + ".symbol", "");

            PointCurrency currency = new PointCurrency(id, display, symbol);

            ConfigurationSection balSec = data.getConfigurationSection(base + ".balances");
            if (balSec != null) {
                for (String key : balSec.getKeys(false)) {
                    double amount = balSec.getDouble(key);
                    OfflinePlayer p;
                    try {
                        p = Bukkit.getOfflinePlayer(UUID.fromString(key));
                    } catch (IllegalArgumentException e) {
                        p = Bukkit.getOfflinePlayer(key);
                    }
                    currency.setBalance(p, amount);
                }
            }

            currencies.put(id.toLowerCase(), currency);
        }
    }

    public void save() {
        data.set("currencies", null);

        for (PointCurrency cur : currencies.values()) {
            String base = "currencies." + cur.getName();

            data.set(base + ".display", cur.getDisplayName());
            data.set(base + ".symbol", cur.getSymbol());

            for (Map.Entry<String, Double> e : cur.getBalances().entrySet()) {
                data.set(base + ".balances." + e.getKey(), e.getValue());
            }
        }

        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка сохранения points.yml");
        }
    }
}