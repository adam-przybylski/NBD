package pl.nbd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.nbd.dao.ClientDao;
import pl.nbd.entities.Client;
import pl.nbd.entities.Default;
import pl.nbd.mappers.ClientMapper;
import pl.nbd.mappers.ClientMapperBuilder;
import pl.nbd.repositories.ClientRepository;

import java.util.UUID;


public class ClientRepoTest {

    private static Client client;

    private static ClientRepository clientRepository;

    private static ClientMapper clientMapper;

    private static ClientDao clientDao;

    @BeforeEach
    public void setup() {
        clientRepository = new ClientRepository();
        client = new Default(UUID.randomUUID(), "Jan", "Kowalski", "123456789", "default");
        clientMapper = new ClientMapperBuilder(clientRepository.getSession()).build();
        clientDao = clientMapper.clientDao();
    }

    @Test
    public void insertClientTest() {
        clientDao.create(client);
        Client client2 = clientDao.findById(client.getId());
        Assertions.assertEquals(client.getFirstName(), client2.getFirstName());
        Assertions.assertEquals(client.getLastName(), client2.getLastName());
        Assertions.assertEquals(client.getPersonalId(), client2.getPersonalId());
        Assertions.assertEquals(client.getDiscriminator(), client2.getDiscriminator());
    }

//    @Test
//    public void deleteClientTest() {
//        clientDao.create(client);
//        clientDao.remove(client);
//        Client client2 = clientDao.findById(client.getId());
//        Assertions.assertNull(client2);
//    }
}
