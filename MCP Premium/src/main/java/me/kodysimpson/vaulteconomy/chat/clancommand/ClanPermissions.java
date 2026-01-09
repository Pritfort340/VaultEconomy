package me.kodysimpson.vaulteconomy.chat.clancommand;

/**
 * Все permission-узлы клановой системы
 */
public final class ClanPermissions {

    private ClanPermissions() {
        // util class
    }

    // =====================================================
    // ================= ОСНОВНЫЕ ==========================
    // =====================================================

    public static final String ADMIN = "clans.admin";

    // =====================================================
    // ================= КОМАНДЫ ===========================
    // =====================================================

    public static final String CREATE = "clans.create";
    public static final String DELETE = "clans.delete";
    public static final String INFO = "clans.info";
    public static final String LEAVE = "clans.leave";

    // =====================================================
    // ================= УЧАСТНИКИ =========================
    // =====================================================

    public static final String INVITE = "clans.invite";
    public static final String KICK = "clans.kick";
    public static final String PROMOTE = "clans.promote";
    public static final String DEMOTE = "clans.demote";
    public static final String TRANSFER = "clans.transfer";

    // =====================================================
    // ================= ЗАЯВКИ ============================
    // =====================================================

    public static final String REQUEST_ACCEPT = "clans.request.accept";
    public static final String REQUEST_DENY = "clans.request.deny";
    public static final String REQUEST_VIEW = "clans.request.view";
    public static final String TOGGLE_JOIN = "clans.togglejoin";

    // =====================================================
    // ================= ЧАТ ===============================
    // =====================================================

    public static final String CHAT = "clans.chat";
    public static final String PREFIX_TOGGLE = "clans.prefix.toggle";

    // =====================================================
    // ================= ЭКОНОМИКА =========================
    // =====================================================

    public static final String DEPOSIT = "clans.deposit";
    public static final String WITHDRAW = "clans.withdraw";
    public static final String BALANCE = "clans.balance";

    // =====================================================
    // ================= ХРАНИЛИЩЕ =========================
    // =====================================================

    public static final String STORAGE_OPEN = "clans.storage.open";
    public static final String STORAGE_UPGRADE = "clans.storage.upgrade";

    // =====================================================
    // ================= ВОЙНЫ / СОЮЗЫ =====================
    // =====================================================

    public static final String WAR = "clans.war";
    public static final String ALLY = "clans.ally";
    public static final String UNALLY = "clans.unally";
    public static final String ENEMY = "clans.enemy";
    public static final String UNENEMY = "clans.unenemy";

    // =====================================================
    // ================= НАСТРОЙКИ =========================
    // =====================================================

    public static final String SET_HOME = "clans.sethome";
    public static final String HOME = "clans.home";
    public static final String SET_BANNER = "clans.setbanner";
    public static final String GET_BANNER = "clans.getbanner";
    public static final String CHANGE_PREFIX = "clans.change";

    // =====================================================
    // ================= ОБНОВЛЕНИЯ ========================
    // =====================================================

    public static final String RELOAD = "clans.reload";
    public static final String UPDATE = "clans.update";
    public static final String UPDATE_CONFIG = "clans.updateconf";
}