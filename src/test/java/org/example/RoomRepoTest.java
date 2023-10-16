package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;


public class RoomRepoTest {
    private static Room room;
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
        em.createQuery("delete from Room").executeUpdate();
        em.getTransaction().commit();
        room = new Room(19, 31, 12);
    }

    @AfterAll
    public static void close() {
        if (em != null) {
            emf.close();
        }
    }

    @Test
    public void testCreateRoom() {
        RoomRepository.createRoom(room);
        int expectedNumberOfRooms = 1;
        int actualNumberOfRooms = em.createQuery("select r from Room r").getResultList().size();
        Assertions.assertEquals(expectedNumberOfRooms, actualNumberOfRooms);
    }

    @Test
    public void testReadAllRooms() {
        RoomRepository.createRoom(room);
        int expectedNumberOfRooms = 1;
        int actualNumberOfRooms = RoomRepository.readAllRooms().size();
        Assertions.assertEquals(expectedNumberOfRooms, actualNumberOfRooms);
    }

    @Test
    public void testUpdateRoom() {
        RoomRepository.createRoom(room);
        room.setBasePrice(100);
        RoomRepository.updateRoomPrice(room.getRoomNumber(), room.getBasePrice());
        double expectedPrice = 100;
        double actualPrice = em.createQuery("select r from Room r where r.id = :id", Room.class)
                .setParameter("id", room.getId())
                .getSingleResult()
                .getBasePrice();
        Assertions.assertEquals(expectedPrice, actualPrice);
    }

    @Test
    public void testDeleteRoom() {
        RoomRepository.createRoom(room);
        RoomRepository.deleteRoom(room.getRoomNumber());
        int expectedNumberOfRooms = 0;
        int actualNumberOfRooms = em.createQuery("select r from Room r").getResultList().size();
        Assertions.assertEquals(expectedNumberOfRooms, actualNumberOfRooms);
    }
}
