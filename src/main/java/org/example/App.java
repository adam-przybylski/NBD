package org.example;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        GregorianCalendar date = new GregorianCalendar(2023, Calendar.OCTOBER, 5);
        Client client = new Client("John", "Doe", "123456789", new RegularClient());
        Room room = new Room(1, 2, 100);
        Rent rent = new Rent(1, date, client, room);
        rent.endRent(new GregorianCalendar(2023, Calendar.OCTOBER, 10));
        System.out.println(rent);

    }
}
