package me.kodysimpson.vaulteconomy.modules.heal;

import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;

public class HealModule {

    private final FileConfiguration config;

    public HealModule(FileConfiguration config) {
        this.config = config;
    }

    // Метод для исцеления игрока
    public void healPlayer(Player player) {
        // Проверяем, имеет ли игрок достаточные права для использования команды
        if (config.getBoolean("heal.enable-heal", true)) {
            player.setHealth(player.getMaxHealth());  // Восстанавливаем здоровье до максимума
            player.sendMessage(config.getString("messages.heal-enabled"));
        } else {
            player.sendMessage(config.getString("messages.heal-disabled"));
        }
    }
}
