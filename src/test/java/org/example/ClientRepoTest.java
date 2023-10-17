package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;


public class ClientRepoTest {
    private static Client client;
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
    }

    @AfterAll
    public static void close() {
        if (em != null) {
            emf.close();
        }
    }

    @Test
    public void testCreateClient() {
        ClientRepository.createClient(client);
        int expectedNumberOfClients = 1;
        int actualNumberOfClients = em.createQuery("select c from Client c").getResultList().size();
        Assertions.assertEquals(expectedNumberOfClients, actualNumberOfClients);
    }

    @Test
    public void testReadAllClients() {
        ClientRepository.createClient(client);
        int expectedNumberOfClients = 1;
        int actualNumberOfClients = ClientRepository.readAllClients().size();
        Assertions.assertEquals(expectedNumberOfClients, actualNumberOfClients);
    }

    @Test
    public void testUpdateClient() {
        ClientRepository.createClient(client);
        client.setLastName("Stonoga");
        ClientRepository.updateLastName(client.getPersonalId(), client.getLastName());
        String expectedLastName = "Stonoga";
        String actualLasttName = em.createQuery("select c from Client c where c.id = :id", Client.class)
                .setParameter("id", client.getId())
                .getSingleResult()
                .getLastName();
        Assertions.assertEquals(expectedLastName, actualLasttName);
    }

}
