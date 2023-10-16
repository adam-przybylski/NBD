package org.example;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Room room1 = new Room(19, 31, 12);
        Client client = new RegularClient("John", "Doe", "187456789", 0.5);
//        Rent rent = new Rent(new GregorianCalendar(2021, Calendar.JANUARY, 1), client, room1);
        RoomRepository.createRoom(room1);
//        ClientRepository.createClient(client);
//        RentRepository.createRent(rent);
//        List<Room> rooms = RoomRepository.readAllRooms();
//        System.out.println(rooms);
//            Room room = RoomRepository.readRoomByRoomNumber(19);
//        System.out.println(room);
//        Room room2 = RoomRepository.readRoomByRoomNumber(13);
//        System.out.println(room2);
//        RoomRepository.updateRoomPrice(19,300);

//        RoomRepository.deleteRoom(19);
    }
}
