package pl.nbd;


import com.datastax.oss.driver.api.core.cql.Row;
import pl.nbd.dao.ClientDao;
import pl.nbd.dao.RoomDao;
import pl.nbd.entities.Default;
import pl.nbd.entities.PremiumClient;
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

public class App {
    public static void main(String[] args) {
//        RoomRepository roomRepository1 = new RoomRepository();
//        ClientRepository clientRepository = new ClientRepository();
        RentRepository rentRepository = new RentRepository();

        Room room = new Room(UUID.randomUUID(), 2, 2, 3);
//        RoomMapper roomMapper = new RoomMapperBuilder(roomRepository1.getSession()).build();
//        RoomDao roomDao = roomMapper.roomDao();
//
//        System.out.println(roomDao.create(room));
//        System.out.println(roomDao.delete(room));
//
//        System.out.println(roomDao.findById(UUID.fromString("c8bde5ae-2c84-4b33-9e43-7286cef83084")));
//        Room roomUpdated = new Room(UUID.fromString("c8bde5ae-2c84-4b33-9e43-7286cef83084"), 2, 2, 90);
//        System.out.println(roomDao.update(roomUpdated, room.getId(), room.getRoomCapacity()));

        PremiumClient premiumClient = new PremiumClient(UUID.randomUUID(), "Jan", "Kowalski", "123456789", 1, 0.9, "premium");
        Default adefault = new Default(UUID.randomUUID(), "Jan", "Kowalski", "123456789", "default");
//
//        ClientMapper clientMapper = new ClientMapperBuilder(clientRepository.getSession()).build();
//        ClientDao clientDao = clientMapper.clientDao();
//        clientDao.create(premiumClient);
//        clientDao.create(adefault);
//        System.out.println(clientDao.findById(UUID.fromString("1916e3a5-5db6-4d4d-8777-206e1a62d0c4")));


        Rent rent = new Rent(UUID.randomUUID(), new GregorianCalendar(2023,GregorianCalendar.DECEMBER,15), premiumClient, room);
        rentRepository.insertData(rent);
        rent.endRent(new GregorianCalendar(2023,GregorianCalendar.DECEMBER,30));
        rentRepository.endRent(rent);
        System.out.println("done");
    }
}
