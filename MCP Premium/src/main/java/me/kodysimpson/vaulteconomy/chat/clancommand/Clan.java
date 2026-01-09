package me.kodysimpson.vaulteconomy.chat.clancommand;

import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Clan {

    /* ===================== ОСНОВНЫЕ ДАННЫЕ ===================== */
    private final String name;       // цветное имя
    private final String plainName;  // имя без цветов
    private UUID leader;

    /* ===================== УЧАСТНИКИ ========================== */
    private final Set<UUID> members = new HashSet<>();

    /* ===================== ЭКОНОМИКА ========================== */
    private double balance;

    /* ===================== НАСТРОЙКИ ========================== */
    private boolean pvpEnabled;
    private boolean openJoin;
    private boolean friendlyFire;
    private ItemStack[] storage = new ItemStack[9];

    /* ===================== ВИЗУАЛ ============================= */
    private ItemStack banner;
    private String chatPrefix;
    private String badge;
    private ItemStack[] storageContents;


    /* ===================== ЛОКАЦИИ ============================ */
    private Location home;

    /* ===================== UPGRADES =========================== */
    private int storageLevel = 1;

    /* ===================== WARS =============================== */
    private final Set<String> wars = new HashSet<>();

    /* ===================== ALLIES ========================== */
    private final Set<String> allies = new HashSet<>();

    /* ===================== КОНСТРУКТОР ======================== */
    public Clan(String name, String plainName, UUID leader) {
        this.name = name;
        this.plainName = plainName;
        this.leader = leader;

        this.members.add(leader);

        this.balance = 0.0;
        this.pvpEnabled = false;
        this.openJoin = true;
        this.friendlyFire = false;

        this.chatPrefix = "&8[" + name + "&8]";
        this.badge = "★";
    }

    /* ===================== PvP BLOCK ========================== */
    public boolean canUseClanCommands(Player player, PvpManager pvpManager) {
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя использовать клановые команды во время PvP!");
            return false;
        }
        return true;
    }

    /* ===================== ИМЕНА ============================== */
    public String getName() {
        return name;
    }

    public String getPlainName() {
        return plainName;
    }

    /* ===================== ЛИДЕР ============================== */
    public UUID getLeader() {
        return leader;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    /* ===================== УЧАСТНИКИ ========================== */
    public Set<UUID> getMembers() {
        return members;
    }

    public boolean isMember(UUID uuid) {
        return members.contains(uuid);
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }

    /* ===================== БАЛАНС ============================= */
    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount) {
        if (amount > 0) balance += amount;
    }

    public boolean removeBalance(double amount) {
        if (amount <= 0 || balance < amount) return false;
        balance -= amount;
        return true;
    }

    /* ===================== HOME ================================ */
    public boolean hasHome() {
        return home != null;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    /* ===================== PvP ================================ */
    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    public void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    /* ===================== JOIN =============================== */
    public boolean isOpenJoin() {
        return openJoin;
    }

    public void setOpenJoin(boolean openJoin) {
        this.openJoin = openJoin;
    }

    /* ===================== FRIENDLY FIRE ====================== */
    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    /* ===================== ВИЗУАЛ ============================= */
    public String getChatPrefix() {
        return chatPrefix;
    }

    public void setChatPrefix(String chatPrefix) {
        this.chatPrefix = chatPrefix;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public ItemStack getBanner() {
        return banner;
    }

    public void setBanner(ItemStack banner) {
        this.banner = banner;
    }

    // ================= STORAGE =================

    public int getStorageLevel() {
        return storageLevel;
    }

    public void upgradeStorage() {
        storageLevel++;
    }

    public int getStorageSize() {
        return Math.min(54, 27 + (storageLevel - 1) * 9);
    }

    public ItemStack[] getStorageContents() {
        return storageContents;
    }

    public void setStorageContents(ItemStack[] contents) {
        this.storageContents = contents;
    }
    /* ===================== WARS =============================== */
    public boolean isAtWarWith(Clan clan) {
        return wars.contains(clan.getPlainName().toLowerCase());
    }

    public void addWar(Clan clan) {
        wars.add(clan.getPlainName().toLowerCase());
    }

    public void removeWar(Clan clan) {
        wars.remove(clan.getPlainName().toLowerCase());
    }

    public Set<String> getWars() {
        return wars;
    }

    /* ===================== ALLIES ========================== */

    public boolean isAlliedWith(Clan clan) {
        if (clan == null) return false;
        return allies.contains(clan.getPlainName().toLowerCase());
    }

    public void addAlly(Clan clan) {
        if (clan == null) return;
        allies.add(clan.getPlainName().toLowerCase());
    }

    public void removeAlly(Clan clan) {
        if (clan == null) return;
        allies.remove(clan.getPlainName().toLowerCase());
    }

    public Set<String> getAllies() {
        return allies;
    }

    public ItemStack[] getStorage() {
        return storage;
    }

    public void resizeStorage(int newSize) {
        ItemStack[] newStorage = new ItemStack[newSize];
        System.arraycopy(storage, 0, newStorage, 0, Math.min(storage.length, newSize));
        storage = newStorage;
    }
}