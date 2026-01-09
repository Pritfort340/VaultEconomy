package me.kodysimpson.vaulteconomy.chat.clancommand;

import java.util.UUID;

public class ClanMember {

    // ===== ОСНОВНОЕ =====
    private final UUID uuid;
    private ClanRole role;

    // ===== НАСТРОЙКИ ИГРОКА =====
    private boolean clanChatEnabled;
    private boolean clanPrefixEnabled;

    // ===== СТАТИСТИКА =====
    private int kills;
    private int deaths;

    // ===== КОНСТРУКТОР =====
    public ClanMember(UUID uuid, ClanRole role) {
        this.uuid = uuid;
        this.role = role;

        this.clanChatEnabled = true;
        this.clanPrefixEnabled = true;

        this.kills = 0;
        this.deaths = 0;
    }

    // ===== UUID =====
    public UUID getUuid() {
        return uuid;
    }

    // ===== РОЛЬ =====
    public ClanRole getRole() {
        return role;
    }

    public void setRole(ClanRole role) {
        this.role = role;
    }

    public boolean isLeader() {
        return role == ClanRole.LEADER;
    }

    public boolean isOfficer() {
        return role == ClanRole.OFFICER || role == ClanRole.LEADER;
    }

    // ===== КЛАН ЧАТ =====
    public boolean isClanChatEnabled() {
        return clanChatEnabled;
    }

    public void setClanChatEnabled(boolean enabled) {
        this.clanChatEnabled = enabled;
    }

    // ===== ПРЕФИКС В ЧАТЕ =====
    public boolean isClanPrefixEnabled() {
        return clanPrefixEnabled;
    }

    public void setClanPrefixEnabled(boolean enabled) {
        this.clanPrefixEnabled = enabled;
    }

    // ===== СТАТИСТИКА =====
    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addKill() {
        kills++;
    }

    public void addDeath() {
        deaths++;
    }

    public double getKdr() {
        return deaths == 0 ? kills : (double) kills / deaths;
    }
}