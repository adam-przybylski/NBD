package pl.nbd.entities;

import java.util.UUID;

public class MongoUUID {

    private UUID uuid;

    public MongoUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return uuid.toString();
    }
}
