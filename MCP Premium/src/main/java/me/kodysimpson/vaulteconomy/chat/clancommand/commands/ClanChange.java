package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanMember;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanRole;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanChange {

    private final ClanManager clanManager;

    public ClanChange(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void execute(Player player, String newName) {

        UUID uuid = player.getUniqueId();

        /* ===== Проверка клана ===== */
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

        /* ===== Проверка роли ===== */
        if (member.getRole() != ClanRole.LEADER) {
            player.sendMessage("§cТолько лидер может менять имя клана.");
            return;
        }

        /* ===== Проверка имени ===== */
        if (newName.length() < 3 || newName.length() > 16) {
            player.sendMessage("§cИмя клана должно быть от 3 до 16 символов.");
            return;
        }

        if (!newName.matches("[a-zA-Z0-9_]+")) {
            player.sendMessage("§cИмя клана может содержать только буквы и цифры.");
            return;
        }

        if (clanManager.getClanByName(newName) != null) {
            player.sendMessage("§cКлан с таким именем уже существует.");
            return;
        }

        /* ===== Смена имени ===== */
        String coloredName = ChatColor.translateAlternateColorCodes('&', newName);

        clanManager.deleteClan(clan);

        Clan newClan = new Clan(
                coloredName,
                newName,
                clan.getLeader()
        );

        for (UUID memberId : clan.getMembers()) {
            ClanMember cm = clanManager.getMember(memberId);
            if (cm != null) {
                clanManager.addMember(newClan, cm);
            }
        }

        player.sendMessage("§aИмя клана успешно изменено на §f" + coloredName);
    }
}