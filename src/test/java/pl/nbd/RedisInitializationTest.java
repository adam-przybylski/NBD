package pl.nbd;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.nbd.entities.Room;
import pl.nbd.mappers.MongoUUID;
import pl.nbd.mappers.MongoUUIDAdapter;
import pl.nbd.repositories.RoomRepository;

import java.util.UUID;

public class RedisInitializationTest {

    private class TestRoomRepository extends RoomRepository {
        public TestRoomRepository() {
            super();
            var collection = db.getCollection("rooms", Room.class);
            collection.insertOne(new Room(new MongoUUID(UUID.randomUUID()), 666, 31, 12));
        }

    }

    @Test
    public void DataInsertionTest() {
        TestRoomRepository testRoomRepository = new TestRoomRepository();
        RoomRepository roomRepository = new RoomRepository();
        // After calling constructor i should have one room in redis
        // The same one I inserted in constructor of TestRoomRepository (MongoDB)
        Assertions.assertEquals(roomRepository.readRoomByRoomNumberFromRedis(666).getRoomCapacity(), 31);
    }
}
