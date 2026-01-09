package me.kodysimpson.vaulteconomy.chat.clancommand;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.chat.clancommand.commands.*;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanCommand implements CommandExecutor {

    private final ClanManager clanManager;
    private final ClanPvp clanPvp;
    private final ClanChat clanChat;
    private final PvpManager pvpManager;

    /* ===== SHARED ===== */
    private final ClanRequests clanRequests;

    /* ===== COMMANDS ===== */
    private final ClanInvite clanInvite;
    private final ClanTransfer clanTransfer;
    private final ClanKick clanKick;
    private final ClanChatCmd clanChatCmd;
    private final ClanRaccept clanRaccept;
    private final ClanRdeny clanRdeny;
    private final ClanPromote clanPromote;
    private final ClanDemote clanDemote;
    private final ClanUpgrade clanUpgrade;
    private final ClanToggleJoin clanToggleJoin;
    private final ClanSetHome clanSetHome;
    private final ClanChange clanChange;
    private final ClanReload clanReload;
    private final ClanDelete clanDelete;
    private final ClanCreate clanCreate;
    private final ClanStorageCommand clanStorageCommand;
    private final ClanDeposit clanDeposit;



    public ClanCommand(ClanManager clanManager, ClanChat clanChat) {
        this.clanManager = clanManager;
        this.clanDeposit = new ClanDeposit(clanManager);
        this.clanCreate = new ClanCreate(clanManager);
        this.clanReload = new ClanReload(VaultEconomy.getInstance());
        this.clanSetHome = new ClanSetHome(clanManager);
        this.clanChat = clanChat;
        this.pvpManager = VaultEconomy.getInstance().getPvpManager();
        this.clanToggleJoin = new ClanToggleJoin(clanManager, pvpManager);
        this.clanStorageCommand = new ClanStorageCommand(clanManager);




        /* ===== SINGLE INSTANCES ===== */
        this.clanDelete = new ClanDelete(clanManager);
        this.clanRequests = new ClanRequests();
        this.clanChange = new ClanChange(clanManager);
        this.clanInvite = new ClanInvite(clanManager, clanRequests);
        this.clanKick = new ClanKick(clanManager);
        this.clanPvp = new ClanPvp(clanManager, pvpManager);
        this.clanChatCmd = new ClanChatCmd(clanManager, clanChat);
        this.clanRaccept = new ClanRaccept(clanManager, clanRequests);
        this.clanRdeny = new ClanRdeny(clanRequests);
        this.clanTransfer = new ClanTransfer(clanManager, pvpManager);
        this.clanPromote = new ClanPromote(clanManager);
        this.clanDemote = new ClanDemote(clanManager);
        this.clanUpgrade = new ClanUpgrade(clanManager);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("§cКоманда доступна только игрокам.");
            return true;
        }

        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя использовать клановые команды во время PvP!");
            return true;
        }

        if (args.length == 0) {
            sendHelp(player);
            return true;
        }

        String sub = args[0].toLowerCase();
        UUID uuid = player.getUniqueId();

        switch (sub) {

            case "reload" -> clanReload.execute(sender);

            case "deposit" -> clanDeposit.execute(player, args);

            case "change" -> {
                if (args.length < 2) {
                    player.sendMessage("§cИспользование: /clan change <новоеИмя>");
                    return true;
                }
                clanChange.execute(player, args[1]);
            }

            case "storage" -> clanStorageCommand.execute(player);



            case "sethome" -> clanSetHome.execute(player);
            case "home" -> new ClanHome(clanManager).execute(player);


            case "delete" -> clanDelete.execute(player);

            case "togglejoin" -> clanToggleJoin.execute(player);


            case "transfer" -> {
                if (args.length < 2) {
                    player.sendMessage("§cИспользование: /clan transfer <ник>");
                    return true;
                }
                clanTransfer.execute(player, args[1]);
            }



            case "pvp" -> clanPvp.execute(player);

            case "help" -> sendHelp(player);

            /* ===== LEAVE ===== */
            case "leave" -> {
                if (!clanManager.hasClan(uuid)) {
                    player.sendMessage("§cТы не состоишь в клане.");
                    return true;
                }

                Clan clan = clanManager.getClan(uuid);
                clanManager.removeMember(clan, uuid);

                player.sendMessage("§cТы покинул клан §f" + clan.getName());
            }

            /* ===== BALANCE ===== */
            case "balance" -> {
                if (!clanManager.hasClan(uuid)) {
                    player.sendMessage("§cТы не состоишь в клане.");
                    return true;
                }

                Clan clan = clanManager.getClan(uuid);
                player.sendMessage("§aБаланс клана: §e" +
                        VaultEconomy.getInstance().getEconomy().format(clan.getBalance()));
            }

            case "create" -> {
                if (args.length < 2) {
                    player.sendMessage("§cИспользование: /clan create <имя>");
                    return true;
                }
                clanCreate.execute(player, args[1]);
            }


            /* ===== CHAT ===== */
            case "chat" -> clanChatCmd.execute(player);

            case "setbanner" -> new ClanSetBanner(clanManager).execute(player);
            case "upgradestorage" -> new ClanUpgradeStorage(clanManager).execute(player);


            /* ===== INVITE ===== */
            case "invite" -> {
                if (args.length < 2) {
                    player.sendMessage("§cИспользование: /clan invite <ник>");
                    return true;
                }
                clanInvite.execute(player, args[1]);
            }

            /* ===== REQUESTS ===== */
            case "raccept" -> clanRaccept.execute(player);
            case "rdeny" -> clanRdeny.execute(player);

            /* ===== KICK ===== */
            case "kick" -> {
                if (args.length < 2) {
                    player.sendMessage("§cИспользование: /clan kick <ник>");
                    return true;
                }
                clanKick.execute(player, args[1]);
            }

            /* ===== PROMOTE ===== */
            case "promote" -> {
                if (args.length < 2) {
                    player.sendMessage("§cИспользование: /clan promote <ник>");
                    return true;
                }
                clanPromote.execute(player, args[1]);
            }

            /* ===== DEMOTE ===== */
            case "demote" -> {
                if (args.length < 2) {
                    player.sendMessage("§cИспользование: /clan demote <ник>");
                    return true;
                }
                clanDemote.execute(player, args[1]);
            }

            /* ===== UPGRADE ===== */
            case "upgrade" -> clanUpgrade.execute(player);

            default -> {
                player.sendMessage("§cНеизвестная подкоманда.");
                sendHelp(player);
            }
        }

        return true;
    }

    private void sendHelp(Player player) {

        UUID uuid = player.getUniqueId();

        player.sendMessage("§6§lКланы:");

        // ===== НЕ В КЛАНЕ =====
        if (!clanManager.hasClan(uuid)) {
            player.sendMessage("§e/clan create <имя> §7- создать клан");
            player.sendMessage("§e/clan help §7- помощь");
            return;
        }

        ClanRole role = clanManager.getRole(uuid);

        // ===== ОБЩИЕ ДЛЯ ВСЕХ =====
        player.sendMessage("§e/clan chat §7- клановый чат");
        player.sendMessage("§e/clan home §7- телепорт в дом клана");
        player.sendMessage("§e/clan balance §7- баланс клана");
        player.sendMessage("§e/clan deposit <сумма> §7- внести деньги в клан");
        player.sendMessage("§e/clan leave §7- покинуть клан");
        player.sendMessage("§e/clan storage §7- хранилище клана");
        player.sendMessage("§e/clan upgradestorage §7— улучшить хранилище клана");


        // ===== ОФИЦЕР И ЛИДЕР =====
        if (role == ClanRole.OFFICER || role == ClanRole.LEADER) {
            player.sendMessage("§e/clan invite <ник> §7- пригласить игрока");
            player.sendMessage("§e/clan kick <ник> §7- исключить игрока");
            player.sendMessage("§e/clan sethome §7- установить дом клана");
            player.sendMessage("§e/clan togglejoin §7- открыть/закрыть вступление");
        }

        // ===== ТОЛЬКО ЛИДЕР =====
        if (role == ClanRole.LEADER) {
            player.sendMessage("§c§lКоманды лидера:");
            player.sendMessage("§e/clan pvp §7- включить пвп между учасниками клана");
            player.sendMessage("§e/clan promote <ник> §7- повысить участника");
            player.sendMessage("§e/clan demote <ник> §7- понизить участника");
            player.sendMessage("§e/clan transfer <ник> §7- передать лидерство");
            player.sendMessage("§e/clan delete §7- удалить клан");
            player.sendMessage("§e/clan change <имя> §7- изменить имя клана");
            player.sendMessage("§e/clan reload §7- перезагрузить конфиг");
        }
    }
}