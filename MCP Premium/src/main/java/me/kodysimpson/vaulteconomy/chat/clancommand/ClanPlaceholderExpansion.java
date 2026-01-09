package me.kodysimpson.vaulteconomy.chat.clancommand;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.kodysimpson.vaulteconomy.VaultEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClanPlaceholderExpansion extends PlaceholderExpansion {

    private final ClanManager clanManager;
    private final Economy economy;

    public ClanPlaceholderExpansion(ClanManager clanManager) {
        this.clanManager = clanManager;
        this.economy = VaultEconomy.getInstance().getEconomy();
    }

    // =====================================================
    // ================= META ==============================
    // =====================================================

    @Override
    public @NotNull String getIdentifier() {
        return "clans";
    }

    @Override
    public @NotNull String getAuthor() {
        return "VaultEconomy";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    // =====================================================
    // ================= PLACEHOLDERS ======================
    // =====================================================

    @Override
    public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {

        if (player == null) return "";

        Clan clan = clanManager.getClan(player.getUniqueId());
        if (clan == null) return "";

        // %clans_name%
        if (params.equalsIgnoreCase("name")) {
            return clan.getName();
        }

        // %clans_name_plain%
        if (params.equalsIgnoreCase("name_plain")) {
            return clan.getPlainName();
        }

        // %clans_name_cm%
        if (params.equalsIgnoreCase("name_cm")) {
            return clan.getName();
        }

        // %clans_badge%
        if (params.equalsIgnoreCase("badge")) {
            return clan.getBadge();
        }

        // %clans_balance%
        if (params.equalsIgnoreCase("balance")) {
            return economy.format(clan.getBalance());
        }

        // =================================================
        // ТОП КЛАНОВ
        // %clans_list_<#>_name%
        // %clans_list_<#>_leader%
        // %clans_list_<#>_balance%
        // %clans_list_<#>_kdr%
        // =================================================
        if (params.startsWith("list_")) {

            String[] parts = params.split("_");
            if (parts.length < 3) return "";

            int index;
            try {
                index = Integer.parseInt(parts[1]) - 1;
            } catch (NumberFormatException e) {
                return "";
            }

            String type = parts[2];

            List<Clan> top = clanManager.getTopByBalance(index + 1);
            if (index < 0 || index >= top.size()) return "";

            Clan topClan = top.get(index);

            return switch (type.toLowerCase()) {
                case "name" -> topClan.getName();
                case "leader" -> topClan.getLeader().toString();
                case "balance" -> economy.format(topClan.getBalance());
                case "kdr" -> "0.0"; // позже подключим статистику
                default -> "";
            };
        }

        return "";
    }
}