package pl.nbd;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.Test;
import pl.nbd.entities.Default;
import pl.nbd.mappers.MongoUUID;

import java.util.UUID;

public class RedisTest {
    Jsonb jsonb = JsonbBuilder.create();

    @Test
    public void test() {
        Default defaultClient = new Default(new MongoUUID(UUID.randomUUID()), "Jan", "Kowalski", "123456789");
        System.out.println(defaultClient);
        String json = jsonb.toJson(defaultClient);
        System.out.println(json);
        Default defaultClient1 = jsonb.fromJson(json, Default.class);
        System.out.println(defaultClient1);
    }
}
