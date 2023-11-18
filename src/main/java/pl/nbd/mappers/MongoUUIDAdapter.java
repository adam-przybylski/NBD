package pl.nbd.mappers;

import pl.nbd.mappers.MongoUUID;
import jakarta.json.bind.adapter.JsonbAdapter;

import java.util.UUID;

public class MongoUUIDAdapter implements JsonbAdapter<MongoUUID, String> {
    @Override
    public String adaptToJson(MongoUUID mongoUUID) throws Exception {
        return mongoUUID != null ? mongoUUID.getUuid().toString() : null;
    }

    @Override
    public MongoUUID adaptFromJson(String s) throws Exception {
        return new MongoUUID(UUID.fromString(s));
    }
}
