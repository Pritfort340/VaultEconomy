package me.kodysimpson.vaulteconomy.economy;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CustomEconomy implements Economy {

    private final Map<String, Double> balances =
            Collections.synchronizedMap(new HashMap<>());

    private final VaultEconomy plugin;
    private final File file;
    private YamlConfiguration data;

    public CustomEconomy(VaultEconomy plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "savebal.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Не удалось создать savebal.yml");
            }
        }

        this.data = YamlConfiguration.loadConfiguration(file);
    }

    /* ===================== RELOAD ===================== */

    public synchronized void reload() {
        saveBalances();
        this.data = YamlConfiguration.loadConfiguration(file);
        loadBalances();
        plugin.getLogger().info("CustomEconomy успешно перезагружена");
    }

    /* ===================== LOAD / SAVE ===================== */

    public synchronized void loadBalances() {
        balances.clear();
        ConfigurationSection sec = data.getConfigurationSection("balances");
        if (sec == null) return;

        for (String key : sec.getKeys(false)) {
            balances.put(key, sec.getDouble(key));
        }
    }

    public synchronized void saveBalances() {
        data.set("balances", null);

        for (Map.Entry<String, Double> e : balances.entrySet()) {
            data.set("balances." + e.getKey(), e.getValue());
        }

        try {
            data.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка сохранения балансов");
        }
    }

    private void create(OfflinePlayer p) {
        if (p == null || p.getName() == null) return;
        balances.putIfAbsent(p.getName(), 0.0);
    }

    /* ===================== VAULT BASE ===================== */

    @Override public boolean isEnabled() { return true; }
    @Override public String getName() { return "VaultEconomy"; }
    @Override public boolean hasBankSupport() { return false; }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        create(player);
        return true;
    }

    @Override
    public boolean hasAccount(String playerName) {
        return balances.containsKey(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        create(player);
        return balances.get(player.getName());
    }

    @Override
    public double getBalance(String playerName) {
        return balances.getOrDefault(playerName, 0.0);
    }

    /* ===================== HAS ===================== */

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public boolean has(String playerName, double amount) {
        return getBalance(playerName) >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    /* ===================== DEPOSIT / WITHDRAW ===================== */

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        create(player);
        balances.put(player.getName(), getBalance(player) + amount);
        return new EconomyResponse(
                amount,
                getBalance(player),
                EconomyResponse.ResponseType.SUCCESS,
                null
        );
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (!has(player, amount)) {
            return new EconomyResponse(
                    0,
                    getBalance(player),
                    EconomyResponse.ResponseType.FAILURE,
                    "Недостаточно средств"
            );
        }

        balances.put(player.getName(), getBalance(player) - amount);
        return new EconomyResponse(
                amount,
                getBalance(player),
                EconomyResponse.ResponseType.SUCCESS,
                null
        );
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        return depositPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(playerName), amount);
    }

    /* ===================== WORLD METHODS ===================== */

    @Override
    public EconomyResponse depositPlayer(String playerName, String world, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String world, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String world, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String world, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean hasAccount(String playerName, String world) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String world) {
        return hasAccount(player);
    }

    /* ===================== FORMAT ===================== */

    @Override public String format(double amount) {
        return String.format("%.2f ⛁", amount);
    }

    @Override public int fractionalDigits() { return 2; }
    @Override public String currencyNameSingular() { return "монета"; }
    @Override public String currencyNamePlural() { return "монеты"; }

    /* ===================== BANK (NOT USED) ===================== */

    @Override public EconomyResponse createBank(String name, String player) { return null; }
    @Override public EconomyResponse createBank(String name, OfflinePlayer player) { return null; }
    @Override public EconomyResponse deleteBank(String name) { return null; }
    @Override public EconomyResponse bankBalance(String name) { return null; }
    @Override public EconomyResponse bankDeposit(String name, double amount) { return null; }
    @Override public EconomyResponse bankWithdraw(String name, double amount) { return null; }
    @Override public EconomyResponse bankHas(String name, double amount) { return null; }
    @Override public EconomyResponse isBankOwner(String name, String player) { return null; }
    @Override public EconomyResponse isBankOwner(String name, OfflinePlayer player) { return null; }
    @Override public EconomyResponse isBankMember(String name, String player) { return null; }
    @Override public EconomyResponse isBankMember(String name, OfflinePlayer player) { return null; }
    @Override public List<String> getBanks() { return Collections.emptyList(); }

    /* ===================== CREATE ACCOUNT ===================== */

    @Override
    public boolean createPlayerAccount(String playerName) {
        balances.putIfAbsent(playerName, 0.0);
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        create(player);
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String world) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String world) {
        return createPlayerAccount(player);
    }
}