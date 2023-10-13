package org.example;

import jakarta.persistence.*;

public class Repository {
    private static EntityManagerFactory emf;

    public static <T> void create(T room) throws RuntimeException {
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
}
