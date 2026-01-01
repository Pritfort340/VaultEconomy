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

    private final Map<String, Double> balances = new HashMap<>();
    private final VaultEconomy plugin;
    private final File file;
    private final YamlConfiguration data;

    public CustomEconomy(VaultEconomy plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "savebal.yml");

        if (!file.exists()) {
            try {
                plugin.getDataFolder().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Ошибка создания savebal.yml");
            }
        }

        this.data = YamlConfiguration.loadConfiguration(file);
    }

    public void loadBalances() {
        ConfigurationSection section = data.getConfigurationSection("balances");
        if (section == null) return;

        for (String key : section.getKeys(false)) {
            balances.put(key, section.getDouble(key));
        }
    }

    public void saveBalances() {
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
        if (p != null && p.getName() != null)
            balances.putIfAbsent(p.getName(), 0.0);
    }

    @Override public boolean isEnabled() { return true; }
    @Override public String getName() { return "VaultEconomy"; }
    @Override public boolean hasBankSupport() { return false; }

    @Override public boolean hasAccount(OfflinePlayer p){return balances.containsKey(p.getName());}
    @Override public boolean hasAccount(String s){return balances.containsKey(s);}
    @Override public boolean hasAccount(OfflinePlayer p,String w){return hasAccount(p);}
    @Override public boolean hasAccount(String s,String w){return hasAccount(s);}

    @Override public boolean createPlayerAccount(OfflinePlayer p){create(p);return true;}
    @Override public boolean createPlayerAccount(String s){balances.putIfAbsent(s,0.0);return true;}
    @Override public boolean createPlayerAccount(OfflinePlayer p,String w){return true;}
    @Override public boolean createPlayerAccount(String s,String w){return true;}

    @Override public double getBalance(OfflinePlayer p){create(p);return balances.get(p.getName());}
    @Override public double getBalance(String s){return balances.getOrDefault(s,0.0);}
    @Override public double getBalance(OfflinePlayer p,String w){return getBalance(p);}
    @Override public double getBalance(String s,String w){return getBalance(s);}

    @Override public boolean has(OfflinePlayer p,double a){return getBalance(p)>=a;}
    @Override public boolean has(String s,double a){return getBalance(s)>=a;}
    @Override public boolean has(OfflinePlayer p,String w,double a){return has(p,a);}
    @Override public boolean has(String s,String w,double a){return has(s,a);}

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer p,double a){
        create(p);
        balances.put(p.getName(),getBalance(p)+a);
        return new EconomyResponse(a,getBalance(p),EconomyResponse.ResponseType.SUCCESS,null);
    }

    @Override public EconomyResponse depositPlayer(String s,double a){return depositPlayer(Bukkit.getOfflinePlayer(s),a);}
    @Override public EconomyResponse depositPlayer(OfflinePlayer p,String w,double a){return depositPlayer(p,a);}
    @Override public EconomyResponse depositPlayer(String s,String w,double a){return depositPlayer(s,a);}

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer p,double a){
        if(getBalance(p)<a)
            return new EconomyResponse(0,getBalance(p),EconomyResponse.ResponseType.FAILURE,"Недостаточно средств");
        balances.put(p.getName(),getBalance(p)-a);
        return new EconomyResponse(a,getBalance(p),EconomyResponse.ResponseType.SUCCESS,null);
    }

    @Override public EconomyResponse withdrawPlayer(String s,double a){return withdrawPlayer(Bukkit.getOfflinePlayer(s),a);}
    @Override public EconomyResponse withdrawPlayer(OfflinePlayer p,String w,double a){return withdrawPlayer(p,a);}
    @Override public EconomyResponse withdrawPlayer(String s,String w,double a){return withdrawPlayer(s,a);}

    @Override public String format(double a){
        return String.format("%.2f %s",a,plugin.getConfig().getString("currency-symbol","⛁"));
    }

    @Override public int fractionalDigits(){return 2;}
    @Override public String currencyNameSingular(){return "монета";}
    @Override public String currencyNamePlural(){return "монеты";}

    // банки не используем
    @Override public EconomyResponse createBank(String a,String b){return null;}
    @Override public EconomyResponse createBank(String a,OfflinePlayer b){return null;}
    @Override public EconomyResponse deleteBank(String a){return null;}
    @Override public EconomyResponse bankBalance(String a){return null;}
    @Override public EconomyResponse bankDeposit(String a,double b){return null;}
    @Override public EconomyResponse bankWithdraw(String a,double b){return null;}
    @Override public EconomyResponse bankHas(String a,double b){return null;}
    @Override public EconomyResponse isBankOwner(String a,String b){return null;}
    @Override public EconomyResponse isBankOwner(String a,OfflinePlayer b){return null;}
    @Override public EconomyResponse isBankMember(String a,String b){return null;}
    @Override public EconomyResponse isBankMember(String a,OfflinePlayer b){return null;}
    @Override public List<String> getBanks(){return Collections.emptyList();}
}