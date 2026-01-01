package me.kodysimpson.vaulteconomy.news.command.auction;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuctionPagination {

    public static void openMarketPage(Player p, String category, int page) {
        AuctionInventory.cleanup(); // Очистка истекших

        List<AuctionItem> filteredItems = new ArrayList<>();
        // ФИЛЬТР ПО КАТЕГОРИИ
        for (AuctionItem item : AuctionInventory.items) {
            if (category.equals("all") || item.category.equalsIgnoreCase(category)) {
                filteredItems.add(item);
            }
        }

        int itemsPerPage = 35;
        int totalPages = (int) Math.ceil((double) filteredItems.size() / itemsPerPage);
        if (page > totalPages) page = totalPages;
        if (page < 1) page = 1;

        Inventory inv = Bukkit.createInventory(null, 54, "§b§lЛОТЫ §7(" + category + ") §f" + page + "/" + totalPages);

        // ✅ ЧЕТКАЯ СЕТКА 5x7
        int[] slots = {10,11,12,13,14,15,16,19,20,21,22,23,24,25,28,29,30,31,32,33,34,37,38,39,40,41,42,43};
        int startIndex = (page - 1) * itemsPerPage;

        for (int i = 0; i < slots.length; i++) {
            int itemIndex = startIndex + i;
            if (itemIndex < filteredItems.size()) {
                inv.setItem(slots[i], filteredItems.get(itemIndex).getDisplay());
            }
        }

        // ✅ КНОПКИ ВСЕГДА РЯДОМ - СЛОтЫ 47,48,49,50,51,52
        inv.setItem(47, createItem(Material.OAK_SIGN, "§7"));
        if (page > 1) {
            inv.setItem(48, createItem(Material.ARROW, "§e◀ §fПредыдущая"));
        }
        inv.setItem(49, createItem(Material.BARRIER, "§cЗакрыть"));
        if (page < totalPages) {
            inv.setItem(50, createItem(Material.ARROW, "§e▶ §fСледующая"));
        }
        inv.setItem(51, createItem(Material.OAK_SIGN, "§7"));

        p.openInventory(inv);
    }

    private static ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }
}