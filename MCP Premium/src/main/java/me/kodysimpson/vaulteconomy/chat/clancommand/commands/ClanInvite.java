package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClanInvite {

    private final ClanManager clanManager;
    private final ClanRequests clanRequests;
    private final PvpManager pvpManager;

    public ClanInvite(ClanManager clanManager, ClanRequests clanRequests) {
        this.clanManager = clanManager;
        this.clanRequests = clanRequests;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    /* ===================== INVITE ===================== */

    public void execute(Player sender, String targetName) {

        if (pvpManager != null && pvpManager.isInPvp(sender)) {
            sender.sendMessage("§cНельзя приглашать в клан во время PvP!");
            return;
        }

        if (!clanManager.hasClan(sender.getUniqueId())) {
            sender.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(sender.getUniqueId());

        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage("§cИгрок не найден.");
            return;
        }

        if (clanManager.hasClan(target.getUniqueId())) {
            sender.sendMessage("§cЭтот игрок уже состоит в клане.");
            return;
        }

        if (clanRequests.hasInvite(target)) {
            sender.sendMessage("§cУ этого игрока уже есть приглашение.");
            return;
        }

        clanRequests.invite(target, clan);

        sender.sendMessage("§aТы пригласил игрока §f" + target.getName() + " §aв клан.");
        target.sendMessage("§6Ты получил приглашение в клан §f" + clan.getName());
        target.sendMessage("§eИспользуй §a/clan raccept §eили §c/clan rdeny");
    }
}