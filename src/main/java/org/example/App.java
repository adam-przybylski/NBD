package org.example;

import java.util.Calendar;
import java.util.GregorianCalendar;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("my-persistence-unit");
        // Use the EntityManagerFactory to create EntityManagers and work with your entities
        // ...
        emf.close(); // Close the EntityManagerFactory when done
//        GregorianCalendar date = new GregorianCalendar(2023, Calendar.OCTOBER, 5, 12, 0, 0);
//        //Client client = new Client("John", "Doe", "123456789", new Default());
//        Room room = new Room(1, 2, 100);
//        Rent rent = new Rent(1, date, client, room);
//        rent.endRent(new GregorianCalendar(2023, Calendar.OCTOBER, 5, 11, 0, 0));
//        System.out.println(rent.toString());
//
//        PremiumClient jakub = new PremiumClient(0.5, 2);
//        //Client client2 = new Client("Joe", "Doe", "1337", jakub);
//
//        System.out.println(client2);
    }
}
