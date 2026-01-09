package me.kodysimpson.vaulteconomy.chat.clancommand;

import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Управляет отображением кланового префикса в чате
 */
public class ClanPrefixManager {

    private final ClanManager clanManager;
    private final PvpManager pvpManager;

    /**
     * true  = показывать префикс
     * false = скрыть префикс
     */
    private final Map<UUID, Boolean> prefixToggle = new HashMap<>();

    public ClanPrefixManager(ClanManager clanManager, PvpManager pvpManager) {
        this.clanManager = clanManager;
        this.pvpManager = pvpManager;
    }

    // =====================================================
    // ================= PvP BLOCK =========================
    // =====================================================

    private boolean canUse(Player player) {
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя менять клановый префикс во время PvP!");
            return false;
        }
        return true;
    }

    // =====================================================
    // ================= TOGGLE ============================
    // =====================================================

    public void toggle(Player player, boolean enabled) {
        if (!canUse(player)) return;

        if (!clanManager.hasClan(player.getUniqueId())) {
            player.sendMessage("§cВы не состоите в клане.");
            return;
        }

        prefixToggle.put(player.getUniqueId(), enabled);

        if (enabled) {
            player.sendMessage("§aОтображение кланового префикса включено.");
        } else {
            player.sendMessage("§cОтображение кланового префикса выключено.");
        }
    }

    // =====================================================
    // ================= GET PREFIX ========================
    // =====================================================

    /**
     * Возвращает клановый префикс для ChatListener
     */
    public String getPrefix(Player player) {

        // ❌ если не в клане — ничего
        Clan clan = clanManager.getClan(player.getUniqueId());
        if (clan == null) {
            return "";
        }

        // ❌ если игрок отключил
        boolean enabled = prefixToggle.getOrDefault(player.getUniqueId(), true);
        if (!enabled) {
            return "";
        }

        return clan.getChatPrefix() + " ";
    }

    // =====================================================
    // ================= UTIL ==============================
    // =====================================================

    public boolean isPrefixEnabled(UUID uuid) {
        return prefixToggle.getOrDefault(uuid, true);
    }

    public void setPrefixEnabled(UUID uuid, boolean enabled) {
        prefixToggle.put(uuid, enabled);
    }
}