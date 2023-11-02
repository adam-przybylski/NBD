package pl.nbd;

import pl.nbd.entities.Room;
import pl.nbd.mappers.MongoUUID;
import pl.nbd.repositories.RoomRepository;
import org.junit.jupiter.api.*;

import java.util.UUID;


public class RoomRepoTest {
    private static Room room;
    private static RoomRepository roomRepository;
    @BeforeEach
    public void clearData() {
        roomRepository = new RoomRepository();
        roomRepository.dropRoomCollection();
        room = new Room(new MongoUUID(UUID.randomUUID()),19, 31, 12);
    }

    @Test
    public void insertRoomTest() {
        roomRepository.insertRoom(room);
        Assertions.assertEquals(roomRepository.readAllRooms().size(), 1);
    }

    @Test
    public void readAllRoomsTest() {
        roomRepository.insertRoom(room);
        Room room2 = new Room(new MongoUUID(UUID.randomUUID()), 20, 32, 13);
        roomRepository.insertRoom(room2);
        Assertions.assertEquals(roomRepository.readAllRooms().size(), 2);
    }

    @Test
    public void updateRoomPriceTest() {
        roomRepository.insertRoom(room);
        roomRepository.updateRoomPrice(19, 100);
        Assertions.assertEquals(roomRepository.readAllRooms().get(0).getBasePrice(), 100);
    }

    @Test
    public void deleteRoomTest() {
        roomRepository.insertRoom(room);
        roomRepository.deleteRoom(19);
        Assertions.assertEquals(roomRepository.readAllRooms().size(), 0);
    }


    @Test
    public void dropRoomCollectionTest() {
        roomRepository.insertRoom(room);
        roomRepository.dropRoomCollection();
        Assertions.assertEquals(roomRepository.readAllRooms().size(), 0);
    }

}
