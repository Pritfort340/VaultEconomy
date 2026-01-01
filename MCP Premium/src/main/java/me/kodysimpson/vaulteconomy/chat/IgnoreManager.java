package me.kodysimpson.vaulteconomy.chat;

import java.util.*;

public class IgnoreManager {

    // Ключ – UUID того, кто игнорирует; значение – множество UUID, кого он игнорирует
    private final Map<UUID, Set<UUID>> ignores = new HashMap<>();

    public void setIgnore(UUID owner, UUID target, boolean ignore){
        ignores.putIfAbsent(owner, new HashSet<>());

        if (ignore){
            ignores.get(owner).add(target);
        }else{
            ignores.get(owner).remove(target);
        }
    }

    public boolean isIgnoring(UUID owner, UUID target){
        return ignores.getOrDefault(owner, Collections.emptySet()).contains(target);
    }
}