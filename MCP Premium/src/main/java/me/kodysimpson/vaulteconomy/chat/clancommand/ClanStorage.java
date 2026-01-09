package me.kodysimpson.vaulteconomy.chat.clancommand;

import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ClanStorage implements InventoryHolder {

    private final Clan clan;
    private final Inventory inventory;
    private final PvpManager pvpManager;

    public ClanStorage(Clan clan, PvpManager pvpManager) {
        this.clan = clan;
        this.pvpManager = pvpManager;

        inventory = Bukkit.createInventory(
                this,
                clan.getStorageSize(),
                "§8Хранилище клана §7" + clan.getPlainName()
        );

        // загружаем предметы
        if (clan.getStorageContents() != null) {
            inventory.setContents(clan.getStorageContents());
        }
    }

    public void open(Player player) {
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя открывать хранилище во время PvP!");
            return;
        }
        player.openInventory(inventory);
    }

    public void save() {
        clan.setStorageContents(inventory.getContents());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public Clan getClan() {
        return clan;
    }
}