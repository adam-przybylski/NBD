package org.example;

import java.util.Date;

public class Rent {

    private final int rentId;
    private double rentCost;
    private final Date rentStartDate;
    private Date rentEndDate;
    private final Client client;
    private final Room room;

    public Rent(int rentId, Date rentStartDate, Client client, Room room) {
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

    public Date getRentStartDate() {
        return rentStartDate;
    }

    public Date getRentEndDate() {
        return rentEndDate;
    }

    public Client getClient() {
        return client;
    }

    public Room getRoom() {
        return room;
    }

    public void endRent(Date rentEndDate) {
        this.rentEndDate = rentEndDate;
    }

    public int getRentDays() {
        return (int) ((rentEndDate.getTime() - rentStartDate.getTime()) / (1000 * 60 * 60 * 24));
    }

    public double calculateFinalRentCost() {
        return 0;
        // TODO end this method
    }
}
