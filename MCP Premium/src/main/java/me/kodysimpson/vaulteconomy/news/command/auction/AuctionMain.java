package me.kodysimpson.vaulteconomy.news.command.auction;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.economy.CustomEconomy;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AuctionMain implements CommandExecutor {

    private static AuctionMain instance;
    public static AuctionInventory auctionInventory;

    private final CustomEconomy economy;
    private final VaultEconomy plugin;
    private final PvpManager pvpManager;

    public AuctionMain(VaultEconomy plugin) {
        instance = this;
        this.plugin = plugin;

        // ✅ ТЕПЕРЬ МЕТОД СУЩЕСТВУЕТ
        this.economy = plugin.getEconomy();
        this.pvpManager = plugin.getPvpManager();

        auctionInventory = new AuctionInventory();
        Bukkit.getPluginManager().registerEvents(auctionInventory, plugin);

        plugin.getLogger().info("[AUCTION] Auction system enabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cТолько для игроков!");
            return true;
        }

        // ❌ ЗАПРЕТ В PvP
        if (pvpManager.isInPvp(p) && !p.hasPermission("vaulteconomy.pvp.bypass")) {
            p.sendMessage("§cНельзя использовать аукцион во время PvP!");
            return true;
        }

        if (!p.hasPermission("vaulteconomy.auction")) {
            p.sendMessage("§c§lНет прав для аукциона!");
            return true;
        }

        if (args.length == 0) {
            auctionInventory.openMain(p);
            return true;
        }

        if (args[0].equalsIgnoreCase("sell") && args.length == 2) {

            if (!p.hasPermission("vaulteconomy.auction.sell")) {
                p.sendMessage("§c§lНет прав для продажи!");
                return true;
            }

            try {
                new AuctionSell().sell(p, args[1]);
            } catch (Exception e) {
                p.sendMessage("§c§lОшибка продажи! Держите предмет в руке.");
                plugin.getLogger().warning("AuctionSell error: " + e.getMessage());
            }
            return true;
        }

        p.sendMessage("§c§lИспользование: §f/ah §7или §f/ah sell <цена>");
        return true;
    }

    public static AuctionMain getInstance() {
        return instance;
    }

    public CustomEconomy getEconomy() {
        return economy;
    }
}