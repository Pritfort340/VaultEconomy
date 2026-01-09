package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import org.bukkit.entity.Player;

public class ClanRdeny {

    private final ClanRequests clanRequests;

    public ClanRdeny(ClanRequests clanRequests) {
        this.clanRequests = clanRequests;
    }

    public void execute(Player player) {

        if (!clanRequests.hasInvite(player)) {
            player.sendMessage("§cУ тебя нет приглашений в клан.");
            return;
        }

        Clan clan = clanRequests.deny(player);
        player.sendMessage("§cТы отклонил приглашение в клан §f" + clan.getName());
    }
}