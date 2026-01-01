package me.kodysimpson.vaulteconomy.news.command;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class RepairCommand implements CommandExecutor {

    private final VaultEconomy plugin;

    public RepairCommand(VaultEconomy plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Команда только для игроков!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("vaulteconomy.repair")) {
            player.sendMessage(ChatColor.RED + "У вас нет прав!");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();

        // Нет предмета
        if (item.getType() == Material.AIR) {
            String msg = plugin.getConfig().getString("utilities.repair.no-item", "&c❌ Возьмите предмет в руку!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            return true;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            player.sendMessage(ChatColor.RED + "❌ Нельзя починить!");
            return true;
        }

        // ✅ ЧИНИТ ВСЕ предметы с прочностью!
        if (meta instanceof Damageable) {
            Damageable damageable = (Damageable) meta;
            damageable.setDamage(0); // ПОЛНЫЙ РЕМОНТ
            item.setItemMeta(meta);

            String msg = plugin.getConfig().getString("utilities.repair.repaired", "&a✅ Предмет полностью починен!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        } else {
            // Блокируется только если вообще нет meta
            String msg = plugin.getConfig().getString("utilities.repair.no-durability", "&c❌ Этот предмет нельзя починить!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }

        player.updateInventory();
        return true;
    }
}