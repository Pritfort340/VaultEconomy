package me.kodysimpson.vaulteconomy.news.command.auction;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuctionItem {
    public UUID seller;
    public ItemStack item;
    public double price;
    public long expires;
    public String category;

    public AuctionItem(UUID seller, ItemStack item, double price, String category) {
        this.seller = seller;
        this.item = item.clone();
        this.price = price;
        this.expires = System.currentTimeMillis() + 24 * 60 * 60 * 1000L; // 24 часа
        this.category = category;
    }

    public ItemStack getDisplay() {
        ItemStack display = item.clone();
        ItemMeta meta = display.getItemMeta();
        List<String> lore = new ArrayList<>();

        if (meta.hasLore()) lore.addAll(meta.getLore());
        lore.add("");
        lore.add("§7§m━━━━━━━━━━━━━━━━━━");
        lore.add("§6Цена: §e" + (int)price);
        lore.add("§7Продавец: §f" + Bukkit.getOfflinePlayer(seller).getName());
        lore.add("§7Осталось: §a" + getTimeLeft());
        lore.add("§7Категория: §e" + category);
        lore.add("§7§m━━━━━━━━━━━━━━━━━━");
        lore.add("§a§lКЛИК §7- §fКУПИТЬ");

        meta.setLore(lore);
        display.setItemMeta(meta);
        return display;
    }

    private String getTimeLeft() {
        long left = (expires - System.currentTimeMillis()) / 1000 / 60;
        if (left > 60) return (left / 60) + "ч";
        return left + "м";
    }
}