package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanRaccept {

    private final ClanManager clanManager;
    private final ClanRequests clanRequests;

    public ClanRaccept(ClanManager clanManager, ClanRequests clanRequests) {
        this.clanManager = clanManager;
        this.clanRequests = clanRequests;
    }

    public void execute(Player player) {

        UUID uuid = player.getUniqueId();

        if (clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы уже состоишь в клане.");
            return;
        }

        if (!clanRequests.hasInvite(player)) {
            player.sendMessage("§cУ тебя нет приглашений в клан.");
            return;
        }

        Clan clan = clanRequests.accept(player);

        clanManager.addMember(
                clan,
                new ClanMember(uuid, ClanRole.MEMBER)
        );

        player.sendMessage("§aТы вступил в клан §f" + clan.getName());
    }
}