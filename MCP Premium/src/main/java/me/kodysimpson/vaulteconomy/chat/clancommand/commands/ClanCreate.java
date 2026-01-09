package me.kodysimpson.vaulteconomy.chat.clancommand.commands;

import me.kodysimpson.vaulteconomy.chat.clancommand.Clan;
import me.kodysimpson.vaulteconomy.chat.clancommand.ClanManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ClanCreate {

    private final ClanManager clanManager;

    public ClanCreate(ClanManager clanManager) {
        this.clanManager = clanManager;
    }

    public void execute(Player player, String inputName) {

        UUID uuid = player.getUniqueId();

        /* ===== ПРОВЕРКА КЛАНА ===== */
        if (clanManager.hasClan(uuid)) {
            player.sendMessage("§cТы уже состоишь в клане.");
            return;
        }

        if (inputName == null) {
            player.sendMessage("§cУкажи имя клана.");
            return;
        }

        /* ===== УБИРАЕМ ЦВЕТА ДЛЯ ПРОВЕРОК ===== */
        String plainName = ChatColor.stripColor(
                ChatColor.translateAlternateColorCodes('&', inputName)
        );

        /* ===== ПРОВЕРКА ДЛИНЫ ===== */
        if (plainName.length() < 3 || plainName.length() > 16) {
            player.sendMessage("§cИмя клана: 3–16 символов (без учёта цветов).");
            return;
        }

        /* ===== ПРОВЕРКА СИМВОЛОВ ===== */
        if (!plainName.matches("[A-Za-zА-Яа-я0-9]+")) {
            player.sendMessage("§cИмя клана может содержать только буквы и цифры.");
            return;
        }

        /* ===== ПРОВЕРКА НА СУЩЕСТВОВАНИЕ ===== */
        if (clanManager.getClanByName(plainName) != null) {
            player.sendMessage("§cКлан с таким именем уже существует.");
            return;
        }

        /* ===== ЦВЕТНОЕ ИМЯ ===== */
        String coloredName = ChatColor.translateAlternateColorCodes('&', inputName);

        /* ===== СОЗДАНИЕ ===== */
        Clan clan = clanManager.createClan(coloredName, player);

        player.sendMessage("§aКлан " + coloredName + " §aуспешно создан!");
    }
}