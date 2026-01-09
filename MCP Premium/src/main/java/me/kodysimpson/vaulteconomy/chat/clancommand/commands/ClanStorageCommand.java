package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanStorageCommand {

    private final ClanManager clanManager;

    public ClanStorageCommand(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void execute(Player player) {

        UUID uuid = player.getUniqueId();

        if (!clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(uuid);

        ClanStorage storage = new ClanStorage(
                clan,
                VaultEconomy.getInstance().getPvpManager()
        );

        storage.open(player);
    }
}