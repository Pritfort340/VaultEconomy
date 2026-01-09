package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanMember;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanRole;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanDemote {

    private final ClanManager clanManager;

    public ClanDemote(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void execute(Player sender, String targetName) {

        UUID senderId = sender.getUniqueId();

        /* ===== CLAN CHECK ===== */
        if (!clanManager.hasClan(senderId)) {
            sender.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(senderId);

        ClanMember senderMember = clanManager.getMember(senderId);
        if (senderMember == null || senderMember.getRole() != ClanRole.LEADER) {
            sender.sendMessage("§cТолько лидер клана может понижать участников.");
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

        ClanMember targetMember = clanManager.getMember(targetId);
        if (targetMember == null) {
            sender.sendMessage("§cОшибка данных участника.");
            return;
        }

        /* ===== DEMOTE LOGIC ===== */
        if (targetMember.getRole() == ClanRole.LEADER) {
            sender.sendMessage("§cНельзя понизить лидера.");
            return;
        }

        if (targetMember.getRole() == ClanRole.MEMBER) {
            sender.sendMessage("§cЭтот игрок уже обычный участник.");
            return;
        }

        targetMember.setRole(ClanRole.MEMBER);

        sender.sendMessage("§aТы понизил игрока §f" + target.getName() + " §aдо участника.");
        target.sendMessage("§cТебя понизили до §fУЧАСТНИКА §cв клане §f" + clan.getName());
    }
}