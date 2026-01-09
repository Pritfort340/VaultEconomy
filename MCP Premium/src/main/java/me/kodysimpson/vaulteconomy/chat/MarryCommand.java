package me.kodysimpson.vaulteconomy.chat;

import me.kodysimpson.vaulteconomy.VaultEconomy;
import me.kodysimpson.vaulteconomy.economy.CustomEconomy;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class MarryCommand implements CommandExecutor {

    private final VaultEconomy plugin;
    private final MarriageManager marriageManager;
    private final CustomEconomy economy;

    // ожидание согласия: priest -> (groom, bride)
    private final Map<UUID, List<UUID>> pendingMarry = new HashMap<>();
    private final Map<UUID, List<UUID>> pendingDivorce = new HashMap<>();

    // кто из пары уже нажал /marry accept: priest -> {uuid1, uuid2}
    private final Map<UUID, Set<UUID>> pendingMarryAccepted = new HashMap<>();
    private final Map<UUID, Set<UUID>> pendingDivorceAccepted = new HashMap<>();

    public MarryCommand(VaultEconomy plugin, MarriageManager marriageManager) {
        this.plugin = plugin;
        this.marriageManager = marriageManager;
        this.economy = plugin.getEconomy();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player priest)) {
            sender.sendMessage("Только игрок может использовать /marry");
            return true;
        }

        if (args.length == 0) {
            priest.sendMessage("§e/marry - список команд");
            priest.sendMessage("§e/marry невеста жених §7- заключить брак (по 50k с каждого)");
            priest.sendMessage("§e/marry divorce невеста жених §7- развод (по 50k с каждого)");
            priest.sendMessage("§e/marry list §7- список игроков в браке");
            priest.sendMessage("§e/marry accept §7- принять свадьбу/развод");
            priest.sendMessage("§e/marry kiss §7- поцеловать партнёра");
            priest.sendMessage("§e/marry gift §7- подарить предмет в руке (50)");
            priest.sendMessage("§e/marry tp §7- телепорт к партнёру (3 раза в день)");
            priest.sendMessage("§e/marry chat on/off §7- показать/скрыть статус брака в чате");
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            List<UUID> married = marriageManager.getAllMarried();
            priest.sendMessage("§aЖенатые игроки:");
            for (UUID u : married) {
                String name = Optional.ofNullable(Bukkit.getOfflinePlayer(u).getName())
                        .orElse("Неизвестен");
                priest.sendMessage(" - " + name);
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("accept")) {
            handleAccept(priest);
            return true;
        }

        if (args[0].equalsIgnoreCase("divorce") && args.length == 3) {
            handleDivorceRequest(priest, args[1], args[2]);
            return true;
        }

        if (args[0].equalsIgnoreCase("kiss")) {
            handleKiss(priest);
            return true;
        }

        if (args[0].equalsIgnoreCase("gift")) {
            handleGift(priest);
            return true;
        }

        if (args[0].equalsIgnoreCase("tp")) {
            handleTp(priest);
            return true;
        }

        if (args[0].equalsIgnoreCase("chat") && args.length == 2) {
            boolean on = args[1].equalsIgnoreCase("on");
            marriageManager.setChatVisible(priest.getUniqueId(), on);
            priest.sendMessage(on
                    ? "§aСтатус брака теперь виден в чате."
                    : "§cСтатус брака скрыт в чате.");
            return true;
        }

        // /marry невеста жених
        if (args.length == 2) {
            handleMarryRequest(priest, args[0], args[1]);
            return true;
        }

        priest.sendMessage("§cНеверная команда /marry");
        return true;
    }

    // ===== ЗАПРОС НА СВАДЬБУ =====

    private void handleMarryRequest(Player priest, String brideName, String groomName) {
        Player bride = Bukkit.getPlayer(brideName);
        Player groom = Bukkit.getPlayer(groomName);

        if (bride == null || groom == null) {
            priest.sendMessage("§cОба игрока должны быть онлайн.");
            return;
        }

        double cost = 50000;

        boolean freeBride = bride.hasPermission("vaulteconomy.marry.free");
        boolean freeGroom = groom.hasPermission("vaulteconomy.marry.free");

        if (!freeBride && economy.getBalance(bride) < cost) {
            priest.sendMessage("§cУ " + bride.getName() + " нет 50,000 для свадьбы.");
            return;
        }
        if (!freeGroom && economy.getBalance(groom) < cost) {
            priest.sendMessage("§cУ " + groom.getName() + " нет 50,000 для свадьбы.");
            return;
        }

        UUID priestId = priest.getUniqueId();
        pendingMarry.put(priestId, Arrays.asList(bride.getUniqueId(), groom.getUniqueId()));
        pendingMarryAccepted.put(priestId, new HashSet<>()); // никто ещё не нажал accept

        priest.sendMessage("§eЗапрос на брак отправлен. Пусть оба введут §f/marry accept§e.");
        bride.sendMessage("§dСвященник предложил вам брак с §f" + groom.getName() + "§d. Введите §f/marry accept§d.");
        groom.sendMessage("§dСвященник предложил вам брак с §f" + bride.getName() + "§d. Введите §f/marry accept§d.");
    }

    // ===== ПРИНЯТИЕ ЗАПРОСА (СВАДЬБА / РАЗВОД) =====

    private void handleAccept(Player p) {
        UUID pid = p.getUniqueId();

        // --- свадьба ---
        for (Map.Entry<UUID, List<UUID>> entry : pendingMarry.entrySet()) {
            UUID priestId = entry.getKey();
            List<UUID> pair = entry.getValue();

            if (!pair.contains(pid)) continue;

            Player priest = Bukkit.getPlayer(priestId);
            if (priest == null) {
                pendingMarry.remove(priestId);
                pendingMarryAccepted.remove(priestId);
                return;
            }

            pendingMarryAccepted.putIfAbsent(priestId, new HashSet<>());
            Set<UUID> accepted = pendingMarryAccepted.get(priestId);
            accepted.add(pid);

            if (accepted.size() < 2) {
                p.sendMessage("§eВы подтвердили. Ожидается согласие второго игрока.");
                return;
            }

            // оба согласились
            UUID a = pair.get(0);
            UUID b = pair.get(1);
            Player pa = Bukkit.getPlayer(a);
            Player pb = Bukkit.getPlayer(b);
            if (pa == null || pb == null) {
                priest.sendMessage("§cКто-то из пары вышел с сервера.");
                pendingMarry.remove(priestId);
                pendingMarryAccepted.remove(priestId);
                return;
            }

            double cost = 50000;

            boolean freeA = pa.hasPermission("vaulteconomy.marry.free");
            boolean freeB = pb.hasPermission("vaulteconomy.marry.free");

            if (!freeA && economy.getBalance(pa) < cost) {
                priest.sendMessage("§cУ " + pa.getName() + " нет 50,000 для бракосочетания.");
                return;
            }
            if (!freeB && economy.getBalance(pb) < cost) {
                priest.sendMessage("§cУ " + pb.getName() + " нет 50,000 для бракосочетания.");
                return;
            }

            if (!freeA) economy.withdrawPlayer(pa, cost);
            if (!freeB) economy.withdrawPlayer(pb, cost);

            marriageManager.marry(a, b);

            pa.sendTitle("§d♥ СВАДЬБА ♥", "Вы теперь с " + pb.getName(), 10, 60, 10);
            pb.sendTitle("§d♥ СВАДЬБА ♥", "Вы теперь с " + pa.getName(), 10, 60, 10);

            pa.playSound(pa.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
            pb.playSound(pb.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);

            pendingMarry.remove(priestId);
            pendingMarryAccepted.remove(priestId);
            return;
        }

        // --- развод ---
        for (Map.Entry<UUID, List<UUID>> entry : pendingDivorce.entrySet()) {
            UUID priestId = entry.getKey();
            List<UUID> pair = entry.getValue();

            if (!pair.contains(pid)) continue;

            Player priest = Bukkit.getPlayer(priestId);
            if (priest == null) {
                pendingDivorce.remove(priestId);
                pendingDivorceAccepted.remove(priestId);
                return;
            }

            pendingDivorceAccepted.putIfAbsent(priestId, new HashSet<>());
            Set<UUID> accepted = pendingDivorceAccepted.get(priestId);
            accepted.add(pid);

            if (accepted.size() < 2) {
                p.sendMessage("§eВы подтвердили развод. Ожидается согласие второй стороны.");
                return;
            }

            UUID a = pair.get(0);
            UUID b = pair.get(1);
            Player pa = Bukkit.getPlayer(a);
            Player pb = Bukkit.getPlayer(b);
            if (pa == null || pb == null) {
                priest.sendMessage("§cКто-то из пары вышел с сервера.");
                pendingDivorce.remove(priestId);
                pendingDivorceAccepted.remove(priestId);
                return;
            }

            double cost = 50000;

            boolean freeA = pa.hasPermission("vaulteconomy.marry.free");
            boolean freeB = pb.hasPermission("vaulteconomy.marry.free");

            if (!freeA && economy.getBalance(pa) < cost) {
                priest.sendMessage("§cУ " + pa.getName() + " нет 50,000 для развода.");
                return;
            }
            if (!freeB && economy.getBalance(pb) < cost) {
                priest.sendMessage("§cУ " + pb.getName() + " нет 50,000 для развода.");
                return;
            }

            if (!freeA) economy.withdrawPlayer(pa, cost);
            if (!freeB) economy.withdrawPlayer(pb, cost);

            marriageManager.divorce(a, b);

            pa.sendMessage("§cВы разведены с " + pb.getName() + ".");
            pb.sendMessage("§cВы разведены с " + pa.getName() + ".");

            pendingDivorce.remove(priestId);
            pendingDivorceAccepted.remove(priestId);
            return;
        }

        p.sendMessage("§cНет активного запроса на свадьбу или развод.");
    }

    // ===== ЗАПРОС НА РАЗВОД =====

    private void handleDivorceRequest(Player priest, String brideName, String groomName) {
        Player bride = Bukkit.getPlayer(brideName);
        Player groom = Bukkit.getPlayer(groomName);

        if (bride == null || groom == null) {
            priest.sendMessage("§cОба игрока должны быть онлайн.");
            return;
        }

        UUID priestId = priest.getUniqueId();
        pendingDivorce.put(priestId, Arrays.asList(bride.getUniqueId(), groom.getUniqueId()));
        pendingDivorceAccepted.put(priestId, new HashSet<>());

        priest.sendMessage("§eЗапрос на развод отправлен. Пусть оба введут §f/marry accept§e.");
        bride.sendMessage("§dСвященник предлагает развод с §f" + groom.getName() + "§d. Введите §f/marry accept§d.");
        groom.sendMessage("§dСвященник предлагает развод с §f" + bride.getName() + "§d. Введите §f/marry accept§d.");
    }

    // ===== ПРОЧИЕ КОМАНДЫ ПАРЫ =====

    private void handleKiss(Player p) {
        UUID partnerId = marriageManager.getPartner(p.getUniqueId());
        if (partnerId == null) {
            p.sendMessage("§cУ вас нет партнёра.");
            return;
        }
        Player partner = Bukkit.getPlayer(partnerId);
        if (partner == null) {
            p.sendMessage("§cПартнёр не в сети.");
            return;
        }
        p.sendMessage("§dВы поцеловали " + partner.getName() + " ♥");
        partner.sendMessage("§d" + p.getName() + " поцеловал(а) вас ♥");
        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1.5f);
    }

    private void handleGift(Player p) {
        UUID partnerId = marriageManager.getPartner(p.getUniqueId());
        if (partnerId == null) {
            p.sendMessage("§cУ вас нет партнёра.");
            return;
        }
        Player partner = Bukkit.getPlayer(partnerId);
        if (partner == null) {
            p.sendMessage("§cПартнёр не в сети.");
            return;
        }

        if (p.getInventory().getItemInMainHand().getType().isAir()) {
            p.sendMessage("§cДержите предмет в руке.");
            return;
        }

        double cost = 50;
        boolean free = p.hasPermission("vaulteconomy.marry.free");

        if (!free && economy.getBalance(p) < cost) {
            p.sendMessage("§cНужно 50 для подарка.");
            return;
        }

        if (!free) {
            economy.withdrawPlayer(p, cost);
        }

        partner.getInventory().addItem(p.getInventory().getItemInMainHand().clone());
        p.getInventory().getItemInMainHand().setAmount(0);

        p.sendMessage("§aПодарок отправлен " + partner.getName());
        partner.sendMessage("§dВы получили подарок от " + p.getName());
    }

    private void handleTp(Player p) {
        UUID partnerId = marriageManager.getPartner(p.getUniqueId());
        if (partnerId == null) {
            p.sendMessage("§cУ вас нет партнёра.");
            return;
        }
        Player partner = Bukkit.getPlayer(partnerId);
        if (partner == null) {
            p.sendMessage("§cПартнёр не в сети.");
            return;
        }

        int used = marriageManager.getDailyTpUsed(p.getUniqueId());
        if (used >= 3) {
            p.sendMessage("§cВы уже использовали 3 телепорта сегодня.");
            return;
        }

        p.teleport(partner.getLocation());
        marriageManager.setDailyTpUsed(p.getUniqueId(), used + 1);
        p.sendMessage("§aТелепортация к партнёру. Использовано: " + (used + 1) + "/3.");
    }
}