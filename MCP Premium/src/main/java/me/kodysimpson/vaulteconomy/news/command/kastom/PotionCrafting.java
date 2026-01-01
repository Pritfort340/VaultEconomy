package me.kodysimpson.vaulteconomy.news.command.kastom;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

public class PotionCrafting implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack[] matrix = inv.getMatrix();

        if (matchesRecipe(matrix)) {
            inv.setResult(createStrength3());
        } else {
            inv.setResult(null);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraftClick(InventoryClickEvent event) {
        if (event.getInventory().getType() != InventoryType.WORKBENCH) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;

        CraftingInventory inv = (CraftingInventory) event.getInventory();
        ItemStack[] matrix = inv.getMatrix();

        if (matchesRecipe(matrix)) {
            event.setCancelled(true);

            if (event.getRawSlot() == 0) {
                craftPotion(player, inv, event);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraftItem(CraftItemEvent event) {
        if (event.getInventory() instanceof CraftingInventory inv) {
            ItemStack[] matrix = inv.getMatrix();
            if (matchesRecipe(matrix)) {
                event.setCancelled(true);
            }
        }
    }

    private void craftPotion(Player player, CraftingInventory inv, InventoryClickEvent event) {
        ItemStack[] matrix = inv.getMatrix();

        // ✅ ПОТРЕБЛЯЕМ ИНГРЕДИЕНТЫ
        consumeIngredients(player, inv);

        // ✅ ДАЕМ ЗЕЛЬЕ
        player.getInventory().addItem(createStrength3());

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
        player.sendMessage("§a§l✓ Зелье Силы III создано! (45с)");

        inv.setResult(null);
    }

    // ✅ ✅ ИСПРАВЛЕННОЕ ПОТРЕБЛЕНИЕ
    private void consumeIngredients(Player player, CraftingInventory inv) {
        int[] recipeSlots = {0,1,2,3,4,5,6,7,8};

        for (int slot : recipeSlots) {
            ItemStack item = inv.getItem(slot);
            if (item != null && item.getAmount() > 0) {
                if (item.getAmount() > 1) {
                    // Возвращаем остатки
                    ItemStack remainder = item.clone();
                    remainder.setAmount(item.getAmount() - 1);
                    player.getInventory().addItem(remainder);
                }
                // ✅ ОЧИЩАЕМ СЛОТ
                inv.setItem(slot, null);
            }
        }
    }

    private boolean matchesRecipe(ItemStack[] slots) {
        return checkSlot(slots, 0, Material.BLAZE_ROD) &&
                checkSlot(slots, 1, Material.NETHER_STAR) &&
                checkSlot(slots, 2, Material.BLAZE_ROD) &&
                checkSlot(slots, 3, Material.IRON_SWORD) &&
                checkSlot(slots, 4, Material.POTION) &&
                checkSlot(slots, 5, Material.IRON_SWORD) &&
                checkSlot(slots, 6, Material.BLAZE_ROD) &&
                checkSlot(slots, 7, Material.GHAST_TEAR) &&
                checkSlot(slots, 8, Material.BLAZE_ROD);
    }

    private boolean checkSlot(ItemStack[] slots, int index, Material material) {
        return slots[index] != null && slots[index].getType() == material;
    }

    public static ItemStack createStrength3() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.addCustomEffect(PotionEffectType.STRENGTH.createEffect(900, 2), true);
        meta.setDisplayName("§cЗелье Силы III (45с)");
        potion.setItemMeta(meta);
        return potion;
    }
}