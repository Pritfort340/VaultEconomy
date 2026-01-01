package me.kodysimpson.vaulteconomy.news.command.kastom;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

public class KastomCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "§cТолько игроки!");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("vaulteconomy.kastom")) {
            player.sendMessage(ChatColor.RED + "§cНет прав!");
            return true;
        }

        showHelp(player);

        // Автоматически выдаем зелье
        ItemStack potion = createStrength3();
        player.getInventory().addItem(potion);
        player.sendMessage(ChatColor.GREEN + "§a§l✓ Зелье §cСилы III §a(45 сек) выдано!");

        return true;
    }

    private void showHelp(Player player) {
        player.sendMessage(ChatColor.GOLD + "§6=== §eЗЕЛЬЯ §6===");
        player.sendMessage(ChatColor.YELLOW + "/kastom §7- §eЗелье Силы III");
        player.sendMessage(ChatColor.YELLOW + "§7Сила II + Сила II §e→ §cСила III §7на наковальне!");
    }

    private static ItemStack createStrength3() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        if (meta != null) {
            meta.addCustomEffect(PotionEffectType.STRENGTH.createEffect(900, 2), true);
            meta.setDisplayName("§cЗелье Силы III (45с)");
            potion.setItemMeta(meta);
        }
        return potion;
    }
}