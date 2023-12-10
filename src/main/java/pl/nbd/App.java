package pl.nbd;


import pl.nbd.dao.ClientDao;
import pl.nbd.dao.RoomDao;
import pl.nbd.entities.*;
import pl.nbd.mappers.ClientMapper;
import pl.nbd.mappers.ClientMapperBuilder;
import pl.nbd.mappers.RoomMapper;
import pl.nbd.mappers.RoomMapperBuilder;
import pl.nbd.repositories.ClientRepository;
import pl.nbd.repositories.RentRepository;
import pl.nbd.repositories.RoomRepository;

import java.util.GregorianCalendar;
import java.util.UUID;

public class App {
    public static void main(String[] args) {
//        RoomRepository roomRepository1 = new RoomRepository();
        Room room = new Room(UUID.randomUUID(), 2, 2, 3);
//        RoomMapper roomMapper = new RoomMapperBuilder(roomRepository1.getSession()).build();
//        RoomDao roomDao = roomMapper.roomDao();

//        System.out.println(roomDao.create(room));
//        System.out.println(roomDao.delete(room));

//        System.out.println(roomDao.findById(UUID.fromString("a8ad9127-55f2-4d3b-be6e-00a574eeaa17")));
        Room roomUpdated = new Room(UUID.fromString("a8ad9127-55f2-4d3b-be6e-00a574eeaa17"), 2, 2, 90);
//        System.out.println(roomDao.update(roomUpdated, room.getId(), room.getRoomCapacity()));

        PremiumClient premiumClient = new PremiumClient(UUID.randomUUID(), "Jan", "Kowalski", "123456789", 1, 0.9, "premium");
        Default adefault = new Default(UUID.randomUUID(), "Jan", "Kowalski", "123456789", "default");
//        ClientRepository clientRepository = new ClientRepository();
//        ClientMapper clientMapper = new ClientMapperBuilder(clientRepository.getSession()).build();
//        ClientDao clientDao = clientMapper.clientDao();
//        clientDao.create(premiumClient);
//        clientDao.create(adefault);
//        System.out.println(clientDao.findById(UUID.fromString("78cb32d8-4061-4bbe-a5a4-c76ce5f27b79")));

        RentRepository rentRepository = new RentRepository();
        Rent rent = new Rent(UUID.randomUUID(), new GregorianCalendar(), premiumClient, room);
        rentRepository.insertData(rent);
        rent.endRent(new GregorianCalendar(2023,GregorianCalendar.DECEMBER,30));
        rentRepository.endRent(rent);

        System.out.println("done");
    }
}
