package pl.nbd;

import pl.nbd.entities.*;
import pl.nbd.mappers.MongoUUID;
import pl.nbd.repositories.ClientRepository;
import org.junit.jupiter.api.*;
import pl.nbd.repositories.RoomRepository;

import java.util.UUID;


public class ClientRepoTest {

    private static Client client1;
    private static Client client2;
    private static Client client3;
    private static ClientRepository clientRepository;

    @BeforeEach
    public void clearData() {
        clientRepository = new ClientRepository();
        clientRepository.dropClientCollection();
        client1 = new RegularClient(new MongoUUID(UUID.randomUUID()),"Jan", "Kowalski", "12345678901", 0.1);
        client2 = new Default(new MongoUUID(UUID.randomUUID()),"Jan", "Nowak", "312214");
        client3 = new PremiumClient(new MongoUUID(UUID.randomUUID()),"John", "Doe", "666", 2, 0.1);
    }

    @Test
    public void insertClientTest() {
        clientRepository.insertClient(client1);
        clientRepository.insertClient(client2);
        clientRepository.insertClient(client3);
        Assertions.assertEquals(clientRepository.readAllClients().size(), 3);
    }

    @Test
    public void readAllClientsTest() {
        clientRepository.insertClient(client1);
        Assertions.assertEquals(clientRepository.readAllClients().size(), 1);
    }

    @Test
    public void readRegularClientsTest() {
        clientRepository.insertClient(client1);
        clientRepository.insertClient(client2);
        clientRepository.insertClient(client3);
        Assertions.assertEquals(clientRepository.readRegularClients().size(), 1);
    }

    @Test
    public void readDefaultClientsTest() {
        clientRepository.insertClient(client1);
        clientRepository.insertClient(client2);
        clientRepository.insertClient(client3);
        Assertions.assertEquals(clientRepository.readDefaultClients().size(), 1);
    }

    @Test
    public void readPremiumClientsTest() {
        clientRepository.insertClient(client1);
        clientRepository.insertClient(client2);
        clientRepository.insertClient(client3);
        Assertions.assertEquals(clientRepository.readPremiumClients().size(), 1);
    }

    @Test
    public void updateClientTest() {
        clientRepository.insertClient(client1);
        clientRepository.updateClient("12345678901", "Nowak");
        Assertions.assertEquals(clientRepository.readAllClients().get(0).getLastName(), "Nowak");
    }

    @Test
    public void dropClientCollectionTest() {
        clientRepository.insertClient(client1);
        clientRepository.dropClientCollection();
        Assertions.assertEquals(clientRepository.readAllClients().size(), 0);
    }
}
