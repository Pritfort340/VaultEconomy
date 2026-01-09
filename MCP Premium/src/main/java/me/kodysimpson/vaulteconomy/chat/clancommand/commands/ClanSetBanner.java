package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class ClanSetBanner {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    public ClanSetBanner(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    public void execute(Player player) {

        UUID uuid = player.getUniqueId();

        /* ================= PvP BLOCK ================= */
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя менять баннер клана во время PvP!");
            return;
        }

        /* ================= CLAN CHECK ================= */
        if (!clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(uuid);
        ClanMember member = clanManager.getMember(uuid);

        if (member == null) {
            player.sendMessage("§cОшибка данных участника.");
            return;
        }

        /* ================= PERMISSIONS ================= */
        if (member.getRole() == ClanRole.MEMBER) {
            player.sendMessage("§cТолько лидер или офицер может менять баннер клана.");
            return;
        }

        /* ================= ITEM CHECK ================= */
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || item.getType() == Material.AIR) {
            player.sendMessage("§cВозьми баннер в руку.");
            return;
        }

        if (!item.getType().name().endsWith("_BANNER")) {
            player.sendMessage("§cВ руке должен быть баннер.");
            return;
        }

        /* ================= SET BANNER ================= */
        clan.setBanner(item.clone());

        player.sendMessage("§aБаннер клана успешно установлен.");
    }
}