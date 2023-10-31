package pl.nbd;

import pl.nbd.entities.Room;
import pl.nbd.repositories.RoomRepository;

import java.util.List;

public class App {
    public static void main(String[] args) {
        try (RoomRepository roomRepository = new RoomRepository()) {
            roomRepository.insertRoom(new Room(1, 1, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try (RoomRepository roomRepository = new RoomRepository()) {
            List<Room> list =  roomRepository.readAllRooms();
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
