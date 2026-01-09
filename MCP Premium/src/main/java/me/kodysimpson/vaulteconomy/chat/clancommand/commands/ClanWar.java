package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanWar {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    public ClanWar(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    public void execute(Player player, String targetClanName) {

        UUID uuid = player.getUniqueId();

        /* ================= PvP BLOCK ================= */
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя объявлять войну во время PvP!");
            return;
        }

        /* ================= CLAN CHECK ================= */
        if (!clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(uuid);
        ClanMember member = clanManager.getMember(uuid);

        if (member == null || member.getRole() != ClanRole.LEADER) {
            player.sendMessage("§cТолько лидер может объявлять войну.");
            return;
        }

        /* ================= TARGET CLAN ================= */
        Clan targetClan = clanManager.getClanByName(targetClanName);

        if (targetClan == null) {
            player.sendMessage("§cКлан не найден.");
            return;
        }

        if (targetClan == clan) {
            player.sendMessage("§cНельзя объявить войну своему клану.");
            return;
        }

        if (clan.isAtWarWith(targetClan)) {
            player.sendMessage("§cВы уже находитесь в состоянии войны с этим кланом.");
            return;
        }

        /* ================= WAR START ================= */
        clan.addWar(targetClan);
        targetClan.addWar(clan);

        broadcast(clan, "§cВаш клан объявил войну клану §f" + targetClan.getName());
        broadcast(targetClan, "§cКлан §f" + clan.getName() + " §cобъявил вам войну!");
    }

    /* ================= MESSAGE ================= */

    private void broadcast(Clan clan, String message) {
        for (UUID memberId : clan.getMembers()) {
            Player p = Bukkit.getPlayer(memberId);
            if (p != null) {
                p.sendMessage(message);
            }
        }
    }
}