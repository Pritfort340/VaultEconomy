package me.kodysimpson.vaulteconomy.news.command.kastom;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class KastomFurnace implements Listener {

    private final FileConfiguration config;
    private static final NamespacedKey FURNACE_LEVEL_KEY =
            new NamespacedKey("vaulteconomy", "furnace_level");

    public KastomFurnace(FileConfiguration config) {
        this.config = config;
    }

    /* =========================
       СОЗДАНИЕ ПЕЧКИ
       ========================= */
    public ItemStack createFurnace(int level) {
        ItemStack furnace = new ItemStack(Material.FURNACE);
        ItemMeta meta = furnace.getItemMeta();

        String name = switch (level) {
            case 1 -> "§eПечка Ур.1 §7(x0.5)";
            case 2 -> "§6Печка Ур.2 §7(x1.0)";
            case 3 -> "§cПечка Ур.3 §7(x3 + x2)";
            default -> "§7Печка";
        };

        meta.setDisplayName(name);
        meta.getPersistentDataContainer()
                .set(FURNACE_LEVEL_KEY, PersistentDataType.INTEGER, level);
        furnace.setItemMeta(meta);
        return furnace;
    }

    /* =========================
       УСТАНОВКА ПЕЧКИ
       ========================= */
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() != Material.FURNACE) return;

        ItemStack item = event.getItemInHand();
        if (!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer itemPdc = meta.getPersistentDataContainer();

        if (!itemPdc.has(FURNACE_LEVEL_KEY, PersistentDataType.INTEGER)) return;

        int level = itemPdc.get(FURNACE_LEVEL_KEY, PersistentDataType.INTEGER);

        BlockState state = event.getBlockPlaced().getState();
        if (!(state instanceof Furnace furnace)) return;

        furnace.getPersistentDataContainer()
                .set(FURNACE_LEVEL_KEY, PersistentDataType.INTEGER, level);
        furnace.update();
    }

    /* =========================
       СКОРОСТЬ ПЛАВКИ
       ========================= */
    @EventHandler
    public void onFurnaceStartSmelt(FurnaceStartSmeltEvent event) {
        Furnace furnace = getCustomFurnace(event.getBlock().getState());
        if (furnace == null) return;

        int level = getLevel(furnace);
        int time = event.getTotalCookTime();

        if (level == 1) event.setTotalCookTime(time * 2);       // медленно
        if (level == 3) event.setTotalCookTime(Math.max(1, time / 3)); // быстро
    }

    /* =========================
       ТОПЛИВО
       ========================= */
    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        Furnace furnace = getCustomFurnace(event.getBlock().getState());
        if (furnace == null) return;

        int level = getLevel(furnace);
        int burn = event.getBurnTime();

        if (level == 1) event.setBurnTime((short) Math.max(1, burn / 2));
        if (level == 3) event.setBurnTime((short) Math.min(32767, burn * 3));
    }

    /* =========================
       ДВОЙНОЙ ДРОП
       ========================= */
    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        Furnace furnace = getCustomFurnace(event.getBlock().getState());
        if (furnace == null) return;

        if (getLevel(furnace) != 3) return;

        ItemStack result = event.getResult();
        if (result != null) {
            result.setAmount(Math.min(64, result.getAmount() * 2));
            event.setResult(result);
        }
    }

    /* =========================
       ЛОМАНИЕ
       ========================= */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.FURNACE) return;

        Furnace furnace = getCustomFurnace(event.getBlock().getState());
        if (furnace == null) return;

        int level = getLevel(furnace);
        if (level <= 0) return;

        event.setDropItems(false);
        event.getBlock().getWorld()
                .dropItemNaturally(event.getBlock().getLocation(),
                        createFurnace(level));
    }

    /* =========================
       UTILS
       ========================= */
    private Furnace getCustomFurnace(BlockState state) {
        if (!(state instanceof Furnace furnace)) return null;
        if (!furnace.getPersistentDataContainer()
                .has(FURNACE_LEVEL_KEY, PersistentDataType.INTEGER)) return null;
        return furnace;
    }

    private int getLevel(Furnace furnace) {
        return furnace.getPersistentDataContainer()
                .getOrDefault(FURNACE_LEVEL_KEY,
                        PersistentDataType.INTEGER, 0);
    }
}