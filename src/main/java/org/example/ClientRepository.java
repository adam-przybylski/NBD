package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.criteria.*;

import java.util.List;

public class ClientRepository {

    private static EntityManagerFactory emf;

    public static void createClient(Client client) throws RuntimeException {
        if (emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("my-persistence-unit");
            } catch (Exception e) {
                System.out.println("Error with instantiating connection: " + e.getMessage());
                throw new RuntimeException();
            }
        }
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction =  em.getTransaction();
        transaction.begin();
        em.persist(client);
        transaction.commit();
        em.close();
    }

    public static List<Client> readAllClients() {
        if(emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("my-persistence-unit");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> query = cb.createQuery(Client.class);
        Root<Client> rootEntry = query.from(Client.class);
        query.select(rootEntry);
        return em.createQuery(query).getResultList();
    }

    public static Client readRoomByRoomPersonalId(String personalId) {
        if(emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("my-persistence-unit");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Client> query = cb.createQuery(Client.class);
        Root<Client> rootEntry = query.from(Client.class);
        Predicate personalIdPredicate = cb.equal(rootEntry.get("personalId"), personalId);
        query.where(personalIdPredicate); // Add the predicate to the query
        return em.createQuery(query).getSingleResult();
    }

    public static void updateLastName(String personalId, String newLastName) {
        if(emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("my-persistence-unit");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Client> criteriaUpdate = cb.createCriteriaUpdate(Client.class);
        Root<Client> root = criteriaUpdate.from(Client.class);
        criteriaUpdate.set("lastName", newLastName);
        criteriaUpdate.where(cb.equal(root.get("personalId"), personalId));
        EntityTransaction transaction =  em.getTransaction();
        transaction.begin();
        em.createQuery(criteriaUpdate).executeUpdate();
        transaction.commit();
        em.close();
    }
}
