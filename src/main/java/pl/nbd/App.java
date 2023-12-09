package pl.nbd;


import pl.nbd.dao.RoomDao;
import pl.nbd.entities.Room;
import pl.nbd.mappers.RoomMapper;
import pl.nbd.mappers.RoomMapperBuilder;
import pl.nbd.repositories.RoomRepository;

import java.util.UUID;

public class App {
    public static void main(String[] args) {
        RoomRepository roomRepository1 = new RoomRepository();
        Room room = new Room(UUID.randomUUID(), 2, 2, 3);
        RoomMapper roomMapper = new RoomMapperBuilder(roomRepository1.getSession()).build();
        RoomDao roomDao = roomMapper.roomDao();

        System.out.println(roomDao.create(room));
//        System.out.println(roomDao.delete(room));

//        System.out.println(roomDao.findById(UUID.fromString("a8ad9127-55f2-4d3b-be6e-00a574eeaa17")));
        Room roomUpdated = new Room(UUID.fromString("a8ad9127-55f2-4d3b-be6e-00a574eeaa17"), 2, 2, 90);
        System.out.println(roomDao.update(roomUpdated, room.getId(), room.getRoomCapacity()));
    }
}
