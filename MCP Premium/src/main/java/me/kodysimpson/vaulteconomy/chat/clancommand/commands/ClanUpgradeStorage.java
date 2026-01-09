package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanUpgradeStorage {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    private static final int MAX_LEVEL = 5;

    public ClanUpgradeStorage(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    public void execute(Player player) {

        UUID uuid = player.getUniqueId();

        /* ================= PvP BLOCK ================= */
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя улучшать клан во время PvP!");
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
        if (member.getRole() != ClanRole.LEADER) {
            player.sendMessage("§cТолько лидер может улучшать хранилище клана.");
            return;
        }

        /* ================= LEVEL ================= */
        int currentLevel = clan.getStorageLevel();

        if (currentLevel >= MAX_LEVEL) {
            player.sendMessage("§aХранилище клана уже достигло §eМАКСИМАЛЬНОГО §aуровня!");
            return;
        }

        int nextLevel = currentLevel + 1;
        double price = getPriceForLevel(nextLevel);

        /* ================= ECONOMY ================= */
        if (!clan.removeBalance(price)) {
            player.sendMessage("§cНедостаточно средств в банке клана.");
            player.sendMessage("§7Для уровня §e" + nextLevel +
                    " §7нужно: §6" +
                    VaultEconomy.getInstance().getEconomy().format(price));
            return;
        }

        /* ================= UPGRADE ================= */
        clan.upgradeStorage();

        player.sendMessage("§aХранилище клана улучшено до §e" +
                clan.getStorageLevel() + " §aуровня!");

        if (clan.getStorageLevel() < MAX_LEVEL) {
            double nextPrice = getPriceForLevel(clan.getStorageLevel() + 1);
            player.sendMessage("§7Цена следующего уровня: §6" +
                    VaultEconomy.getInstance().getEconomy().format(nextPrice));
        } else {
            player.sendMessage("§6Вы достигли максимального уровня хранилища!");
        }
    }

    /* ================= PRICE LOGIC ================= */

    private double getPriceForLevel(int level) {
        switch (level) {
            case 2: return 200_000;
            case 3: return 600_000;
            case 4: return 1_500_000;
            case 5: return 3_000_000;
            default: return 6_000_000;
        }
    }
}