package org.example;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import java.util.List;

public class RoomRepository {

    private static EntityManagerFactory emf;

    public static void createRoom(Room room) throws RuntimeException {
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
        em.persist(room);
        transaction.commit();
        em.close();
    }

    public static List<Room> readAllRooms() {
        if(emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("my-persistence-unit");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Room> query = cb.createQuery(Room.class);
        Root<Room> rootEntry = query.from(Room.class);
        query.select(rootEntry);
        return em.createQuery(query).getResultList();
    }

    public static Room readRoomByRoomNumber(int roomNumber) {
        if(emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("my-persistence-unit");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Room> query = cb.createQuery(Room.class);
        Root<Room> rootEntry = query.from(Room.class);
        Predicate roomNumberPredicate = cb.equal(rootEntry.get("roomNumber"), roomNumber);
        query.where(roomNumberPredicate); // Add the predicate to the query
        return em.createQuery(query).getSingleResult();
    }

    public static void updateRoomPrice(int roomNumber, double newPrice) {
        if(emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("my-persistence-unit");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaUpdate<Room> criteriaUpdate = cb.createCriteriaUpdate(Room.class);
        Root<Room> root = criteriaUpdate.from(Room.class);
        criteriaUpdate.set("basePrice", newPrice);
        criteriaUpdate.where(cb.equal(root.get("roomNumber"), roomNumber));
        EntityTransaction transaction =  em.getTransaction();
        transaction.begin();
        em.createQuery(criteriaUpdate).executeUpdate();
        transaction.commit();
        em.close();
    }

    public static void deleteRoom(int roomNumber) {
        if(emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("my-persistence-unit");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        EntityManager em = emf.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaDelete<Room> criteriaDelete = cb.createCriteriaDelete(Room.class);
        Root<Room> root = criteriaDelete.from(Room.class);
        criteriaDelete.where(cb.equal(root.get("roomNumber"), roomNumber));
        EntityTransaction transaction =  em.getTransaction();
        transaction.begin();
        em.createQuery(criteriaDelete).executeUpdate();
        transaction.commit();
        em.close();
    }

    public static boolean isRoomAvailable(long roomID) {
        if(emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("my-persistence-unit");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Room room = em.find(Room.class, roomID, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        em.getTransaction().commit();
        em.close();
        return room.isAvailable();
    }
}
