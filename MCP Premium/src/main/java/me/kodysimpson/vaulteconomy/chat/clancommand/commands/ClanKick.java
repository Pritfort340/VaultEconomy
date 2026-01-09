package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanKick {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    public ClanKick(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    public void execute(Player sender, String targetName) {

        /* ===================== PvP BLOCK ===================== */
        if (pvpManager != null && pvpManager.isInPvp(sender)) {
            sender.sendMessage("§cНельзя исключать игроков из клана во время PvP!");
            return;
        }

        UUID senderId = sender.getUniqueId();

        /* ===================== CLAN CHECK ===================== */
        if (!clanManager.hasClan(senderId)) {
            sender.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(senderId);

        ClanMember senderMember = clanManager.getMember(senderId);
        if (senderMember == null) {
            sender.sendMessage("§cОшибка данных клана.");
            return;
        }

        /* ===================== PERMISSIONS ===================== */
        if (!senderMember.isOfficer()) {
            sender.sendMessage("§cУ тебя нет прав исключать игроков из клана.");
            return;
        }

        /* ===================== TARGET ===================== */
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
            sender.sendMessage("§cТы не можешь исключить самого себя.");
            return;
        }

        ClanMember targetMember = clanManager.getMember(targetId);
        if (targetMember == null) {
            sender.sendMessage("§cОшибка данных участника.");
            return;
        }

        if (targetMember.getRole() == ClanRole.LEADER) {
            sender.sendMessage("§cНельзя исключить лидера клана.");
            return;
        }

        /* ===================== KICK ===================== */
        clanManager.removeMember(clan, targetId);

        sender.sendMessage("§aИгрок §f" + target.getName() + " §aисключён из клана.");
        target.sendMessage("§cТы был исключён из клана §f" + clan.getName());
    }
}