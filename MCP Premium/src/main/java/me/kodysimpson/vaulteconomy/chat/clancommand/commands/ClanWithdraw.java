package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanWithdraw {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;
    private final Economy economy;

    public ClanWithdraw(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
        this.economy = VaultEconomy.getInstance().getEconomy();
    }

    public void execute(Player player, String amountArg) {

        /* ===================== PvP BLOCK ===================== */
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя снимать деньги клана во время PvP!");
            return;
        }

        UUID uuid = player.getUniqueId();

        /* ===================== CLAN CHECK ===================== */
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

        /* ===================== PERMISSIONS ===================== */
        if (!member.isOfficer()) {
            player.sendMessage("§cТолько лидер или офицер может снимать деньги клана.");
            return;
        }

        /* ===================== AMOUNT ===================== */
        double amount;
        try {
            amount = Double.parseDouble(amountArg);
        } catch (NumberFormatException e) {
            player.sendMessage("§cВведите корректную сумму.");
            return;
        }

        if (amount <= 0) {
            player.sendMessage("§cСумма должна быть больше 0.");
            return;
        }

        /* ===================== BALANCE CHECK ===================== */
        if (clan.getBalance() < amount) {
            player.sendMessage("§cНедостаточно средств в банке клана.");
            return;
        }

        /* ===================== WITHDRAW ===================== */
        clan.removeBalance(amount);
        economy.depositPlayer(player, amount);

        player.sendMessage("§aТы снял §e" + economy.format(amount) +
                " §aиз банка клана §f" + clan.getName());

    }
}