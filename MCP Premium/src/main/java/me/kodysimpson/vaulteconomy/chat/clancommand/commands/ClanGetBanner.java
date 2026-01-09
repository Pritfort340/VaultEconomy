package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ClanGetBanner {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    public ClanGetBanner(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    public void execute(Player player) {

        /* ===================== PvP BLOCK ===================== */
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя получать клановый флаг во время PvP!");
            return;
        }

        /* ===================== CLAN CHECK ===================== */
        if (!clanManager.hasClan(player.getUniqueId())) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cОшибка: клан не найден.");
            return;
        }

        /* ===================== BANNER ===================== */
        ItemStack banner = clan.getBanner();

        if (banner == null) {
            player.sendMessage("§cУ клана не установлен флаг.");
            return;
        }

        /* ===================== GIVE ITEM ===================== */
        player.getInventory().addItem(banner.clone());
        player.sendMessage("§aТы получил клановый флаг.");
    }
}