package me.kodysimpson.vaulteconomy.news.command.auction;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.economy.CustomEconomy;
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

    public AuctionMain(VaultEconomy plugin) {
        instance = this;
        this.economy = plugin.getEconomy();
        this.plugin = plugin;

        auctionInventory = new AuctionInventory();
        Bukkit.getPluginManager().registerEvents(auctionInventory, plugin);

        plugin.getLogger().info("§a[АУКЦИОН] §fУспешно запущен!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cТолько для игроков!");
            return true;
        }

        if (!p.hasPermission("vaulteconomy.auction")) {
            p.sendMessage("§c§lНет прав для аукциона!");
            return true;
        }

        // ✅ ВСЕ АЛИАСЫ РАБОТАЮТ! (ah, auction, market, auc, ac)
        String cmdName = cmd.getName().toLowerCase();
        if (cmdName.equals("ah") || cmdName.equals("auction") ||
                cmdName.equals("market") || cmdName.equals("auc") || cmdName.equals("ac")) {

            if (args.length == 0) {
                auctionInventory.openMain(p);
                return true;
            } else if (args[0].equalsIgnoreCase("sell") && args.length == 2) {
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
            } else {
                p.sendMessage("§c§l§nИспользование: §f/ah §7или §f/ah sell <цена>");
                return true;
            }
        }
        return true;
    }

    public static AuctionMain getInstance() { return instance; }
    public CustomEconomy getEconomy() { return economy; }
}
