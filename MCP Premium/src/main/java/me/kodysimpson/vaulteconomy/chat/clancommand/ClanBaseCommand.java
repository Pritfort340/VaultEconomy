package me.kodysimpson.vaulteconomy.chat.clancommand;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClanBaseCommand implements CommandExecutor {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    public ClanBaseCommand(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда доступна только игрокам.");
            return true;
        }

        /* ===================== PvP BLOCK ===================== */
        if (pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя использовать клановые команды во время PvP!");
            return true;
        }

        /* ===================== BASE ===================== */
        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String sub = args[0].toLowerCase();

        switch (sub) {

            case "create" -> {
                player.sendMessage("§7/clan create <name>");
                // дальше подключим ClanCreateCommand
            }

            case "leave" -> {
                player.sendMessage("§7/clan leave");
            }

            case "chat" -> {
                player.sendMessage("§7/clan chat");
            }

            case "balance" -> {
                Clan clan = clanManager.getClan(player.getUniqueId());
                if (clan == null) {
                    player.sendMessage("§cТы не состоишь в клане.");
                    return true;
                }

                player.sendMessage("§aБаланс клана: §e" + clan.getBalance());
            }

            default -> {
                player.sendMessage("§cНеизвестная подкоманда.");
                sendHelp(player);
            }
        }

        return true;
    }

    /* ===================== HELP ===================== */

    private void sendHelp(Player player) {
        player.sendMessage("§6§lКланы:");
        player.sendMessage("§e/clan create <name> §7- создать клан");
        player.sendMessage("§e/clan leave §7- покинуть клан");
        player.sendMessage("§e/clan chat §7- клановый чат");
        player.sendMessage("§e/clan balance §7- баланс клана");
    }
}
