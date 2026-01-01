package me.kodysimpson.vaulteconomy.news.command.kastom;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.FurnaceStartSmeltEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.NamespacedKey;

public class KastomFurnace implements Listener {
    private final FileConfiguration config;
    private static final NamespacedKey FURNACE_LEVEL_KEY = new NamespacedKey("vaulteconomy", "furnace_level");

    public KastomFurnace(FileConfiguration config) {
        this.config = config;
    }

    public ItemStack createFurnace(int level) {
        ItemStack furnace = new ItemStack(Material.FURNACE);
        ItemMeta meta = furnace.getItemMeta();
        String name = switch (level) {
            case 1 -> "§eПечка Ур.1 (x0.5)";
            case 2 -> "§6Печка Ур.2 (x1.0)";
            case 3 -> "§cПечка Ур.3 (x3.0 + x2!)";
            default -> "§7Печка";
        };
        meta.setDisplayName(name);
        meta.getPersistentDataContainer().set(FURNACE_LEVEL_KEY, PersistentDataType.INTEGER, level);
        furnace.setItemMeta(meta);
        return furnace;
    }

    @EventHandler
    public void onFurnaceStartSmelt(FurnaceStartSmeltEvent event) {
        Furnace furnace = getFurnaceLevel(event.getBlock().getState());
        if (furnace == null) return;

        int level = furnace.getPersistentDataContainer().get(FURNACE_LEVEL_KEY, PersistentDataType.INTEGER);
        int totalCookTime = event.getTotalCookTime();

        switch (level) {
            case 1 -> event.setTotalCookTime(totalCookTime * 2);
            case 3 -> event.setTotalCookTime(Math.max(1, totalCookTime / 3));
        }
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        Furnace furnace = getFurnaceLevel(event.getBlock().getState());
        if (furnace == null) return;

        int level = furnace.getPersistentDataContainer().get(FURNACE_LEVEL_KEY, PersistentDataType.INTEGER);
        int burnTime = event.getBurnTime();

        switch (level) {
            case 1 -> event.setBurnTime((short) Math.max(1, burnTime / 2));
            case 3 -> event.setBurnTime((short) Math.min(32767, burnTime * 3));
        }
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        Furnace furnace = getFurnaceLevel(event.getBlock().getState());
        if (furnace == null || furnace.getPersistentDataContainer().get(FURNACE_LEVEL_KEY, PersistentDataType.INTEGER) != 3)
            return;

        ItemStack result = event.getResult();
        if (result != null) {
            result.setAmount((short) Math.min(64, result.getAmount() * 2));
            event.setResult(result);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.FURNACE) return;

        Furnace furnace = getFurnaceLevel(event.getBlock().getState());
        if (furnace == null) return;

        int level = furnace.getPersistentDataContainer().get(FURNACE_LEVEL_KEY, PersistentDataType.INTEGER);
        if (level > 0 && event.getPlayer().hasPermission("vaulteconomy.kastom")) {
            event.setDropItems(false);
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), createFurnace(level));
        }
    }

    private Furnace getFurnaceLevel(BlockState state) {
        if (!(state instanceof Furnace furnace)) return null;
        if (furnace.getPersistentDataContainer().has(FURNACE_LEVEL_KEY, PersistentDataType.INTEGER))
            return furnace;
        return null;
    }
}