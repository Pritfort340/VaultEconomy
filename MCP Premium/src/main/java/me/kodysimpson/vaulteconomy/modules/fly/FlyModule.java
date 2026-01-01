package me.kodysimpson.vaulteconomy.modules.fly;

import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class FlyModule implements Listener {

    private final FileConfiguration config;

    public FlyModule(FileConfiguration config) {
        this.config = config;
    }

    // Активировать или деактивировать полет для игрока
    public void toggleFly(Player player) {
        if (player.getAllowFlight()) {
            player.setAllowFlight(false);  // Выключаем полет
            player.setFlying(false);        // Прекращаем полет
            player.sendMessage(config.getString("messages.fly-disabled"));
        } else {
            player.setAllowFlight(true);   // Включаем полет
            player.sendMessage(config.getString("messages.fly-enabled"));
        }
    }

    // Обработка урона, отключая полет при получении урона
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player.getAllowFlight()) {
                player.setAllowFlight(false);  // Выключаем полет
                player.setFlying(false);        // Прекращаем полет
                player.sendMessage(config.getString("messages.fly-disabled-on-damage"));
            }
        }
    }
}