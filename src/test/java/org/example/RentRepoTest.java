package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RentRepoTest {

    private static Client client;
    private static Room room;
    private static Rent rent;

    private static EntityManagerFactory emf;

    private static EntityManager em;

    @BeforeAll
    public static void setup() {
        emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        em = emf.createEntityManager();
    }

    @BeforeEach
    public void clearData() {
        em.getTransaction().begin();
        em.createQuery("delete from Rent").executeUpdate();
        em.createQuery("delete from Room").executeUpdate();
        em.createQuery("delete from Client").executeUpdate();
        em.getTransaction().commit();
        client = new RegularClient("John", "Doe", "187456789", 0.5);
        room = new Room(19, 31, 12);
        rent = new Rent(new GregorianCalendar(2021, Calendar.JANUARY, 1), client, room);
    }

    @AfterAll
    public static void close() {
        if (em != null) {
            emf.close();
        }
    }

    @Test
    public void testCreateRent() {
        ClientRepository.createClient(client);
        RoomRepository.createRoom(room);
        try {
            RentRepository.createRent(rent);
        } catch (RuntimeException err) {
            System.out.println(err.getMessage());
        }
        int expectedNumberOfRents = 1;
        int actualNumberOfRents = em.createQuery("select r from Rent r").getResultList().size();
        Assertions.assertEquals(expectedNumberOfRents, actualNumberOfRents);
    }

    @Test
    public void testRoomIsNotAvailable() {
        Assertions.assertNotNull(room);
        ClientRepository.createClient(client);
        RoomRepository.createRoom(room);
        Assertions.assertNotNull(room);
        Assertions.assertTrue(RoomRepository.isRoomAvailable(room.getId()));
        RentRepository.createRent(rent);
        Assertions.assertThrows(RuntimeException.class, () -> {
            RentRepository.createRent(rent);
        });
        Assertions.assertFalse(RoomRepository.isRoomAvailable(room.getId()));
    }

    @Test
    public void testEndRent() {
        ClientRepository.createClient(client);
        RoomRepository.createRoom(room);
        RentRepository.createRent(rent);
        Assertions.assertNotNull(rent);
        Assertions.assertNotNull(room);
        Assertions.assertNotNull(client);
        Assertions.assertFalse(RoomRepository.isRoomAvailable(room.getId()));
        RentRepository.endRent(rent);
        Assertions.assertTrue(RoomRepository.isRoomAvailable(room.getId()));
    }
}
