package me.kodysimpson.vaulteconomy.economy;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.economy.PointCurrency;
import me.kodysimpson.vaulteconomy.economy.PointManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PointCommand implements CommandExecutor {

    private final VaultEconomy plugin;
    private final PointManager manager;

    public PointCommand(VaultEconomy plugin, PointManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    private String msg(String path) {
        String raw = plugin.getConfig().getString("points.messages." + path,
                "&cСообщение не найдено: " + path);
        return color(raw);
    }

    private String msg(String path, Map<String, String> placeholders) {
        String s = msg(path);
        for (Map.Entry<String, String> e : placeholders.entrySet()) {
            s = s.replace("{" + e.getKey() + "}", e.getValue());
        }
        return s;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage(color("&6===== Дополнительная валюта (/point) ====="));
        sender.sendMessage(color("&e/point help &7- показать эту справку"));
        sender.sendMessage(color("&e/point create <name> <displayName> [symbol] &7- создать валюту"));
        sender.sendMessage(color("&e/point remove <name> &7- удалить валюту"));
        sender.sendMessage(color("&e/point list &7- список всех валют"));
        sender.sendMessage(color("&e/point bal <name> [player] &7- баланс по валюте"));
        sender.sendMessage(color("&e/point <name> give <player> <amount> &7- выдать валюту"));
        sender.sendMessage(color("&e/point <name> take <player> <amount> &7- забрать валюту"));
        sender.sendMessage(color("&e/point <name> remove <player> <amount> &7- то же, что take"));
        sender.sendMessage(color("&e/point <name> pay <player> <amount> &7- заплатить игроку"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("vaulteconomy.point.use")) {
            sender.sendMessage(msg("no-permission"));
            return true;
        }

        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }

        // /point help
        if (args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }

        // ===== админские подкоманды =====

        if (args[0].equalsIgnoreCase("create")) {
            if (!sender.hasPermission("vaulteconomy.point.admin")) {
                sender.sendMessage(msg("no-permission"));
                return true;
            }

            if (args.length < 3) {
                sender.sendMessage(color("&cИспользование: /point create <name> <displayName> [symbol]"));
                return true;
            }

            String name = args[1];
            String display = args[2];

            String symbol;
            if (args.length >= 4) {
                symbol = args[3];
            } else {
                symbol = plugin.getConfig().getString("points.default-symbol", "★");
            }

            if (!manager.createCurrency(name, display, symbol)) {
                Map<String, String> ph = new HashMap<>();
                ph.put("name", name);
                sender.sendMessage(msg("currency-exists", ph));
                return true;
            }

            Map<String, String> ph = new HashMap<>();
            ph.put("name", name);
            ph.put("symbol", symbol);
            sender.sendMessage(msg("currency-created", ph));
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (!sender.hasPermission("vaulteconomy.point.admin")) {
                sender.sendMessage(msg("no-permission"));
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(color("&cИспользование: /point remove <name>"));
                return true;
            }

            String name = args[1];
            Map<String, String> ph = new HashMap<>();
            ph.put("name", name);

            if (manager.removeCurrency(name)) {
                sender.sendMessage(msg("currency-removed", ph));
            } else {
                sender.sendMessage(msg("currency-not-found", ph));
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            sender.sendMessage(msg("list-header"));
            for (PointCurrency c : manager.getCurrencies()) {
                Map<String, String> ph = new HashMap<>();
                ph.put("name", c.getName());
                ph.put("display", c.getDisplayName());
                ph.put("symbol", c.getSymbol());
                sender.sendMessage(msg("list-entry", ph));
            }
            return true;
        }

        // ===== /point bal <currency> [player] =====

        if (args[0].equalsIgnoreCase("bal")) {
            if (args.length < 2) {
                sender.sendMessage(color("&cИспользование: /point bal <name> [player]"));
                return true;
            }

            String currencyId = args[1];
            PointCurrency currency = manager.getCurrency(currencyId);
            if (currency == null) {
                Map<String, String> ph = new HashMap<>();
                ph.put("name", currencyId);
                sender.sendMessage(msg("currency-not-found", ph));
                return true;
            }

            OfflinePlayer target;
            if (args.length >= 3) {
                target = Bukkit.getOfflinePlayer(args[2]);
            } else {
                if (!(sender instanceof Player p)) {
                    sender.sendMessage(color("&cУкажите игрока: /point bal " + currency.getName() + " <player>"));
                    return true;
                }
                target = p;
            }

            double bal = currency.getBalance(target);
            sender.sendMessage(color("&aБаланс " + target.getName() + " по валюте "
                    + currency.getName() + ": &6" + bal + currency.getSymbol()));
            return true;
        }

        // ===== /point <currency> ... =====

        if (args.length < 2) {
            sender.sendMessage(color("&cНеверная команда. Используйте /point help"));
            return true;
        }

        String currencyId = args[0];
        PointCurrency currency = manager.getCurrency(currencyId);

        if (currency == null) {
            Map<String, String> ph = new HashMap<>();
            ph.put("name", currencyId);
            sender.sendMessage(msg("currency-not-found", ph));
            return true;
        }

        String sub = args[1].toLowerCase();

        if (args.length < 4) {
            sender.sendMessage(color("&cИспользование: /point " + currency.getName()
                    + " " + sub + " <player> <amount>"));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[2]);
        if (target.getName() == null) {
            sender.sendMessage(msg("player-not-found"));
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(msg("not-a-number"));
            return true;
        }

        Map<String, String> ph = new HashMap<>();
        ph.put("player", target.getName());
        ph.put("amount", String.valueOf(amount));
        ph.put("symbol", currency.getSymbol());
        ph.put("currency", currency.getName());

        switch (sub) {
            case "give" -> {
                if (!sender.hasPermission("vaulteconomy.point.admin")) {
                    sender.sendMessage(msg("no-permission"));
                    return true;
                }
                currency.deposit(target, amount);
                sender.sendMessage(msg("give", ph));
            }

            case "take", "remove" -> {
                if (!sender.hasPermission("vaulteconomy.point.admin")) {
                    sender.sendMessage(msg("no-permission"));
                    return true;
                }
                if (!currency.withdraw(target, amount)) {
                    sender.sendMessage(msg("insufficient-funds"));
                    return true;
                }
                sender.sendMessage(msg("take", ph));
            }

            case "pay" -> {
                if (!(sender instanceof Player p)) {
                    sender.sendMessage(color("&cКоманда только для игроков."));
                    return true;
                }

                OfflinePlayer from = p;

                Map<String, String> phSender = new HashMap<>(ph);
                phSender.put("player", target.getName());

                Map<String, String> phReceiver = new HashMap<>();
                phReceiver.put("player", p.getName());
                phReceiver.put("amount", String.valueOf(amount));
                phReceiver.put("symbol", currency.getSymbol());
                phReceiver.put("currency", currency.getName());

                if (!currency.withdraw(from, amount)) {
                    sender.sendMessage(msg("insufficient-funds"));
                    return true;
                }
                currency.deposit(target, amount);

                p.sendMessage(msg("pay-sender", phSender));

                if (target.isOnline() && target.getPlayer() != null) {
                    target.getPlayer().sendMessage(msg("pay-receiver", phReceiver));
                }
            }

            default -> sender.sendMessage(color("&cНеизвестное действие. Используйте /point help"));
        }

        return true;
    }
}