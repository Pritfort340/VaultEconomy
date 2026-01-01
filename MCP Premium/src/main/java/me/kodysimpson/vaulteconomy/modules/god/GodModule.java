package me.kodysimpson.vaulteconomy.modules.god;

import org.bukkit.entity.Player;

public class GodModule {

    // Метод для активации режима Бога
    public void enableGodMode(Player player) {
        player.setInvulnerable(true);  // Игрок не будет получать урон
    }

    // Метод для деактивации режима Бога
    public void disableGodMode(Player player) {
        player.setInvulnerable(false);  // Игрок снова будет получать урон
    }

    // Метод для переключения режима Бога
    public void toggleGodMode(Player player) {
        if (player.isInvulnerable()) {
            disableGodMode(player);  // Если режим Бога включен, выключаем его
            player.sendMessage("Режим Бога выключен.");
        } else {
            enableGodMode(player);  // Если режим Бога выключен, включаем его
            player.sendMessage("Режим Бога включен.");
        }
    }
}
