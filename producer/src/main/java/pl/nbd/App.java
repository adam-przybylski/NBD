package pl.nbd;

import pl.nbd.entities.PremiumClient;
import pl.nbd.entities.Rent;
import pl.nbd.entities.Room;
import pl.nbd.mappers.MongoUUID;
import pl.nbd.mappers.ClientCodec;
import pl.nbd.mappers.ClientProvider;
import pl.nbd.repositories.ClientRepository;
import pl.nbd.repositories.RentRepository;
import pl.nbd.repositories.RoomRepository;

import java.util.GregorianCalendar;
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

        try (RentRepository rentRepository = new RentRepository();
             ClientRepository clientRepository = new ClientRepository();
             RoomRepository roomRepository = new RoomRepository()) {
            PremiumClient premiumClient = new PremiumClient(new MongoUUID(UUID.randomUUID()), "Jan", "Kowalski", "123456789", 2, 0.2);
            Room room1 = new Room(new MongoUUID(UUID.randomUUID()), 1, 2, 100);
            Rent rent1 = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), premiumClient, room1);
            clientRepository.insertClient(premiumClient);
            roomRepository.insertRoom(room1);
            rentRepository.insertRent(rent1);
            List<Rent> rents = rentRepository.readAllRents();
            PremiumClient client = (PremiumClient) rents.get(0).getClient();
            System.out.println(client);
            for (Rent rent : rents) {
                System.out.println(rent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
