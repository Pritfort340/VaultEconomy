package me.kodysimpson.vaulteconomy.news.command.auction;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AuctionInventory implements Listener {
    public static List<AuctionItem> items = new ArrayList<>();

    public void openMain(Player p) {
        Inventory inv = Bukkit.createInventory(null, 27, "§6§lАУКЦИОН");

        // Категории
        inv.setItem(10, createItem(Material.CHEST, "§e§lВСЕ ТОВАРЫ", "§7Открыть все лоты"));
        inv.setItem(12, createItem(Material.DIAMOND_SWORD, "§b§lОРУЖИЕ", "§7Мечи, луки, арбалеты"));
        inv.setItem(14, createItem(Material.DIAMOND_PICKAXE, "§6§lИНСТРУМЕНТЫ", "§7Кирки, лопаты, топоры"));
        inv.setItem(16, createItem(Material.EMERALD, "§a§lРЕСУРСЫ", "§7Руда, блоки, материалы"));

        // ✅ БАЛАНС + СЛОТЫ
        ItemStack info = createItem(Material.GOLD_INGOT, "§6§lБАЛАНС И СЛОТЫ");
        ItemMeta infoMeta = info.getItemMeta();
        int slotsUsed = getPlayerSlotsUsed(p.getUniqueId());
        int maxSlots = getMaxSlots(p);
        infoMeta.setLore(Arrays.asList(
                "§7Ваш баланс: §e$" + VaultEconomy.getInstance().getEconomy().format(
                        VaultEconomy.getInstance().getEconomy().getBalance(p)
                ),
                "",
                "§7Слоты: §e" + slotsUsed + "/" + maxSlots,
                "§7Доступно: §a" + (maxSlots - slotsUsed)
        ));
        info.setItemMeta(infoMeta);
        inv.setItem(22, info);

        inv.setItem(4, createItem(Material.REDSTONE_BLOCK, "§c§lМОИ ТОВАРЫ", "§7Забрать свои лоты"));
        p.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        String title = e.getView().getTitle();
        if (!title.equals("§6§lАУКЦИОН") && !title.contains("ЛОТЫ") && !title.equals("§c§lМОИ ТОВАРЫ"))
            return;

        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;

        String name = item.getItemMeta().getDisplayName();

        // Главное меню
        if (title.equals("§6§lАУКЦИОН")) {
            if (name.equals("§e§lВСЕ ТОВАРЫ")) openAuction(p, "ВСЕ", 1);
            else if (name.equals("§b§lОРУЖИЕ")) openAuction(p, "ОРУЖИЕ", 1);
            else if (name.equals("§6§lИНСТРУМЕНТЫ")) openAuction(p, "ИНСТРУМЕНТЫ", 1);
            else if (name.equals("§a§lРЕСУРСЫ")) openAuction(p, "РЕСУРСЫ", 1);
            else if (name.equals("§c§lМОИ ТОВАРЫ")) openMyItems(p);
            return;
        }

        // Аукцион
        if (title.contains("ЛОТЫ")) {
            if (name.equals("§c§l❌ Закрыть")) {
                p.closeInventory();
                return;
            }
            if (name.equals("§e◀ Назад")) {
                openAuction(p, getCategory(title), getPage(title) - 1);
                return;
            }
            if (name.equals("§eВперед ▶")) {
                openAuction(p, getCategory(title), getPage(title) + 1);
                return;
            }
            buyItem(p, item);
            return;
        }

        // Мои товары
        if (title.equals("§c§lМОИ ТОВАРЫ")) {
            if (name.equals("§c§l❌ Закрыть")) {
                p.closeInventory();
                return;
            }
            returnMyItem(p, item);
        }
    }

    // ✅ ПЕРМИШЕНЫ СЛОТОВ
    public static int getMaxSlots(Player p) {
        int maxSlots = 0;
        for (int i = 1; i <= 1000; i++) {
            if (p.hasPermission("auction.slot.group." + i)) {
                maxSlots = i;
            }
        }
        return Math.max(maxSlots, 5);
    }

    public static int getPlayerSlotsUsed(UUID playerId) {
        int count = 0;
        AuctionInventory.cleanup(); // ✅ Очистка перед подсчетом
        for (AuctionItem item : items) {
            if (item.seller.equals(playerId)) {
                count++;
            }
        }
        return count;
    }

    public static boolean canSell(Player p) {
        int used = getPlayerSlotsUsed(p.getUniqueId());
        int max = getMaxSlots(p);
        return used < max;
    }

    private void openAuction(Player p, String category, int page) {
        cleanup();
        List<AuctionItem> filtered = new ArrayList<>();

        for (AuctionItem item : items) {
            if (category.equals("ВСЕ") || item.category.equals(category)) {
                filtered.add(item);
            }
        }

        int perPage = 28;
        int totalPages = (int) Math.ceil(filtered.size() / (double) perPage);
        if (page > totalPages) page = totalPages;
        if (page < 1) page = 1;

        Inventory inv = Bukkit.createInventory(null, 54, "§b§lЛОТЫ " + category + " §7(" + page + "/" + totalPages + ")");

        int[] slots = {10,11,12,13,14,15,16, 19,20,21,22,23,24,25, 28,29,30,31,32,33,34, 37,38,39,40,41,42,43};
        int start = (page - 1) * perPage;

        for (int i = 0; i < slots.length && (start + i) < filtered.size(); i++) {
            inv.setItem(slots[i], filtered.get(start + i).getDisplay());
        }

        if (page > 1) inv.setItem(48, createItem(Material.ARROW, "§e◀ Назад"));
        inv.setItem(49, createItem(Material.BARRIER, "§c§l❌ Закрыть"));
        if (page < totalPages) inv.setItem(50, createItem(Material.ARROW, "§eВперед ▶"));

        p.openInventory(inv);
    }

    private void openMyItems(Player p) {
        cleanup();
        Inventory inv = Bukkit.createInventory(null, 54, "§c§lМОИ ТОВАРЫ");
        int[] slots = {10,11,12,13,14,15,16, 19,20,21,22,23,24,25, 28,29,30,31,32,33,34, 37,38,39,40,41,42,43};
        int slotIndex = 0;

        boolean hasItems = false;
        for (AuctionItem item : items) {
            if (item.seller.equals(p.getUniqueId()) && slotIndex < slots.length) {
                inv.setItem(slots[slotIndex], item.getDisplay());
                hasItems = true;
                slotIndex++;
            }
        }

        if (!hasItems) {
            inv.setItem(22, createItem(Material.BARRIER, "§7У вас нет товаров на аукционе"));
        }
        inv.setItem(49, createItem(Material.BARRIER, "§c§l❌ Закрыть"));
        p.openInventory(inv);
    }

    // ✅ ✅ ✅ КОМИССИЯ 3% (100$ → 97$)
    private void buyItem(Player p, ItemStack clicked) {
        cleanup();
        for (AuctionItem item : items) {
            if (item.getDisplay().isSimilar(clicked)) {
                if (VaultEconomy.getInstance().getEconomy().getBalance(p) < item.price) {
                    p.sendMessage("§c§lНедостаточно средств!");
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
                    return;
                }

                // ✅ ПОКУПАТЕЛЬ платит ПОЛНУЮ цену
                VaultEconomy.getInstance().getEconomy().withdrawPlayer(p, item.price);

                // ✅ ПРОДАВЦУ 97% (3% комиссия)
                double sellerGets = item.price * 0.97;
                VaultEconomy.getInstance().getEconomy().depositPlayer(
                        Bukkit.getOfflinePlayer(item.seller), sellerGets);

                p.getInventory().addItem(item.item.clone());
                items.remove(item);

                p.sendMessage("§a§l✓ Покупка успешна! §e-$" + (int)item.price);
                p.sendMessage("§7Продавец получил: §e$" + (int)sellerGets + " §7(комиссия 3%)");
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.5f);
                Bukkit.broadcastMessage("§6[АУКЦИОН] §f" + p.getName() + " §aкупил за §e$" + (int)item.price);
                p.closeInventory();
                return;
            }
        }
    }

    private void returnMyItem(Player p, ItemStack clicked) {
        cleanup();
        for (AuctionItem item : items) {
            if (item.getDisplay().isSimilar(clicked) && item.seller.equals(p.getUniqueId())) {
                p.getInventory().addItem(item.item.clone());
                items.remove(item);
                p.sendMessage("§a§l✓ Товар возвращен в инвентарь!");
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
                p.closeInventory();
                return;
            }
        }
    }

    private String getCategory(String title) {
        if (title.contains("ВСЕ")) return "ВСЕ";
        if (title.contains("ОРУЖИЕ")) return "ОРУЖИЕ";
        if (title.contains("ИНСТРУМЕНТЫ")) return "ИНСТРУМЕНТЫ";
        if (title.contains("РЕСУРСЫ")) return "РЕСУРСЫ";
        return "ВСЕ";
    }

    private int getPage(String title) {
        try {
            String[] parts = title.split("/");
            if (parts.length > 1) {
                String pagePart = parts[0].substring(parts[0].lastIndexOf(" ") + 1).trim();
                return Integer.parseInt(pagePart);
            }
        } catch (Exception ignored) {}
        return 1;
    }

    public static void addItem(AuctionItem item) {
        items.add(item);
        Bukkit.broadcastMessage("§6[АУКЦИОН] §f" +
                Bukkit.getOfflinePlayer(item.seller).getName() +
                " §7выставил §e$" + (int)item.price + " §7(" + item.category + ")");
    }

    public static void cleanup() {
        items.removeIf(item -> System.currentTimeMillis() > item.expires);
    }

    private ItemStack createItem(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if (lore.length > 0) meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }
}