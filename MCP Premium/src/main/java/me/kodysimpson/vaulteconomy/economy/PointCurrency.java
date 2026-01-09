package me.kodysimpson.vaulteconomy.economy;

import org.bukkit.OfflinePlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PointCurrency {

    private final String name;
    private final String displayName;
    private final String symbol;

    private final Map<String, Double> balances =
            Collections.synchronizedMap(new HashMap<>());

    public PointCurrency(String name, String displayName, String symbol) {
        this.name = name.toLowerCase();
        this.displayName = displayName;
        this.symbol = symbol;
    }

    /* ===================== INFO ===================== */

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    /* ===================== BALANCES ===================== */

    public double getBalance(OfflinePlayer player) {
        return balances.getOrDefault(getKey(player), 0.0);
    }

    public void setBalance(OfflinePlayer player, double amount) {
        balances.put(getKey(player), Math.max(0, amount));
    }

    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    /* ===================== API (для команд) ===================== */

    // используется в /point give
    public void deposit(OfflinePlayer player, double amount) {
        setBalance(player, getBalance(player) + amount);
    }

    // используется в /point take / pay
    public boolean withdraw(OfflinePlayer player, double amount) {
        if (!has(player, amount)) return false;
        setBalance(player, getBalance(player) - amount);
        return true;
    }

    /* ===================== SAVE SUPPORT ===================== */

    public Map<String, Double> getBalances() {
        return balances;
    }

    /* ===================== UTILS ===================== */

    private String getKey(OfflinePlayer player) {
        UUID uuid = player.getUniqueId();
        return uuid != null ? uuid.toString() : player.getName();
    }
}