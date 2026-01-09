package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanChat;
import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanChatCmd {

    private final ClanManager clanManager;
    private final ClanChat clanChat;
    private final PvpManager pvpManager;

    public ClanChatCmd(ClanManager clanManager, ClanChat clanChat) {
        this.clanManager = clanManager;
        this.clanChat = clanChat;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    public void execute(Player player) {

        /* ===================== PvP BLOCK ===================== */
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя использовать клан-чат во время PvP!");
            return;
        }

        /* ===================== CLAN CHECK ===================== */
        UUID uuid = player.getUniqueId();

        if (!clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы не состоишь в клане.");
            return;
        }

        Clan clan = clanManager.getClan(uuid);
        if (clan == null) {
            player.sendMessage("§cОшибка: клан не найден.");
            return;
        }

        /* ===================== TOGGLE CHAT ===================== */
        boolean enabled = clanChat.toggle(uuid);

        if (enabled) {
            player.sendMessage("§aКлановый чат включён.");
        } else {
            player.sendMessage("§cКлановый чат выключен.");
        }


    }
}