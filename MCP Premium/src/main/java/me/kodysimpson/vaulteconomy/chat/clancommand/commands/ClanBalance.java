package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.economy.CustomEconomy;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanBalance {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;
    private final CustomEconomy economy;

    public ClanBalance(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
        this.economy = VaultEconomy.getInstance().getEconomy();
    }

    public void execute(Player player) {

        /* ===================== PvP BLOCK ===================== */
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя смотреть баланс клана во время PvP!");
            return;
        }

        /* ===================== CLAN CHECK ===================== */
        UUID uuid = player.getUniqueId();

        if (!clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(uuid);
        if (clan == null) {
            player.sendMessage("§cОшибка: клан не найден.");
            return;
        }

        /* ===================== BALANCE ===================== */
        double balance = clan.getBalance();

        player.sendMessage("§7Баланс клана §a" + clan.getName() + "§7:");
        player.sendMessage("§e" + economy.format(balance));
    }
}