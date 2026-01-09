package me.kodysimpson.vaulteconomy.chat.clancommand;

import me.kodysimpson.vaulteconomy.pvp.PvpManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class ClanManager {

    private final Map<String, Clan> clansByName = new HashMap<>();
    private final Map<UUID, Clan> clanByPlayer = new HashMap<>();
    private final Map<UUID, ClanMember> members = new HashMap<>();

    private final PvpManager pvpManager;

    public ClanManager(PvpManager pvpManager) {
        this.pvpManager = pvpManager;
    }

    public boolean canUseClanCommand(Player player) {
        if (pvpManager != null && pvpManager.isInPvp(player)) {
            player.sendMessage("§cНельзя использовать клановые команды во время PvP!");
            return false;
        }
        return true;
    }

    public Clan getClan(UUID player) {
        return clanByPlayer.get(player);
    }

    public boolean hasClan(UUID player) {
        return clanByPlayer.containsKey(player);
    }

    public Clan getClanByName(String name) {
        return clansByName.get(name.toLowerCase());
    }

    public ClanMember getMember(UUID uuid) {
        return members.get(uuid);
    }

    public ClanRole getRole(UUID uuid) {
        ClanMember member = members.get(uuid);
        return member != null ? member.getRole() : null;
    }

    public Clan createClan(String name, Player leader) {

        String plainName = ChatColor.stripColor(name); // позже можно stripColor

        Clan clan = new Clan(name, plainName, leader.getUniqueId());

        ClanMember leaderMember = new ClanMember(
                leader.getUniqueId(),
                ClanRole.LEADER
        );

        clansByName.put(plainName.toLowerCase(), clan);
        clanByPlayer.put(leader.getUniqueId(), clan);
        members.put(leader.getUniqueId(), leaderMember);

        return clan;
    }


    public boolean addMember(Clan clan, ClanMember member) {
        if (clan == null || member == null) return false;

        clan.addMember(member.getUuid());
        clanByPlayer.put(member.getUuid(), clan);
        members.put(member.getUuid(), member);
        return true;
    }

    public boolean removeMember(Clan clan, UUID uuid) {
        if (clan == null) return false;

        clan.removeMember(uuid);
        clanByPlayer.remove(uuid);
        members.remove(uuid);
        return true;
    }

    public void deleteClan(Clan clan) {
        if (clan == null) return;

        for (UUID member : clan.getMembers()) {
            clanByPlayer.remove(member);
            members.remove(member);
        }

        clansByName.remove(clan.getName().toLowerCase());
    }

    public Collection<Clan> getAllClans() {
        return Collections.unmodifiableCollection(clansByName.values());
    }

    public List<Clan> getTopByBalance(int limit) {
        return clansByName.values().stream()
                .sorted(Comparator.comparingDouble(Clan::getBalance).reversed())
                .limit(limit)
                .toList();
    }
}