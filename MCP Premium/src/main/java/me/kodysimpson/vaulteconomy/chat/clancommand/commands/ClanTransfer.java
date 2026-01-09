package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanTransfer {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    public ClanTransfer(ClanManager clanManager, PvpManager pvpManager) {
        this.clanManager = clanManager;
        this.pvpManager = pvpManager;
    }

    public void execute(Player sender, String targetName) {

        UUID senderId = sender.getUniqueId();

        /* ===== CLAN CHECK ===== */
        if (!clanManager.hasClan(senderId)) {
            sender.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        if (pvpManager != null && pvpManager.isInPvp(sender)) {
            sender.sendMessage("§cНельзя передавать клан во время PvP!");
            return;
        }

        Clan clan = clanManager.getClan(senderId);
        ClanMember senderMember = clanManager.getMember(senderId);

        if (senderMember == null || !senderMember.isLeader()) {
            sender.sendMessage("§cТолько лидер может передать клан.");
            return;
        }

        /* ===== TARGET ===== */
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден.");
            return;
        }

        UUID targetId = target.getUniqueId();

        if (!clan.isMember(targetId)) {
            sender.sendMessage("§cЭтот игрок не состоит в твоём клане.");
            return;
        }

        if (senderId.equals(targetId)) {
            sender.sendMessage("§cНельзя передать клан самому себе.");
            return;
        }

        ClanMember targetMember = clanManager.getMember(targetId);
        if (targetMember == null) {
            sender.sendMessage("§cОшибка данных участника.");
            return;
        }

        /* ===== TRANSFER ===== */
        senderMember.setRole(ClanRole.OFFICER);
        targetMember.setRole(ClanRole.LEADER);
        clan.setLeader(targetId);

        sender.sendMessage("§aТы передал лидерство игроку §f" + target.getName());
        target.sendMessage("§6Ты стал лидером клана §f" + clan.getName());
    }
}