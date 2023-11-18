package pl.nbd;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.nbd.entities.Default;
import pl.nbd.entities.Room;
import pl.nbd.mappers.MongoUUID;
import pl.nbd.repositories.RoomRepository;

import java.util.UUID;

public class RedisTest {
    RoomRepository roomRepository = new RoomRepository();
    Jsonb jsonb = JsonbBuilder.create();
    Room room = new Room(new MongoUUID(UUID.randomUUID()),666, 31, 12);

    @Test
    public void insertRedisRoom() {
        roomRepository.insertRoom(room);
        Assertions.assertEquals(roomRepository.readRoomByRoomNumber(666).getRoomNumber(), 666);
        Assertions.assertEquals(roomRepository.readRoomByRoomNumberFromRedis(666), jsonb.toJson(room));

    }
}
