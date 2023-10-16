package org.example;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class RentRepository {

    private static EntityManagerFactory emf;

    public static void createRent(Rent rent) throws RuntimeException {
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
        Room room = em.find(Room.class, rent.getRoom().getId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        if (!room.isAvailable()) {
            throw new RuntimeException("Room is not available");
        }
        room.setAvailable(false);
        em.persist(room);
        em.persist(rent);
        transaction.commit();
        em.close();
    }

    public static List<Rent> readAllRents() {
        if(emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("my-persistence-unit");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rent> query = cb.createQuery(Rent.class);
        Root<Rent> rootEntry = query.from(Rent.class);
        query.select(rootEntry);
        return em.createQuery(query).getResultList();
    }

    public static void endRent(Rent rent) {
        if(emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("my-persistence-unit");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction =  em.getTransaction();
        transaction.begin();
        Room room = em.find(Room.class, rent.getRoom().getId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        room.setAvailable(true);
        em.persist(room);
        rent.endRent(new GregorianCalendar());
        em.merge(rent);
        transaction.commit();
        em.close();
    }
}
