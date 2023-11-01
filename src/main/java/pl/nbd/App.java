package pl.nbd;

import pl.nbd.entities.Client;
import pl.nbd.entities.PremiumClient;
import pl.nbd.entities.RegularClient;
import pl.nbd.entities.Room;
import pl.nbd.mappers.MongoUUID;
import pl.nbd.repositories.ClientRepository;
import pl.nbd.repositories.RoomRepository;

import java.util.List;
import java.util.UUID;

public class App {
    public static void main(String[] args) {
//        try (RoomRepository roomRepository = new RoomRepository()) {
//            Room room1 = new Room(new MongoUUID(UUID.randomUUID()), 1, 2, 100);
//            roomRepository.insertRoom(room1);
//            Room room2 = new Room(new MongoUUID(UUID.randomUUID()),2, 3, 150);
//            roomRepository.insertRoom(room2);
//            roomRepository.updateRoomPrice(1, 200);
//            roomRepository.deleteRoom(2);
//            List<Room> rooms = roomRepository.readAllRooms();
//            for (Room room : rooms) {
//                System.out.println(room);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try (ClientRepository clientRepository = new ClientRepository()) {
            PremiumClient premiumClient = new PremiumClient(new MongoUUID(UUID.randomUUID()), "Jan", "Kowalski", "123456789", 2, 0.5);
            clientRepository.insertClient(premiumClient);
            RegularClient regularClient = new RegularClient(new MongoUUID(UUID.randomUUID()), "Jan", "Kowalski", "123", 2);
            clientRepository.insertClient(regularClient);
            clientRepository.updateClient("123456789", "Nowak");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
