package pl.nbd;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.nbd.entities.*;
import pl.nbd.mappers.MongoUUID;
import pl.nbd.repositories.ClientRepository;
import pl.nbd.repositories.RentRepository;
import pl.nbd.repositories.RoomRepository;

import java.util.*;

public class KafkaTest {

    private static Client client1;
    private static Room room1;
    private static Rent rent1;

    private static Client client2;
    private static Room room2;
    private static Rent rent2;

    private static Client client3;
    private static Room room3;
    private static Rent rent3;

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

        client1 = new PremiumClient(new MongoUUID(UUID.randomUUID()), "John", "Doe", "666", 2, 0.95);
        room1 = new Room(new MongoUUID(UUID.randomUUID()), 8, 2, 100);
        rent1 = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), client1, room1);

        client2 = new Default(new MongoUUID(UUID.randomUUID()), "John", "Doe", "666");
        room2 = new Room(new MongoUUID(UUID.randomUUID()), 1, 2, 100);
        rent2 = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), client2, room2);

        client3 = new PremiumClient(new MongoUUID(UUID.randomUUID()), "John", "Doe", "666", 2, 0.95);
        room3 = new Room(new MongoUUID(UUID.randomUUID()), 3, 2, 100);
        rent3 = new Rent(new MongoUUID(UUID.randomUUID()), new GregorianCalendar(), client3, room3);
    }

    @Test
    public void sendRents(){
        rentRepository.insertRent(rent1);
        rentRepository.insertRent(rent2);
        rentRepository.insertRent(rent3);
    }

}
