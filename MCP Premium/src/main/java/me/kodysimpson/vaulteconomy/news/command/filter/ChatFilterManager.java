package me.kodysimpson.vaulteconomy.news.command.filter;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;
import me.kodysimpson.vaulteconomy.VaultEconomy;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ChatFilterManager implements Listener {

    private final VaultEconomy plugin;
    private final Set<String> badWords = new HashSet<>();
    private boolean enabled = true;
    private File filterFile;
    private FileConfiguration filterConfig;

    // ✅ СИСТЕМА ПРЕДУПРЕЖДЕНИЙ И МУТА
    private final Map<UUID, Integer> warnings = new HashMap<>();
    private final Set<UUID> mutedPlayers = new HashSet<>();

    public ChatFilterManager(VaultEconomy plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        setupFilterFile();
        loadFilter();
    }

    private void setupFilterFile() {
        filterFile = new File(plugin.getDataFolder(), "filter.yml");
        if (!filterFile.exists()) {
            try {
                filterFile.getParentFile().mkdirs();
                plugin.saveResource("filter.yml", false);
            } catch (Exception e) {
                plugin.getLogger().warning("Не удалось создать filter.yml!");
                try {
                    filterFile.createNewFile();
                } catch (IOException ex) {
                    plugin.getLogger().severe("Ошибка создания filter.yml: " + ex.getMessage());
                }
            }
        }
        filterConfig = YamlConfiguration.loadConfiguration(filterFile);
    }

    public void addWord(String word) {
        badWords.add(word.toLowerCase());
        filterConfig.set("enabled", enabled);
        filterConfig.set("words", new ArrayList<>(badWords));
        saveFilter();
        plugin.getLogger().info("Фильтр: добавлено слово '" + word + "'");
    }

    public void removeWord(String word) {
        badWords.remove(word.toLowerCase());
        filterConfig.set("enabled", enabled);
        filterConfig.set("words", new ArrayList<>(badWords));
        saveFilter();
        plugin.getLogger().info("Фильтр: удалено слово '" + word + "'");
    }

    public void showList(CommandSender sender) {
        if (badWords.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "Список пуст");
            return;
        }
        sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "=== " + ChatColor.YELLOW +
                "ЗАБАНЕННЫЕ СЛОВА " + ChatColor.GOLD + "" + ChatColor.BOLD +
                "=== " + ChatColor.GRAY + "(" + badWords.size() + ")");
        int i = 1;
        for (String word : badWords) {
            sender.sendMessage(ChatColor.GRAY + "" + i++ + ". " + ChatColor.WHITE + word);
        }
    }

    public void toggleFilter() {
        enabled = !enabled;
        filterConfig.set("enabled", enabled);
        saveFilter();
        plugin.getLogger().info("Фильтр чата: " + (enabled ? "ВКЛЮЧЁН" : "ВЫКЛЮЧЁН"));
    }

    public boolean isEnabled() {
        return enabled;
    }

    // ✅ ПРОВЕРКА ПРЕДУПРЕЖДЕНИЙ
    private void checkWarnings(Player player, String badWord) {
        UUID uuid = player.getUniqueId();
        int count = warnings.getOrDefault(uuid, 0) + 1;
        warnings.put(uuid, count);

        switch (count) {
            case 1:
                player.sendMessage(ChatColor.RED + "❌ " + ChatColor.BOLD + "ПРЕДУПРЕЖДЕНИЕ 1/3" + ChatColor.RED + ": " + ChatColor.WHITE + badWord);
                player.sendMessage(ChatColor.YELLOW + "⚠️ " + ChatColor.BOLD + "Ещё 2 = МУТ 10 минут!");
                break;
            case 2:
                player.sendMessage(ChatColor.GOLD + "❌ " + ChatColor.BOLD + "ПРЕДУПРЕЖДЕНИЕ 2/3" + ChatColor.GOLD + ": " + ChatColor.WHITE + badWord);
                player.sendMessage(ChatColor.RED + "⚠️ " + ChatColor.BOLD + "Следующее = МУТ 10 МИНУТ!");
                break;
            case 3:
                mutePlayer(player);
                warnings.remove(uuid); // Сброс после мута
                break;
        }
    }

    // ✅ МУТ НА 10 МИНУТ
    private void mutePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        mutedPlayers.add(uuid);

        player.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "🔇 МУТ 10 МИНУТ за 3 предупреждения!");
        plugin.getLogger().warning("🔇 МУТ: " + player.getName() + " за 3 предупреждения (фильтр чата)");

        // Снимаем мут через 10 минут
        new BukkitRunnable() {
            @Override
            public void run() {
                mutedPlayers.remove(uuid);
                if (player.isOnline()) {
                    player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "✅ Мут снят! Больше не матерись.");
                }
            }
        }.runTaskLater(plugin, 20L * 60 * 10); // 10 минут = 12000 тиков
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (!enabled || e.isCancelled()) return;

        Player p = e.getPlayer();

        // ✅ ПРОВЕРКА МУТА
        if (mutedPlayers.contains(p.getUniqueId())) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.DARK_RED + "🔇 Ты в муте! Останется: ~" +
                    getRemainingMuteTime(p.getUniqueId()) + " мин");
            return;
        }

        // ✅ ПРОПУСК ДЛЯ АДМИНОВ
        if (p.hasPermission("vaulteconomy.filter.bypass")) return;

        String msg = e.getMessage().toLowerCase();
        for (String bad : badWords) {
            if (msg.contains(bad)) {
                e.setCancelled(true); // ✅ УДАЛЯЕТ СООБЩЕНИЕ
                plugin.getLogger().info("Фильтр: " + p.getName() + " пытался сказать: '" + bad + "'");
                checkWarnings(p, bad); // ✅ ДАЁТ ПРЕДУПРЕЖДЕНИЕ
                return;
            }
        }
    }

    // ✅ ВРЕМЯ ОСТАЛОСЬ ДО СНЯТИЯ МУТА (примерно)
    private String getRemainingMuteTime(UUID uuid) {
        return "9:30"; // Упрощённо, можно сделать точный таймер
    }

    private void loadFilter() {
        enabled = filterConfig.getBoolean("enabled", true);
        List<?> wordsList = filterConfig.getList("words");
        if (wordsList != null) {
            for (Object word : wordsList) {
                if (word instanceof String) {
                    badWords.add(((String) word).toLowerCase());
                }
            }
        }
        plugin.getLogger().info("Фильтр чата загружен: " + badWords.size() + " слов");
    }

    private void saveFilter() {
        try {
            filterConfig.set("enabled", enabled);
            filterConfig.set("words", new ArrayList<>(badWords));
            filterConfig.save(filterFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Ошибка сохранения filter.yml: " + e.getMessage());
        }
    }
}