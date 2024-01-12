package pl.nbd;

import pl.nbd.entities.*;
import pl.nbd.mappers.MongoUUID;
import pl.nbd.repositories.ClientRepository;
import pl.nbd.repositories.RentRepository;
import pl.nbd.repositories.RoomRepository;
import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

public class RentRepoTest {

    private static Client client;
    private static Room room;
    private static Rent rent;

    private static RentRepository rentRepository;
    private static ClientRepository clientRepository;
    private static RoomRepository roomRepository;

    @BeforeEach
    public void clearData() {
        rentRepository = new RentRepository();
        rentRepository.dropRentCollection();
        clientRepository = new ClientRepository();
        clientRepository.dropClientCollection();
        roomRepository = new RoomRepository();
        roomRepository.dropRoomCollection();
        client = new PremiumClient(new MongoUUID(UUID.randomUUID()), "John", "Doe", "666", 2, 0.95);
        room = new Room(new MongoUUID(UUID.randomUUID()), 8, 2, 100);
        rent = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), client, room);
    }


    @Test
    public void insertRentTest() {
        rentRepository.insertRent(rent);
        Assertions.assertEquals(rentRepository.readAllRents().size(), 1);
    }

    @Test
    public void readAllRentsTest() {
        rentRepository.insertRent(rent);
        PremiumClient client2 = new PremiumClient(new MongoUUID(UUID.randomUUID()), "John", "Doe", "666", 2, 0.95);
        Room room2 = new Room(new MongoUUID(UUID.randomUUID()), 1, 2, 100);
        Rent rent2 = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), client2, room2);
        rentRepository.insertRent(rent2);
        Assertions.assertEquals(rentRepository.readAllRents().size(), 2);
    }

    @Test
    public void readRentsByClientTest() {
        rentRepository.insertRent(rent);
        PremiumClient client2 = new PremiumClient(new MongoUUID(UUID.randomUUID()), "John", "Doe", "666", 2, 0.1);
        Room room2 = new Room(new MongoUUID(UUID.randomUUID()), 1, 2, 100);
        Rent rent2 = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), client2, room2);
        rentRepository.insertRent(rent2);
        Assertions.assertEquals(rentRepository.readRentsbyClient(client).size(), 1);
    }

    @Test
    public void readRentsByRoomTest() {
        rentRepository.insertRent(rent);
        PremiumClient client2 = new PremiumClient(new MongoUUID(UUID.randomUUID()), "John", "Doe", "666", 2, 0.1);
        Room room2 = new Room(new MongoUUID(UUID.randomUUID()), 1, 2, 100);
        Rent rent2 = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), client2, room2);
        rentRepository.insertRent(rent2);
        Assertions.assertEquals(rentRepository.readRentsbyRoom(room).size(), 1);
    }

    @Test
    public void dropRentCollectionTest() {
        rentRepository.insertRent(rent);
        rentRepository.dropRentCollection();
        Assertions.assertEquals(rentRepository.readAllRents().size(), 0);
    }

    @Test
    public void updateEndRentDateTest() {
        rentRepository.insertRent(rent);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        rent.endRent(calendar);
        rentRepository.updateEndRentDate(rent);
        Assertions.assertEquals(rentRepository.readAllRents().get(0).getRentEndDate(), calendar);
    }

    @Test
    public void updateEndRentCostTest() {
        rentRepository.insertRent(rent);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        rent.endRent(calendar);
        rentRepository.updateEndRentDate(rent);
        Assertions.assertEquals(rentRepository.readAllRents().get(0).getRentCost(), 150);
    }

    @Test
    public void insertRentWithRoomAlreadyRentedTest() {
        Rent rentSameRoom = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), client, room);
        rentRepository.insertRent(rentSameRoom);
        Assertions.assertThrows(IllegalStateException.class, () -> rentRepository.insertRent(rent));
    }
    
    @Test
    public void insertRentWithRoomNotInDatabaseTest() {
        Room roomNotInDatabase = new Room(new MongoUUID(UUID.randomUUID()), 1, 2, 100);
        Rent rentNotInDatabase = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), client, roomNotInDatabase);
        rentRepository.insertRent(rentNotInDatabase);
        Assertions.assertEquals(rentRepository.readAllRents().size(), 1);
        Assertions.assertEquals(roomRepository.readAllRooms().size(), 1);
    }

    @Test
    public void insertRentWithClientNotInDatabaseTest() {
        Client clientNotInDatabase = new PremiumClient(new MongoUUID(UUID.randomUUID()), "John", "Doe", "666", 2, 0.1);
        Rent rentNotInDatabase = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), clientNotInDatabase, room);
        rentRepository.insertRent(rentNotInDatabase);
        Assertions.assertEquals(rentRepository.readAllRents().size(), 1);
        Assertions.assertEquals(clientRepository.readAllClients().size(), 1);
    }

    @Test
    public void insertRentWithClientAndRoomNotInDatabaseTest() {
        Client clientNotInDatabase = new PremiumClient(new MongoUUID(UUID.randomUUID()), "John", "Doe", "666", 2, 0.1);
        Room roomNotInDatabase = new Room(new MongoUUID(UUID.randomUUID()), 1, 2, 100);
        Rent rentNotInDatabase = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), clientNotInDatabase, roomNotInDatabase);
        rentRepository.insertRent(rentNotInDatabase);
        Assertions.assertEquals(rentRepository.readAllRents().size(), 1);
        Assertions.assertEquals(clientRepository.readAllClients().size(), 1);
        Assertions.assertEquals(roomRepository.readAllRooms().size(), 1);
    }

    @Test
    public void insertRentWithClientAndRoomInDatabaseTest() {
        clientRepository.insertClient(client);
        roomRepository.insertRoom(room);
        rentRepository.insertRent(rent);
        Assertions.assertEquals(rentRepository.readAllRents().size(), 1);
        Assertions.assertEquals(clientRepository.readAllClients().size(), 1);
        Assertions.assertEquals(roomRepository.readAllRooms().size(), 1);
    }

    @Test
    public void insertRentWithTheSameClientInDatabase() {
        clientRepository.insertClient(client);
        rentRepository.insertRent(rent);
        Assertions.assertEquals(rentRepository.readAllRents().size(), 1);
        Assertions.assertEquals(clientRepository.readAllClients().size(), 1);
        Assertions.assertEquals(roomRepository.readAllRooms().size(), 1);
        Room roomNotInDatabase = new Room(new MongoUUID(UUID.randomUUID()), 1, 2, 100);
        Rent rentSameClient = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), client, roomNotInDatabase);
        rentRepository.insertRent(rentSameClient);
        Assertions.assertEquals(clientRepository.readAllClients().size(), 1);
        Assertions.assertEquals(roomRepository.readAllRooms().size(), 2);
    }
}
