package me.kodysimpson.vaulteconomy.news.command.filter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import me.kodysimpson.vaulteconomy.VaultEconomy;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.List;

public class ChatFilterCommand implements CommandExecutor, TabCompleter {

    private final VaultEconomy plugin;
    private final ChatFilterManager filterManager;

    public ChatFilterCommand(VaultEconomy plugin) {
        this.plugin = plugin;
        this.filterManager = new ChatFilterManager(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("vaulteconomy.filter.admin")) {
            sender.sendMessage(ChatColor.RED + "❌ Нет прав!");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "=== " + ChatColor.YELLOW + "ФИЛЬТР ЧАТА " + ChatColor.GOLD + "" + ChatColor.BOLD + "===");
            sender.sendMessage(ChatColor.GRAY + "/filter add " + ChatColor.WHITE + "слово " + ChatColor.GRAY + "- добавить");
            sender.sendMessage(ChatColor.GRAY + "/filter remove " + ChatColor.WHITE + "слово " + ChatColor.GRAY + "- удалить");
            sender.sendMessage(ChatColor.GRAY + "/filter list " + ChatColor.GRAY + "- список");
            sender.sendMessage(ChatColor.GRAY + "/filter toggle " + ChatColor.GRAY + "- вкл/выкл");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add":
                if (args.length < 2) return false;
                filterManager.addWord(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
                sender.sendMessage(ChatColor.GREEN + "✅ Слово добавлено!");
                break;
            case "remove":
            case "del":
                if (args.length < 2) return false;
                filterManager.removeWord(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
                sender.sendMessage(ChatColor.GREEN + "✅ Слово удалено!");
                break;
            case "list":
                filterManager.showList(sender);
                break;
            case "toggle":
                filterManager.toggleFilter();
                sender.sendMessage(filterManager.isEnabled() ?
                        ChatColor.GREEN + "✅ Фильтр ВКЛ" :
                        ChatColor.RED + "❌ Фильтр ВЫКЛ");
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("add", "remove", "list", "toggle");
        }
        return null;
    }
}