package me.kodysimpson.vaulteconomy.news.command.auction;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AuctionSell {
    public void sell(Player p, String priceStr) {
        // ✅ ПРОВЕРКА СЛОТОВ ПО ПЕРМИШЕНАМ
        AuctionInventory.cleanup();
        if (!AuctionInventory.canSell(p)) {
            int used = AuctionInventory.getPlayerSlotsUsed(p.getUniqueId());
            int max = AuctionInventory.getMaxSlots(p);
            p.sendMessage("§c§l❌ Нет слотов! §e" + used + "/" + max);
            p.sendMessage("§7Пермишн: §eauction.slot.group." + (max + 1));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
            return;
        }

        try {
            double price = Double.parseDouble(priceStr);
            if (price < 1500 || price > 1000000000000000L) {  // ← 10 МИЛЛИАРДОВ!
                p.sendMessage("§6§lЦена§r§0: §fот §a1.5к §fдо §41 КВАДРИЛЛИОН!");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
                return;
            }

            ItemStack hand = p.getInventory().getItemInMainHand();
            if (hand.getType() == Material.AIR || hand.getAmount() == 0) {
                p.sendMessage("§c§lДержите предмет в главной руке!");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
                return;
            }

            if (hand.getAmount() > 64) {
                p.sendMessage("§c§lМаксимум 64 предмета!");
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
                return;
            }

            String category = getCategory(hand.getType());
            AuctionItem item = new AuctionItem(p.getUniqueId(), hand.clone(), price, category);
            AuctionInventory.addItem(item);

            p.getInventory().getItemInMainHand().setAmount(0);

            int usedAfter = AuctionInventory.getPlayerSlotsUsed(p.getUniqueId());
            int maxSlots = AuctionInventory.getMaxSlots(p);

            p.sendMessage("§a§l✓ Выставлен за §e$" + (int)price + " §7(" + category + ")");
            p.sendMessage("§7Слоты: §e" + usedAfter + "/" + maxSlots);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);

        } catch (NumberFormatException e) {
            p.sendMessage("§c§lЧисло! Пример: §f/ah sell 100");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 0.8f);
        }
    }

    private String getCategory(Material mat) {
        String name = mat.name().toUpperCase();

        if (name.contains("SWORD") || name.contains("BOW") || name.contains("CROSSBOW") ||
                name.contains("TRIDENT") || name.contains("AXE") ||
                name.contains("HELMET") || name.contains("CHESTPLATE") ||
                name.contains("LEGGINGS") || name.contains("BOOTS")) {
            return "ОРУЖИЕ";
        }

        if (name.contains("PICKAXE") || name.contains("SHOVEL") || name.contains("HOE") ||
                name.contains("FISHING_ROD")) {
            return "ИНСТРУМЕНТЫ";
        }

        if (name.contains("ORE") || name.contains("INGOT") || name.contains("BLOCK") ||
                name.contains("LOG") || name.contains("PLANKS") || name.contains("WOOL") ||
                name.contains("COBBLESTONE") || name.contains("STONE")) {
            return "РЕСУРСЫ";
        }

        return "ВСЕ";
    }
}
