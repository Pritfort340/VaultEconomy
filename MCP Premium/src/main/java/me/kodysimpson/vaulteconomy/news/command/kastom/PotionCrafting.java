package me.kodysimpson.vaulteconomy.news.command.kastom;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionCrafting implements Listener {

    private static final int ANVIL_LEVEL_COST = 10;
    private final NamespacedKey LEVEL_KEY;

    public PotionCrafting(JavaPlugin plugin) {
        this.LEVEL_KEY = new NamespacedKey(plugin, "custom_strength_level");
    }

    /* ===================================================== */
    /* =================== PREPARE CRAFT =================== */
    /* ===================================================== */

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPrepareCraft(PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack[] m = inv.getMatrix();

        if (!matchesRecipe(m)) return;

        // ❗ анти-дюп: в каждом слоте ТОЛЬКО 1 предмет
        for (ItemStack item : m) {
            if (item != null && item.getAmount() != 1) {
                inv.setResult(null);
                return;
            }
        }

        inv.setResult(createStrength3());
    }

    /* ===================================================== */
    /* ===================== CRAFT ITEM ==================== */
    /* ===================================================== */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCraft(CraftItemEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory() instanceof CraftingInventory inv)) return;

        ItemStack[] m = inv.getMatrix();
        if (!matchesRecipe(m)) return;

        event.setCancelled(true);

        // Удаляем ингредиенты вручную
        for (int i = 0; i < m.length; i++) {
            inv.setItem(i, null);
        }

        player.getInventory().addItem(createStrength3());
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.4f);
        player.sendMessage("§a§l✓ Зелье Силы III создано! §7(45 секунд)");
    }

    /* ===================================================== */
    /* =================== PREPARE ANVIL =================== */
    /* ===================================================== */

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {

        AnvilInventory inv = event.getInventory();
        ItemStack left = inv.getItem(0);
        ItemStack right = inv.getItem(1);

        if (!isStrength3(left) || !isStrength3(right)) return;

        inv.setResult(createStrength4());
    }

    /* ===================================================== */
    /* =================== ANVIL CLICK ===================== */
    /* ===================================================== */

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAnvilClick(InventoryClickEvent event) {

        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!(event.getInventory() instanceof AnvilInventory inv)) return;
        if (event.getRawSlot() != 2) return;

        ItemStack result = inv.getItem(2);
        if (!isStrength4(result)) return;

        if (player.getLevel() < ANVIL_LEVEL_COST) {
            player.sendMessage("§c❌ Нужно §e" + ANVIL_LEVEL_COST + " §cуровней!");
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        player.setLevel(player.getLevel() - ANVIL_LEVEL_COST);

        inv.setItem(0, null);
        inv.setItem(1, null);
        inv.setItem(2, null);

        player.getInventory().addItem(createStrength4());
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1f, 1f);
        player.sendMessage("§6§l⚡ Зелье Силы IV создано! §7(25 секунд)");
    }

    /* ===================================================== */
    /* ===================== RECIPE ======================== */
    /* ===================================================== */

    private boolean matchesRecipe(ItemStack[] s) {
        return check(s,0,Material.BLAZE_ROD) &&
                check(s,1,Material.NETHER_STAR) &&
                check(s,2,Material.BLAZE_ROD) &&
                check(s,3,Material.IRON_SWORD) &&
                check(s,4,Material.POTION) &&
                check(s,5,Material.IRON_SWORD) &&
                check(s,6,Material.BLAZE_ROD) &&
                check(s,7,Material.GHAST_TEAR) &&
                check(s,8,Material.BLAZE_ROD);
    }

    private boolean check(ItemStack[] s, int i, Material m) {
        return s[i] != null && s[i].getType() == m;
    }

    /* ===================================================== */
    /* ===================== CHECK ========================= */
    /* ===================================================== */

    private boolean isStrength3(ItemStack item) {
        return hasLevel(item, 3);
    }

    private boolean isStrength4(ItemStack item) {
        return hasLevel(item, 4);
    }

    private boolean hasLevel(ItemStack item, int level) {
        if (item == null || item.getType() != Material.POTION) return false;
        if (!item.hasItemMeta()) return false;

        PotionMeta meta = (PotionMeta) item.getItemMeta();
        Integer stored = meta.getPersistentDataContainer()
                .get(LEVEL_KEY, PersistentDataType.INTEGER);

        return stored != null && stored == level;
    }

    /* ===================================================== */
    /* ===================== POTIONS ======================= */
    /* ===================================================== */

    private ItemStack createStrength3() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        meta.addCustomEffect(new PotionEffect(PotionEffectType.STRENGTH, 900, 2), true);
        meta.setDisplayName("§cЗелье Силы III §7(45с)");
        meta.getPersistentDataContainer().set(LEVEL_KEY, PersistentDataType.INTEGER, 3);

        potion.setItemMeta(meta);
        return potion;
    }

    private ItemStack createStrength4() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();

        meta.addCustomEffect(new PotionEffect(PotionEffectType.STRENGTH, 500, 3), true);
        meta.setDisplayName("§4§lЗелье Силы IV §7(25с)");
        meta.getPersistentDataContainer().set(LEVEL_KEY, PersistentDataType.INTEGER, 4);

        potion.setItemMeta(meta);
        return potion;
    }
}