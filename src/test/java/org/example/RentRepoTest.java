package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RentRepoTest {

    private static Client client;
    private static Room room;
    private static Rent rent;

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeAll
    public void setup() {
        client = new RegularClient("John", "Doe", "187456789", 0.5);
        room = new Room(19, 31, 12);
        rent = new Rent(new GregorianCalendar(2021, Calendar.JANUARY, 1), client, room);
        emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        em = emf.createEntityManager();

    }

    @Test
    public void testCreateRent() {
        RentRepository.createRent(rent);

    }
}
