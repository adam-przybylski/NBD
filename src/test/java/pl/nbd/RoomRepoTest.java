package pl.nbd;

import pl.nbd.dao.RoomDao;
import pl.nbd.entities.Room;

import pl.nbd.mappers.RoomMapper;
import pl.nbd.mappers.RoomMapperBuilder;
import pl.nbd.repositories.RoomRepository;
import org.junit.jupiter.api.*;

import java.util.UUID;


public class RoomRepoTest {
    private static Room room;
    private static RoomRepository roomRepository;

    private static RoomDao roomDao;

    private static RoomMapper roomMapper;

    @BeforeEach
    public void setup() {
        roomRepository = new RoomRepository();
        room = new Room(UUID.randomUUID(), 2, 2, 3);
        roomMapper = new RoomMapperBuilder(roomRepository.getSession()).build();
         roomDao = roomMapper.roomDao();
    }

    @Test
    public void insertRoomTest() {
        roomDao.create(room);
        Room room2 = roomDao.findById(room.getId());
        Assertions.assertEquals(room, room2);
    }


    @Test
    public void updateRoomPriceTest() {
        roomDao.create(room);
        room.setBasePrice(100);
        roomDao.update(room, room.getId(), room.getRoomCapacity());
        Room roomUpdated = roomDao.findById(room.getId());
        Assertions.assertEquals(roomUpdated.getBasePrice(), 100);
    }

    @Test
    public void deleteRoomTest() {
        roomDao.create(room);

        roomDao.delete(room);
        Room room2 = roomDao.findById(room.getId());
        Assertions.assertNull(room2);
    }



}
