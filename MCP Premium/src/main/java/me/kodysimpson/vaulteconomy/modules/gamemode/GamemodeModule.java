package me.kodysimpson.vaulteconomy.modules.gamemode;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GamemodeModule {

    // Метод для смены игрового режима игрока
    public void setGamemode(Player player, String mode) {
        switch (mode.toLowerCase()) {
            case "0":
            case "survival":
                player.setGameMode(GameMode.SURVIVAL);
                break;
            case "1":
            case "creative":
                player.setGameMode(GameMode.CREATIVE);
                break;
            case "2":
            case "adventure":
                player.setGameMode(GameMode.ADVENTURE);
                break;
            case "3":
            case "spectator":
                player.setGameMode(GameMode.SPECTATOR);
                break;
            default:
                player.sendMessage("Неверный режим игры! Используйте 0, 1, 2 или 3.");
                return;
        }

        player.sendMessage("Ваш игровой режим изменен на: " + mode);
    }
}