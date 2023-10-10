package org.example;

import java.util.Date;
import java.util.GregorianCalendar;

public class Rent {

    private final int rentId;
    private double rentCost;
    private final GregorianCalendar rentStartDate;
    private GregorianCalendar rentEndDate;
    private final Client client;
    private final Room room;

    public Rent(int rentId, GregorianCalendar rentStartDate, Client client, Room room) {
        this.rentId = rentId;
        this.rentStartDate = rentStartDate;
        this.client = client;
        this.room = room;
    }

    public int getRentId() {
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
