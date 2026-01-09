package me.kodysimpson.vaulteconomy.modules.heal;

import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

public class HealModule {

    private final FileConfiguration config;
    private final PvpManager pvpManager;

    public HealModule(FileConfiguration config, PvpManager pvpManager) {
        this.config = config;
        this.pvpManager = pvpManager;
    }

    public void healPlayer(Player player) {

        // ❌ БЛОК В PvP
        if (pvpManager.isInPvp(player)) {
            player.sendMessage("§c⛔ Нельзя лечиться во время PvP!");
            return;
        }

        if (!config.getBoolean("heal.enable-heal", true)) {
            player.sendMessage(config.getString(
                    "messages.heal-disabled",
                    "§cЛечение отключено."
            ));
            return;
        }

        player.setHealth(player.getMaxHealth());
        player.sendMessage(config.getString(
                "messages.heal-enabled",
                "§aВы успешно вылечены."
        ));
    }
}