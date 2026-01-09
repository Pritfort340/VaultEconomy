package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanMember;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanRole;
import org.bukkit.entity.Player;

public class ClanUpgrade {

    private final ClanManager clanManager;

    public ClanUpgrade(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void execute(Player player) {

        if (!clanManager.hasClan(player.getUniqueId())) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        ClanMember member = clanManager.getMember(player.getUniqueId());
        if (member == null) {
            player.sendMessage("§cОшибка данных клана.");
            return;
        }

        if (member.getRole() != ClanRole.LEADER) {
            player.sendMessage("§cТолько лидер клана может улучшать клан.");
            return;
        }

        Clan clan = clanManager.getClan(player.getUniqueId());

        /* ===== ЗАГЛУШКА УЛУЧШЕНИЯ ===== */
        player.sendMessage("§6Улучшения кланов пока не настроены.");
        player.sendMessage("§7Баланс клана: §e" + clan.getBalance());
        player.sendMessage("§7(Система уровней будет добавлена позже)");
    }
}