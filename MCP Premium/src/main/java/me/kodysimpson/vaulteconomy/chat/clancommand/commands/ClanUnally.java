package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanUnally {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    public ClanUnally(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    public void execute(Player player, String targetClanName) {

        /* ===================== PvP BLOCK ===================== */
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя использовать команды клана во время PvP!");
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

        if (member == null || member.getRole() != ClanRole.LEADER) {
            player.sendMessage("§cТолько лидер может разрывать союз.");
            return;
        }

        Clan targetClan = clanManager.getClanByName(targetClanName);
        if (targetClan == null) {
            player.sendMessage("§cКлан не найден.");
            return;
        }

        if (!clan.isAlliedWith(targetClan)) {
            player.sendMessage("§cЭтот клан не является союзником.");
            return;
        }

        /* ===================== REMOVE ALLY ===================== */
        clan.removeAlly(targetClan);
        targetClan.removeAlly(clan);

        player.sendMessage("§aСоюз с кланом §f" + targetClan.getName() + " §aразорван.");
    }
}