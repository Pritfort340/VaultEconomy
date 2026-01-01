package me.kodysimpson.vaulteconomy.news.command.auction;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.InputStream;
import java.io.InputStreamReader;

public class HelpAh implements CommandExecutor {
    private final VaultEconomy plugin;
    private YamlConfiguration config;

    public HelpAh(VaultEconomy plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    private void loadConfig() {
        try {
            InputStream stream = plugin.getResource("HelpAh.yml");
            if (stream != null) {
                config = YamlConfiguration.loadConfiguration(new InputStreamReader(stream));
            } else {
                plugin.getLogger().warning("HelpAh.yml не найден в resources!");
                config = new YamlConfiguration();
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Ошибка HelpAh.yml: " + e.getMessage());
            config = new YamlConfiguration();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(ChatColor.RED + "Только для игроков!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("vaulteconomy.auction.help")) {
            String noPerm = config.getString("messages.no_permission", "&c&l❌ Нет прав для справки!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPerm));
            return true;
        }

        // ✅ СООБЩЕНИЯ ИЗ YML с ChatColor
        String title = config.getString("messages.help.title", "&c&l&nАУКЦИОН КОМАНДЫ:");
        String menu = config.getString("messages.help.menu", "&f/ah &7- открыть меню");
        String sell = config.getString("messages.help.sell", "&f/ah sell <цена> &7- продать предмет");
        String example = config.getString("messages.help.example", "&e&lПример: &f/ah sell 100");

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', title));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', menu));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', sell));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', example));

        return true;
    }
}