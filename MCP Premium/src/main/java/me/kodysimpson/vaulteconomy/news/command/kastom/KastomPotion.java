package me.kodysimpson.vaulteconomy.news.command.kastom;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;

public class KastomPotion implements Listener {

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        ItemStack first = event.getInventory().getFirstItem();
        ItemStack second = event.getInventory().getSecondItem();

        // Сила II + Сила II → Сила III (45с)
        if (isStrength2Potion(first) && isStrength2Potion(second)) {
            event.setResult(createStrength3());
            return;
        }

        // Сила III + Сила III → Сила IV (25с) ✅ НОВОЕ!
        if (isStrength3Potion(first) && isStrength3Potion(second)) {
            event.setResult(createStrength4());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilClick(InventoryClickEvent event) {
        if (event.getInventory().getType() != InventoryType.ANVIL) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getRawSlot() != 2 || event.getCurrentItem() == null) return;

        ItemStack result = event.getCurrentItem();

        // Сила III
        if (isStrength3Potion(result)) {
            event.setCancelled(true);
            event.getInventory().setItem(0, null);
            event.getInventory().setItem(1, null);
            player.getInventory().addItem(createStrength3());
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.5f);
            player.sendMessage("§a§l✓ Зелье §cСилы III §a(45 сек) получено!");
            return;
        }

        // ✅ Сила IV (25с)
        if (isStrength4Potion(result)) {
            event.setCancelled(true);
            event.getInventory().setItem(0, null);
            event.getInventory().setItem(1, null);
            player.getInventory().addItem(createStrength4());
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 2.0f);
            player.sendMessage("§d§l✨ Зелье §cСилы IV §d(25 сек) получено! §c§lУЛЮЧШЕННОЕ!");
        }
    }

    // ✅ СИЛА III (45 сек, amplifier 2)
    public static ItemStack createStrength3() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.addCustomEffect(PotionEffectType.STRENGTH.createEffect(900, 2), true);  // 45с, lvl 3
        meta.setDisplayName("§cЗелье Силы III (45с)");
        potion.setItemMeta(meta);
        return potion;
    }

    // ✅ СИЛА IV (25 сек, amplifier 3) НОВОЕ!
    public static ItemStack createStrength4() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.addCustomEffect(PotionEffectType.STRENGTH.createEffect(500, 3), true);  // 25с, lvl 4
        meta.setDisplayName("§d§lЗелье Силы IV (25с) §c§l★ЭПИК★");
        potion.setItemMeta(meta);
        return potion;
    }

    // ✅ Любой Сила II (amplifier 1)
    private static boolean isStrength2Potion(ItemStack item) {
        if (item == null || item.getType() != Material.POTION) return false;
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        if (meta == null || !meta.hasCustomEffect(PotionEffectType.STRENGTH)) return false;
        return meta.getCustomEffects().stream()
                .anyMatch(e -> e.getType() == PotionEffectType.STRENGTH && e.getAmplifier() == 1);
    }

    // ✅ Сила III (amplifier 2)
    private static boolean isStrength3Potion(ItemStack item) {
        if (item == null || item.getType() != Material.POTION || !item.hasItemMeta()) return false;
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        return meta.hasCustomEffect(PotionEffectType.STRENGTH) &&
                meta.getCustomEffects().stream()
                        .anyMatch(e -> e.getType() == PotionEffectType.STRENGTH && e.getAmplifier() == 2);
    }

    // ✅ Сила IV (amplifier 3) НОВОЕ!
    private static boolean isStrength4Potion(ItemStack item) {
        if (item == null || item.getType() != Material.POTION || !item.hasItemMeta()) return false;
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        return meta.hasCustomEffect(PotionEffectType.STRENGTH) &&
                meta.getCustomEffects().stream()
                        .anyMatch(e -> e.getType() == PotionEffectType.STRENGTH && e.getAmplifier() == 3);
    }
}