package org.example;

import jakarta.persistence.*;
import org.hibernate.annotations.NotFound;

import java.util.Date;
import java.util.GregorianCalendar;

@Entity
@Access(AccessType.FIELD)
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long rentId;
    private double rentCost;
    private GregorianCalendar rentStartDate;
    private GregorianCalendar rentEndDate;

    @Version
    private long version;

    @ManyToOne
    @JoinColumn
    private Client client;

    @ManyToOne
    @JoinColumn
    private Room room;

    public Rent(GregorianCalendar rentStartDate, Client client, Room room) {
        this.rentStartDate = rentStartDate;
        this.client = client;
        this.room = room;
    }

    public Rent() {

    }

    public long getRentId() {
        return rentId;
    }

    public double getRentCost() {
        return rentCost;
    }

    public GregorianCalendar getRentStartDate() {
        return rentStartDate;
    }

    public GregorianCalendar getRentEndDate() {
        return rentEndDate;
    }

    public Client getClient() {
        return client;
    }

    public Room getRoom() {
        return room;
    }

    public void endRent(GregorianCalendar endDate) {
        if (endDate.before(rentStartDate)) {
            rentEndDate = rentStartDate;
        } else {
            rentEndDate = endDate;
        }
        calculateFinalRentCost();
    }

    public int getRentDays() {
        return (int) Math.ceil((double) (rentEndDate.getTimeInMillis() - rentStartDate.getTimeInMillis()) / (1000 * 60 * 60 * 24));
    }

    public void calculateFinalRentCost() {
        rentCost = client.applyDiscount(getRentDays() * room.getBasePrice());
    }

    public String toString() {
        return String.format("Rent %d - %s - %s - %.2f EUR", rentId, rentStartDate.getTime(), rentEndDate.getTime(), rentCost);
    }
}
