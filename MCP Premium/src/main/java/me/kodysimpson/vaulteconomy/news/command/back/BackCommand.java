package me.kodysimpson.vaulteconomy.news.command.back;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BackCommand implements CommandExecutor {

    private final VaultEconomy plugin;
    private final PvpManager pvpManager;

    public BackCommand(VaultEconomy plugin) {
        this.plugin = plugin;
        this.pvpManager = plugin.getPvpManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            sender.sendMessage("§cТолько для игроков!");
            return true;
        }

        // ❌ БЛОК В PvP
        if (pvpManager.isInPvp(p)) {
            p.sendMessage("§c❌ Нельзя использовать /back во время PvP!");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 0.8f);
            return true;
        }

        Location back = BackListener.getLastLocation(p);
        if (back == null) {
            p.sendMessage("§c❌ Нет точки возврата!");
            return true;
        }

        p.teleport(back);
        p.sendMessage("§a✔ Вы вернулись назад");
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 1.2f);
        return true;
    }
}