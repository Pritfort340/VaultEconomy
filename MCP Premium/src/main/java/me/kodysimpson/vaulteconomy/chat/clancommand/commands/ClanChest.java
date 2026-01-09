package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanStorage;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ClanChest {

    private final ClanManager clanManager;
    private final ClanStorage clanStorage;
    private final PvpManager pvpManager;

    public ClanChest(ClanManager clanManager, ClanStorage clanStorage) {
        this.clanManager = clanManager;
        this.clanStorage = clanStorage;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    public void execute(Player player) {

        // 🔒 PvP BLOCK
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя открывать клановый сундук во время PvP!");
            return;
        }

        // 👥 CLAN CHECK
        if (!clanManager.hasClan(player.getUniqueId())) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cОшибка: клан не найден.");
            return;
        }

        // 📦 STORAGE (БЕЗ АРГУМЕНТОВ)
        Inventory inventory = clanStorage.getInventory();

        if (inventory == null) {
            player.sendMessage("§cКлановое хранилище недоступно.");
            return;
        }

        player.openInventory(inventory);
        player.sendMessage("§aКлановый сундук открыт.");
    }
}