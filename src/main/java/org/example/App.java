package org.example;

import java.util.Calendar;
import java.util.GregorianCalendar;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Room room1 = new Room(13, 31, 12);
        Client client = new RegularClient("John", "Doe", "187456789", 0.5);
        Rent rent = new Rent(new GregorianCalendar(2021, Calendar.JANUARY, 1), client, room1);
        Repository.create(room1);
        Repository.create(client);
        Repository.create(rent);
    }
}
