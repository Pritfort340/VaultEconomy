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

import java.util.*;

public class AuctionInventory implements Listener {

    public static final List<AuctionItem> items = new ArrayList<>();

    public void openMain(Player p) {

        if (VaultEconomy.getInstance().getPvpManager().isInPvp(p)) {
            p.sendMessage(AuctionMessages.get("pvp-block"));
            return;
        }

        Inventory inv = Bukkit.createInventory(null, 27, "§6§lАУКЦИОН");

        inv.setItem(10, createItem(Material.CHEST, "§e§lВСЕ ТОВАРЫ"));
        inv.setItem(12, createItem(Material.DIAMOND_SWORD, "§b§lОРУЖИЕ"));
        inv.setItem(14, createItem(Material.DIAMOND_PICKAXE, "§6§lИНСТРУМЕНТЫ"));
        inv.setItem(16, createItem(Material.EMERALD, "§a§lРЕСУРСЫ"));

        int used = getPlayerSlotsUsed(p.getUniqueId());
        int max = getMaxSlots(p);

        ItemStack info = createItem(Material.GOLD_INGOT, "§6§lБАЛАНС И СЛОТЫ");
        ItemMeta meta = info.getItemMeta();
        if (meta != null) {
            meta.setLore(Arrays.asList(
                    "§7Баланс: §e$" + (int) VaultEconomy.getInstance().getEconomy().getBalance(p),
                    "",
                    "§7Слоты: §e" + used + "/" + max,
                    "§7Доступно: §a" + (max - used)
            ));
            info.setItemMeta(meta);
        }

        inv.setItem(22, info);
        inv.setItem(4, createItem(Material.REDSTONE_BLOCK, "§c§lМОИ ТОВАРЫ"));
        p.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player p)) return;
        String title = e.getView().getTitle();

        if (!title.contains("АУКЦИОН") && !title.contains("ЛОТЫ") && !title.contains("МОИ ТОВАРЫ"))
            return;

        e.setCancelled(true);

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;

        String name = clicked.hasItemMeta() ? clicked.getItemMeta().getDisplayName() : "";

        if (title.equals("§6§lАУКЦИОН")) {
            if (name.contains("ВСЕ")) openAuction(p, "ВСЕ", 1);
            if (name.contains("ОРУЖИЕ")) openAuction(p, "ОРУЖИЕ", 1);
            if (name.contains("ИНСТРУМЕНТЫ")) openAuction(p, "ИНСТРУМЕНТЫ", 1);
            if (name.contains("РЕСУРСЫ")) openAuction(p, "РЕСУРСЫ", 1);
            if (name.contains("МОИ")) openMyItems(p);
            return;
        }

        if (title.contains("ЛОТЫ")) {
            if (name.contains("Назад")) openAuction(p, getCategory(title), getPage(title) - 1);
            else if (name.contains("Вперед")) openAuction(p, getCategory(title), getPage(title) + 1);
            else if (name.contains("Закрыть")) p.closeInventory();
            else buyItem(p, clicked);
        }

        if (title.contains("МОИ ТОВАРЫ")) {
            if (name.contains("Закрыть")) p.closeInventory();
            else returnMyItem(p, clicked);
        }
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
        int totalPages = Math.max(1, (int) Math.ceil(filtered.size() / (double) perPage));
        page = Math.max(1, Math.min(page, totalPages));

        Inventory inv = Bukkit.createInventory(null, 54,
                "§b§lЛОТЫ " + category + " §7(" + page + "/" + totalPages + ")");

        int[] slots = {
                10,11,12,13,14,15,16,
                19,20,21,22,23,24,25,
                28,29,30,31,32,33,34,
                37,38,39,40,41,42,43
        };

        int start = (page - 1) * perPage;
        for (int i = 0; i < slots.length && start + i < filtered.size(); i++) {
            inv.setItem(slots[i], filtered.get(start + i).getDisplay());
        }

        if (page > 1) inv.setItem(48, createItem(Material.ARROW, "§e◀ Назад"));
        inv.setItem(49, createItem(Material.BARRIER, "§c§l❌ Закрыть"));
        if (page < totalPages) inv.setItem(50, createItem(Material.ARROW, "§eВперед ▶"));

        p.openInventory(inv);
    }

    private void buyItem(Player p, ItemStack clicked) {

        Iterator<AuctionItem> it = items.iterator();
        while (it.hasNext()) {
            AuctionItem item = it.next();

            if (!item.getDisplay().isSimilar(clicked)) continue;

            if (VaultEconomy.getInstance().getEconomy().getBalance(p) < item.price) {
                p.sendMessage(AuctionMessages.get("no-money"));
                return;
            }

            VaultEconomy.getInstance().getEconomy().withdrawPlayer(p, item.price);
            VaultEconomy.getInstance().getEconomy()
                    .depositPlayer(Bukkit.getOfflinePlayer(item.seller), item.price * 0.97);

            p.getInventory().addItem(item.item.clone());
            it.remove();

            p.sendMessage(AuctionMessages.get("purchase-success",
                    "price", String.valueOf((int) item.price)));
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1.2f);
            p.closeInventory();
            return;
        }
    }

    private void openMyItems(Player p) {

        cleanup();
        Inventory inv = Bukkit.createInventory(null, 54, "§c§lМОИ ТОВАРЫ");

        int[] slots = {
                10,11,12,13,14,15,16,
                19,20,21,22,23,24,25,
                28,29,30,31,32,33,34,
                37,38,39,40,41,42,43
        };

        int index = 0;
        for (AuctionItem item : items) {
            if (item.seller.equals(p.getUniqueId()) && index < slots.length) {
                inv.setItem(slots[index++], item.getDisplay());
            }
        }

        if (index == 0) {
            inv.setItem(22, createItem(Material.BARRIER, "§7У вас нет товаров"));
        }

        inv.setItem(49, createItem(Material.BARRIER, "§c§l❌ Закрыть"));
        p.openInventory(inv);
    }

    private void returnMyItem(Player p, ItemStack clicked) {

        Iterator<AuctionItem> it = items.iterator();
        while (it.hasNext()) {
            AuctionItem item = it.next();
            if (item.seller.equals(p.getUniqueId()) && item.getDisplay().isSimilar(clicked)) {
                p.getInventory().addItem(item.item.clone());
                it.remove();
                p.sendMessage(AuctionMessages.get("item-returned"));
                p.closeInventory();
                return;
            }
        }
    }

    public static boolean canSell(Player p) {
        return getPlayerSlotsUsed(p.getUniqueId()) < getMaxSlots(p);
    }

    public static void addItem(AuctionItem item) {
        items.add(item);
        Bukkit.broadcastMessage(AuctionMessages.get("item-listed",
                "player", Bukkit.getOfflinePlayer(item.seller).getName(),
                "price", String.valueOf((int) item.price),
                "category", item.category));
    }

    public static int getMaxSlots(Player p) {
        int max = 5;
        for (int i = 1; i <= 1000; i++) {
            if (p.hasPermission("auction.slot.group." + i)) max = i;
        }
        return max;
    }

    public static int getPlayerSlotsUsed(UUID uuid) {
        cleanup();
        int count = 0;
        for (AuctionItem item : items) {
            if (item.seller.equals(uuid)) count++;
        }
        return count;
    }

    public static void cleanup() {
        items.removeIf(item -> System.currentTimeMillis() > item.expires);
    }

    private String getCategory(String title) {
        if (title.contains("ОРУЖИЕ")) return "ОРУЖИЕ";
        if (title.contains("ИНСТРУМЕНТЫ")) return "ИНСТРУМЕНТЫ";
        if (title.contains("РЕСУРСЫ")) return "РЕСУРСЫ";
        return "ВСЕ";
    }

    private int getPage(String title) {
        try {
            String part = title.substring(title.indexOf("(") + 1, title.indexOf(")"));
            return Integer.parseInt(part.split("/")[0]);
        } catch (Exception e) {
            return 1;
        }
    }

    private ItemStack createItem(Material mat, String name) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        return item;
    }
}