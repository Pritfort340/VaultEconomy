package me.kodysimpson.vaulteconomy.modules.teleport;

import java.util.UUID;

public class TeleportRequest {

    public enum Type {
        TPA,      // requester -> target
        TPAHERE   // target -> requester
    }

    private final UUID requester;
    private final UUID target;
    private final Type type;

    public TeleportRequest(UUID requester, UUID target, Type type) {
        this.requester = requester;
        this.target = target;
        this.type = type;
    }

    public UUID getRequester() {
        return requester;
    }

    public UUID getTarget() {
        return target;
    }

    public Type getType() {
        return type;
    }
}