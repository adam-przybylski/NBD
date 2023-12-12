package pl.nbd;

import com.datastax.oss.driver.api.core.cql.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.nbd.entities.Client;
import pl.nbd.entities.Default;
import pl.nbd.entities.Rent;
import pl.nbd.entities.Room;
import pl.nbd.mappers.ClientMapper;
import pl.nbd.mappers.ClientMapperBuilder;
import pl.nbd.mappers.RoomMapper;
import pl.nbd.mappers.RoomMapperBuilder;
import pl.nbd.repositories.ClientRepository;
import pl.nbd.repositories.RentRepository;
import pl.nbd.repositories.RoomRepository;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public class RentRepoTest {

    private static Client client;
    private static Room room;
    private static Rent rent;

    private static RentRepository rentRepository;
    private static ClientRepository clientRepository;
    private static RoomRepository roomRepository;

    @BeforeAll
    public static void setup() {
        rentRepository = new RentRepository();
        clientRepository = new ClientRepository();
        roomRepository = new RoomRepository();
        client = new Default(UUID.randomUUID(), "Jan", "Kowalski", "123456789", "default");
        room = new Room(UUID.randomUUID(), 2, 2, 3);
        rent = new Rent(UUID.randomUUID(), new GregorianCalendar(), client, room);

    }


    @Test
    public void insertRentTest() {
        ClientMapper clientMapper = new ClientMapperBuilder(clientRepository.getSession()).build();
        RoomMapper roomMapper = new RoomMapperBuilder(roomRepository.getSession()).build();
        clientMapper.clientDao().create(client);
        roomMapper.roomDao().create(room);
        rentRepository.insertData(rent);

        rentRepository.insertData(rent);

        List<Row> rows = rentRepository.selectAllDataByClient(client.getId());
        assert rows.size() == 1;

    }


    @Test
    public void endRentDateTest() {
        rent.endRent(new GregorianCalendar(2023, GregorianCalendar.DECEMBER, 30));
        rentRepository.endRent(rent);
        List<Row> rows = rentRepository.selectAllDataByClient(client.getId());
        Assertions.assertNotEquals(rows.get(0).getInstant("rent_end_date"), null);
    }


}
