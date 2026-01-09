package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.economy.CustomEconomy;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;

public class ClanDeposit {

    private final ClanManager clanManager;
    private final CustomEconomy economy;
    private final PvpManager pvpManager;

    public ClanDeposit(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.economy = VaultEconomy.getInstance().getEconomy();
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    public void execute(Player player, String[] args) {

        /* ===================== PvP BLOCK ===================== */
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя вносить деньги в клан во время PvP!");
            return;
        }

        /* ===================== CLAN CHECK ===================== */
        if (!clanManager.hasClan(player.getUniqueId())) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        /* ===================== ARGS ===================== */
        if (args.length < 2) {
            player.sendMessage("§cИспользование: /clan deposit <amount>");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("§cВведите корректное число.");
            return;
        }

        if (amount <= 0) {
            player.sendMessage("§cСумма должна быть больше нуля.");
            return;
        }

        /* ===================== BALANCE CHECK ===================== */
        if (!economy.has(player, amount)) {
            player.sendMessage("§cУ тебя недостаточно средств.");
            return;
        }

        Clan clan = clanManager.getClan(player.getUniqueId());
        if (clan == null) {
            player.sendMessage("§cОшибка: клан не найден.");
            return;
        }

        /* ===================== TRANSACTION ===================== */
        economy.withdrawPlayer(player, amount);
        clan.addBalance(amount);

        player.sendMessage("§aТы внес §e" + economy.format(amount)
                + " §aна баланс клана §6" + clan.getName());
    }
}