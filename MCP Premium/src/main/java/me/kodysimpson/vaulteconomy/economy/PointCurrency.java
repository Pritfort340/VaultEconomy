package me.kodysimpson.vaulteconomy.economy;

import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PointCurrency {

    private final String name;          // id валюты (coins, gems...)
    private final String displayName;   // красивое имя
    private final String symbol;        // символ, напр. ★

    // ключом храним UUID игрока в виде строки
    private final Map<String, Double> balances = new HashMap<>();

    public PointCurrency(String name, String displayName, String symbol) {
        this.name = name.toLowerCase();
        this.displayName = displayName;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    private String key(OfflinePlayer p) {
        UUID uuid = p.getUniqueId();
        return uuid == null ? p.getName() : uuid.toString();
    }

    public double getBalance(OfflinePlayer p) {
        return balances.getOrDefault(key(p), 0.0);
    }

    public void setBalance(OfflinePlayer p, double amount) {
        balances.put(key(p), amount);
    }

    public void deposit(OfflinePlayer p, double amount) {
        setBalance(p, getBalance(p) + amount);
    }

    public boolean withdraw(OfflinePlayer p, double amount) {
        double cur = getBalance(p);
        if (cur < amount) return false;
        setBalance(p, cur - amount);
        return true;
    }

    public Map<String, Double> getBalances() {
        return balances;
    }
}