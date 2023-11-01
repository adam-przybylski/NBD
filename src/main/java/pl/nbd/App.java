package pl.nbd;

import pl.nbd.entities.MongoUUID;
import pl.nbd.entities.Room;
import pl.nbd.repositories.RoomRepository;

import java.util.List;
import java.util.UUID;

public class App {
    public static void main(String[] args) {
        try (RoomRepository roomRepository = new RoomRepository()) {
            Room room = new Room(new MongoUUID(UUID.randomUUID()), 1, 1, 100);
            System.out.println(room);
            roomRepository.insertRoom(room);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (RoomRepository roomRepository = new RoomRepository()) {
            List<Room> list = roomRepository.readAllRooms();
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
