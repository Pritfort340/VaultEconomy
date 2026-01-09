package me.kodysimpson.vaulteconomy;

import me.kodysimpson.vaulteconomy.chat.*;
import me.kodysimpson.vaulteconomy.chat.clancommand.*;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanCommand;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import me.kodysimpson.vaulteconomy.chat.clancommand.commands.*;
import me.kodysimpson.vaulteconomy.chat.clancommand.commands.ClanUpdater;
import me.kodysimpson.vaulteconomy.commands.*;
import me.kodysimpson.vaulteconomy.economy.*;
import me.kodysimpson.vaulteconomy.listeners.*;
import me.kodysimpson.vaulteconomy.modules.fly.*;
import me.kodysimpson.vaulteconomy.modules.gamemode.*;
import me.kodysimpson.vaulteconomy.modules.god.*;
import me.kodysimpson.vaulteconomy.modules.heal.*;
import me.kodysimpson.vaulteconomy.modules.teleport.*;
import me.kodysimpson.vaulteconomy.news.command.*;
import me.kodysimpson.vaulteconomy.news.command.auction.*;
import me.kodysimpson.vaulteconomy.news.command.back.*;
import me.kodysimpson.vaulteconomy.news.command.filter.ChatFilterCommand;
import me.kodysimpson.vaulteconomy.news.command.kastom.*;
import me.kodysimpson.vaulteconomy.pvp.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public final class VaultEconomy extends JavaPlugin {

    private static VaultEconomy instance;

    public static VaultEconomy getInstance() {
        return instance;
    }


    private FileConfiguration pvpConfig;

    private CustomEconomy economy;
    private PointManager pointManager;
    private WarpManager warpManager;
    private PvpManager pvpManager;
    private ClanManager clanManager;

    private PrefixManager prefixManager;
    private IgnoreManager ignoreManager;
    private MarriageManager marriageManager;

    private FlyModule flyModule;
    private HealModule healModule;
    private GodModule godModule;
    private GamemodeModule gamemodeModule;
    private TeleportModule teleportModule;

    private KastomFurnace kastomFurnace;
    private AuctionMain auctionMain;
    private ClanChat clanChat;



    @Override
    public void onEnable() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault не найден! Плагин отключён.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        saveDefaultConfig();
        loadPvpConfig();

        pvpManager = new PvpManager(this);
        clanManager = new ClanManager(pvpManager);

        /* ===== CLAN CHAT ===== */
        clanChat = new ClanChat(clanManager);
        registerListener(clanChat);

        registerListener(new PvpListener(pvpManager));
        registerListener(new PvpQuitListener(pvpManager));
        registerListener(new PvpCommandBlocker(this, pvpManager));

        prefixManager = new PrefixManager(this);
        ignoreManager = new IgnoreManager(this);
        marriageManager = new MarriageManager(this);

        economy = new CustomEconomy(this);
        economy.loadBalances();

        registerListener(new EconomyQuitListener(economy));

        getServer().getServicesManager().register(
                Economy.class,
                economy,
                this,
                ServicePriority.Highest
        );
        getLogger().info("Vault Economy успешно зарегистрирована");


        AuctionMessages.init(this);

        pointManager = new PointManager(this);
        pointManager.load();

        warpManager = new WarpManager(this);

        flyModule = new FlyModule(this);
        registerListener(flyModule);
        command("fly", new FlyCommand(this, flyModule));

        healModule = new HealModule(getConfig(), pvpManager);
        command("heal", new HealCommand(getConfig(), pvpManager));

        godModule = new GodModule(pvpManager);
        command("god", new GodCommand(godModule));

        gamemodeModule = new GamemodeModule();
        command("gamemode", new GamemodeCommand(gamemodeModule));

        teleportModule = new TeleportModule(this, pvpManager);
        registerListener(teleportModule);
        command("tp", new TeleportCommand(teleportModule));
        command("tpa", new TpaCommand(teleportModule));
        command("tpaaccept", new TpaAcceptCommand(teleportModule));
        command("tpadeny", new TpaDenyCommand(teleportModule));
        command("tpahere", new TpahereCommand(teleportModule));

        command("prefix", new PrefixCommand(prefixManager));
        command("clan", new ClanCommand(clanManager, clanChat));


        registerListener(new PotionCrafting(this));
        registerListener(new ChatListener(prefixManager, ignoreManager, marriageManager, clanManager));

        kastomFurnace = new KastomFurnace(getConfig());
        registerListener(kastomFurnace);
        command("kostoms", new KastomFurnaceCommand(kastomFurnace));
        command("kastom", new KastomCommand());

        auctionMain = new AuctionMain(this);
        if (getCommand("ve") != null) getCommand("ve").setExecutor(new VaultEconomyCommand(this));
        command("ah", auctionMain);
        command("ahelp", new HelpAh(this));

        getServer().getPluginManager().registerEvents(
                new ClanStorageListener(),
                this
        );

        registerListener(new BackListener());
        command("back", new BackCommand(this));

        command("bits", new BalanceCommand(this));
        command("baltop", new BaltopCommand(this));
        command("point", new PointCommand(this, pointManager));

        command("spawn", new SpawnCommand(warpManager, pvpManager));
        command("setspawn", new SetSpawnCommand(warpManager));
        command("home", new HomeCommand(warpManager));
        command("sethome", new SetHomeCommand(warpManager, this));
        command("delhome", new DelHomeCommand(warpManager));
        command("homelist", new HomelistCommand(warpManager));
        command("warp", new WarpCommand(warpManager));
        command("setwarp", new SetWarpCommand(warpManager));
        command("delwarp", new DelWarpCommand(warpManager));
        command("warplist", new WarplistCommand(warpManager));

        command("craft", new CraftCommand(this));
        command("anvil", new AnvilCommand());
        command("ec", new EnderChestCommand());
        command("repair", new RepairCommand(this));
        command("filter", new ChatFilterCommand(this));
        command("clearchat", new ClearChatCommand());
        command("feed", new FeedCommand());
        command("ignore", new IgnoreCommand(ignoreManager));
        command("sp", new SpawnMobCommand());

        command("marry", new MarryCommand(this, marriageManager));

        new ClanUpdater(clanManager).runTaskTimer(this, 20L * 60, 20L * 60);
    }

    @Override
    public void onDisable() {
        if (economy != null) economy.saveBalances();
        if (pointManager != null) pointManager.save();
    }

    /* ===================== HELPERS ===================== */

    private void registerListener(org.bukkit.event.Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    private void command(String name, org.bukkit.command.CommandExecutor executor) {
        if (getCommand(name) != null) {
            getCommand(name).setExecutor(executor);
        }
    }

    private void loadPvpConfig() {
        saveResource("pvp.yml", false);
        File file = new File(getDataFolder(), "pvp.yml");
        pvpConfig = YamlConfiguration.loadConfiguration(file);
    }

    /* ===================== GETTERS ===================== */


    public FileConfiguration getPvpConfig() {
        return pvpConfig;
    }

    public PvpManager getPvpManager() {
        return pvpManager;
    }

    public PointManager getPointManager() {
        return pointManager;
    }

    public CustomEconomy getEconomy() {
        return economy;
    }

    public ClanManager getClanManager() {
        return clanManager;
    }

    /* ===================== RELOAD ===================== */

    public void reloadPluginConfig() {
        reloadConfig();
        getLogger().info("VaultEconomy: config.yml перезагружен");
    }

    public void reloadAll() {
        reloadConfig();
        loadPvpConfig();

        if (prefixManager != null) prefixManager.reload();
        if (economy != null) economy.reload();

        if (pointManager != null) {
            pointManager.save();
            pointManager.load();
        }

        AuctionMessages.reload(this);
        getLogger().info("VaultEconomy: полный reload выполнен");
    }
}
