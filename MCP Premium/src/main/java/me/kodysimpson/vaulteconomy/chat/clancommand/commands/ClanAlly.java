package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanAlly {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    public ClanAlly(ClanManager clanManager) {
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
            player.sendMessage("§cТолько лидер может заключать союзы.");
            return;
        }

        Clan targetClan = clanManager.getClanByName(targetClanName);
        if (targetClan == null) {
            player.sendMessage("§cКлан не найден.");
            return;
        }

        if (targetClan == clan) {
            player.sendMessage("§cНельзя заключить союз с самим собой.");
            return;
        }

        /* ===================== WAR CHECK ===================== */
        if (clan.isAtWarWith(targetClan)) {
            player.sendMessage("§cНельзя заключить союз с кланом, с которым идёт война.");
            return;
        }

        /* ===================== ALLY ===================== */
        clan.addWar(targetClan);        // используем wars как relations
        targetClan.addWar(clan);

        player.sendMessage("§aКлан §f" + targetClan.getName() + " §aтеперь ваш союзник.");
    }
}