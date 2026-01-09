package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanToggleJoin {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    public ClanToggleJoin(ClanManager clanManager, PvpManager pvpManager) {
        this.clanManager = clanManager;
        this.pvpManager = pvpManager;
    }

    public void execute(Player player) {

        UUID uuid = player.getUniqueId();

        /* ===== CLAN CHECK ===== */
        if (!clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя менять настройки клана во время PvP!");
            return;
        }

        Clan clan = clanManager.getClan(uuid);
        ClanMember member = clanManager.getMember(uuid);

        if (member == null) {
            player.sendMessage("§cОшибка данных участника.");
            return;
        }

        /* ===== PERMISSIONS ===== */
        if (!member.isOfficer()) {
            player.sendMessage("§cТолько лидер или офицер может менять настройки клана.");
            return;
        }

        /* ===== TOGGLE ===== */
        boolean newState = !clan.isOpenJoin();
        clan.setOpenJoin(newState);

        if (newState) {
            player.sendMessage("§aКлан теперь §2ОТКРЫТ §aдля вступления.");
        } else {
            player.sendMessage("§cКлан теперь §4ЗАКРЫТ §cдля вступления.");
        }
    }
}