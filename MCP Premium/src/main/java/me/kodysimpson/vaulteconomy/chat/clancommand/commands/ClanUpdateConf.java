package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import org.bukkit.configuration.file.FileConfiguration;

public class ClanUpdateConf {

    private final ClanManager clanManager;
    private final FileConfiguration config;

    public ClanUpdateConf(ClanManager clanManager, FileConfiguration config) {
        this.clanManager = clanManager;
        this.config = config;
    }

    /* ===================== СОХРАНЕНИЕ ВСЕХ КЛАНОВ ===================== */

    public void saveAllClans() {

        config.set("clans", null); // очистка перед сохранением

        for (Clan clan : clanManager.getAllClans()) {
            String path = "clans." + clan.getPlainName();

            config.set(path + ".name", clan.getName());
            config.set(path + ".leader", clan.getLeader().toString());
            config.set(path + ".balance", clan.getBalance());
            config.set(path + ".openJoin", clan.isOpenJoin());
            config.set(path + ".pvp", clan.isPvpEnabled());
            config.set(path + ".friendlyFire", clan.isFriendlyFire());
            config.set(path + ".storageLevel", clan.getStorageLevel());

            config.set(path + ".members",
                    clan.getMembers().stream().map(Object::toString).toList());

            if (clan.hasHome()) {
                config.set(path + ".home", clan.getHome());
            }

            if (clan.getBanner() != null) {
                config.set(path + ".banner", clan.getBanner());
            }

            config.set(path + ".wars", clan.getWars());
        }
    }
}