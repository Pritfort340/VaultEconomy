package me.kodysimpson.vaulteconomy;

import me.kodysimpson.vaulteconomy.news.command.auction.HelpAh;
import me.kodysimpson.vaulteconomy.news.command.auction.AuctionMain;
import me.kodysimpson.vaulteconomy.news.command.filter.ChatFilterCommand;
import org.bukkit.plugin.java.JavaPlugin;
import me.kodysimpson.vaulteconomy.news.command.kastom.KastomFurnace;
import me.kodysimpson.vaulteconomy.news.command.kastom.KastomPotion;
import me.kodysimpson.vaulteconomy.news.command.kastom.KastomCommand;
import me.kodysimpson.vaulteconomy.news.command.kastom.PotionCrafting; // ✅ ДОБАВЬТЕ ИМПОРТ
import me.kodysimpson.vaulteconomy.chat.IgnoreManager;
import me.kodysimpson.vaulteconomy.chat.PrefixManager;
import me.kodysimpson.vaulteconomy.commands.*;
import me.kodysimpson.vaulteconomy.economy.CustomEconomy;
import me.kodysimpson.vaulteconomy.economy.PointCommand;
import me.kodysimpson.vaulteconomy.economy.PointManager;
import me.kodysimpson.vaulteconomy.listeners.ChatListener;
import me.kodysimpson.vaulteconomy.modules.fly.FlyCommand;
import me.kodysimpson.vaulteconomy.modules.fly.FlyModule;
import me.kodysimpson.vaulteconomy.modules.gamemode.GamemodeCommand;
import me.kodysimpson.vaulteconomy.modules.gamemode.GamemodeModule;
import me.kodysimpson.vaulteconomy.modules.god.GodCommand;
import me.kodysimpson.vaulteconomy.modules.god.GodModule;
import me.kodysimpson.vaulteconomy.modules.heal.HealCommand;
import me.kodysimpson.vaulteconomy.modules.heal.HealModule;
import me.kodysimpson.vaulteconomy.modules.teleport.TeleportModule;
import me.kodysimpson.vaulteconomy.modules.teleport.WarpManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;

public class VaultEconomy extends JavaPlugin {

    private static VaultEconomy instance;  // Синглтон для плагина

    private CustomEconomy economy;
    private PointManager pointManager;
    private PrefixManager prefixManager;
    private IgnoreManager ignoreManager;
    private WarpManager warpManager;
    private KastomFurnace kastomFurnace;
    private KastomPotion kastomPotion;
    private AuctionMain auctionMain;


    private HealModule healModule;
    private TeleportModule teleportModule;
    private GodModule godModule;
    private FlyModule flyModule;
    private GamemodeModule gamemodeModule;



    @Override
    public void onEnable() {

        // Загружаем конфиг по умолчанию, если его нет
        saveDefaultConfig();

        // Сохраняем текущий экземпляр плагина для синглтона
        instance = this;

        // В onEnable() добавьте:
        getServer().getPluginManager().registerEvents(new PotionCrafting(), this);

        // Инициализируем экономику
        economy = new CustomEconomy(this);
        economy.loadBalances();

        // Регистрируем экономику в системе Vault
        getServer().getServicesManager().register(
                Economy.class,
                economy,
                this,
                ServicePriority.Highest
        );

        // Инициализируем менеджер поинтов (мультивалюта) и загружаем данные
        pointManager = new PointManager(this);
        pointManager.load();

        // Ignore manager
        ignoreManager = new IgnoreManager();

        // Инициализируем систему префиксов и чат
        prefixManager = new PrefixManager(this);
        getServer().getPluginManager().registerEvents(
                new ChatListener(prefixManager, ignoreManager),
                this
        );

        // Инициализируем Warp систему
        warpManager = new WarpManager(this);

        // Регистрируем команды экономики
        if (getCommand("bits") != null) {
            getCommand("bits").setExecutor(new BalanceCommand(this));  // /bits
        }
        if (getCommand("point") != null) {
            getCommand("point").setExecutor(new PointCommand(this, pointManager)); // /point ...
        }
        if (getCommand("baltop") != null) {
            getCommand("baltop").setExecutor(new BaltopCommand(this));
        }

        // Команда управления префиксами /pex
        if (getCommand("pex") != null) {
            getCommand("pex").setExecutor(new PexCommand(this, prefixManager));
        }



        // ===== WARP / HOME / LIST КОМАНДЫ =====
        if (getCommand("spawn") != null) {
            getCommand("spawn").setExecutor(new SpawnCommand(warpManager));
        }
        if (getCommand("home") != null) {
            getCommand("home").setExecutor(new HomeCommand(warpManager));
        }
        if (getCommand("sethome") != null) {
            getCommand("sethome").setExecutor(new SetHomeCommand(warpManager, this));
        }
        if (getCommand("delhome") != null) {
            getCommand("delhome").setExecutor(new DelHomeCommand(warpManager));
        }
        if (getCommand("warp") != null) {
            getCommand("warp").setExecutor(new WarpCommand(warpManager));
        }
        if (getCommand("setwarp") != null) {
            getCommand("setwarp").setExecutor(new SetWarpCommand(warpManager));
        }
        if (getCommand("delwarp") != null) {
            getCommand("delwarp").setExecutor(new DelWarpCommand(warpManager));
        }
        if (getCommand("warplist") != null) {
            getCommand("warplist").setExecutor(new WarplistCommand(warpManager));
        }
        if (getCommand("homelist") != null) {
            getCommand("homelist").setExecutor(new HomelistCommand(warpManager));
        }

        // Регистрируем команды модулей

        // Fly ✅ ИСПРАВЛЕНО
        flyModule = new FlyModule(getConfig());
        getServer().getPluginManager().registerEvents(flyModule, this);
        if (getCommand("fly") != null) {
            getCommand("fly").setExecutor(new FlyCommand(getConfig(), flyModule));  // Передаём flyModule
        }

        // Heal
        healModule = new HealModule(getConfig());
        if (getCommand("heal") != null) {
            getCommand("heal").setExecutor(new HealCommand(getConfig()));  // /heal
        }
        // УБРАНО: healModule не implements Listener

        // Gamemode
        gamemodeModule = new GamemodeModule();
        if (getCommand("gamemode") != null) {
            getCommand("gamemode").setExecutor(new GamemodeCommand(gamemodeModule));  // /gamemode
        }
        // УБРАНО: gamemodeModule не implements Listener

        // God
        godModule = new GodModule();
        if (getCommand("god") != null) {
            getCommand("god").setExecutor(new GodCommand(godModule));  // /god
        }
        // УБРАНО: godModule не implements Listener

        // Teleport
        teleportModule = new TeleportModule();
        getServer().getPluginManager().registerEvents(teleportModule, this);

        if (getCommand("tp") != null) {
            getCommand("tp").setExecutor(new me.kodysimpson.vaulteconomy.modules.teleport.TeleportCommand(teleportModule));
        }
        if (getCommand("tpa") != null) {
            getCommand("tpa").setExecutor(new me.kodysimpson.vaulteconomy.modules.teleport.TpaCommand(teleportModule));
        }
        if (getCommand("tpaaccept") != null) {
            getCommand("tpaaccept").setExecutor(new me.kodysimpson.vaulteconomy.modules.teleport.TpaAcceptCommand(teleportModule));
        }
        if (getCommand("tpadeny") != null) {
            getCommand("tpadeny").setExecutor(new me.kodysimpson.vaulteconomy.modules.teleport.TpaDenyCommand(teleportModule));
        }
        if (getCommand("tpahere") != null) {
            getCommand("tpahere").setExecutor(new me.kodysimpson.vaulteconomy.modules.teleport.TpahereCommand(teleportModule));
        }

        // Дополнительные команды
        if (getCommand("clearchat") != null) {
            getCommand("clearchat").setExecutor(new ClearChatCommand());
        }
        if (getCommand("feed") != null) {
            getCommand("feed").setExecutor(new FeedCommand());
        }
        if (getCommand("ignore") != null) {
            getCommand("ignore").setExecutor(new IgnoreCommand(ignoreManager));
        }
        if (getCommand("sp") != null) {
            getCommand("sp").setExecutor(new SpawnMobCommand());
        }

        // Визуальные эффекты (пример, пока не используется)
        if (getCommand("visualeffects") != null) {
            // VisualEffects.showTeleportEffect(getServer().getPlayer("playerName"));
        }

        // ===== УТИЛИТЫ =====
        if (getCommand("craft") != null) {
            getCommand("craft").setExecutor(new me.kodysimpson.vaulteconomy.news.command.CraftCommand(this));
        }

        if (getCommand("anvil") != null) {
            getCommand("anvil").setExecutor(new me.kodysimpson.vaulteconomy.news.command.AnvilCommand());
        }
        if (getCommand("ec") != null) {
            getCommand("ec").setExecutor(new me.kodysimpson.vaulteconomy.news.command.EnderChestCommand());
        }
        if (getCommand("sejf") != null) {
            getCommand("sejf").setExecutor(new me.kodysimpson.vaulteconomy.news.command.SejfCommand());
        }
        if (getCommand("repair") != null) {
            getCommand("repair").setExecutor(new me.kodysimpson.vaulteconomy.news.command.RepairCommand(this));
        }

        // ✅ ФИЛЬТР ЧАТА - ДОБАВЬ ЭТУ СТРОКУ
        if (getCommand("filter") != null) {
            getCommand("filter").setExecutor(new ChatFilterCommand(this));
        }

        // ✅ КАСТОМНЫЕ ПРЕДМЕТЫ - ТОЛЬКО ЗЕЛЬЯ
        this.kastomFurnace = new KastomFurnace(getConfig());
        this.kastomPotion = new KastomPotion();
        getServer().getPluginManager().registerEvents(this.kastomFurnace, this);
        getServer().getPluginManager().registerEvents(this.kastomPotion, this);

        // ✅ АУКЦИОН - ОДИН РАЗ
        this.auctionMain = new AuctionMain(this);
        if (getCommand("ah") != null) {
            getCommand("ah").setExecutor(this.auctionMain);
        }

        // ✅ НЕЗАВИСИМЫЙ HELP - БЕЗОПАСНО
        HelpAh helpAh = new HelpAh(this);
        if (getCommand("ahelp") != null) {
            getCommand("ahelp").setExecutor(helpAh);
        }

        if (getCommand("kastom") != null) {
            getCommand("kastom").setExecutor(new KastomCommand());  // ✅ 0 аргументов
        }

        // Выводим сообщение в консоль о символе валюты
        String symbol = getConfig().getString("currency.symbol", "⛁");
        getLogger().info("VaultEconomy включён. Символ основной валюты: " + symbol);
    }

    @Override
    public void onDisable() {

        // Сохраняем балансы основной экономики
        if (economy != null) {
            economy.saveBalances();
        }

        // Сохраняем данные по мультивалюте
        if (pointManager != null) {
            pointManager.save();
        }

        getLogger().info("VaultEconomy выключен.");
    }

    // Метод для перезагрузки конфигурации плагина
    public void reloadPluginConfig() {
        reloadConfig();
        getLogger().info("Конфигурация плагина перезагружена.");
    }

    // Геттеры для доступа к экономике и модулям
    public CustomEconomy getEconomy() {
        return economy;
    }

    public PointManager getPointManager() {
        return pointManager;
    }

    public PrefixManager getPrefixManager() {
        return prefixManager;
    }

    public IgnoreManager getIgnoreManager() {
        return ignoreManager;
    }


    public WarpManager getWarpManager() {
        return warpManager;
    }

    public HealModule getHealModule() {
        return healModule;
    }

    public TeleportModule getTeleportModule() {
        return teleportModule;
    }

    public GodModule getGodModule() {
        return godModule;
    }

    public FlyModule getFlyModule() {
        return flyModule;
    }

    public GamemodeModule getGamemodeModule() {
        return gamemodeModule;
    }

    public KastomFurnace getKastomFurnace() {
        return kastomFurnace;
    }

    public KastomPotion getKastomPotion() {
        return kastomPotion;
    }

    public AuctionMain getAuctionMain() { return auctionMain; }
    // Синглтон: возвращаем единственный экземпляр плагина
    public static VaultEconomy getInstance() {
        return instance;
    }
}