package pl.nbd.entities;

import jakarta.json.bind.annotation.JsonbTypeAdapter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.GregorianCalendar;
import java.util.UUID;

public class Rent {


    private UUID rentId;

    private double rentCost;

    private GregorianCalendar rentStartDate;

    private GregorianCalendar rentEndDate;

    private Client client;

    private Room room;

    public Rent(UUID uuid, GregorianCalendar rentStartDate, Client client, Room room) {
        this.rentId = uuid;
        this.rentStartDate = rentStartDate;
        this.client = client;
        this.room = room;
    }


    public Rent(UUID rentId,
                double rentCost,
                GregorianCalendar rentStartDate,
                GregorianCalendar rentEndDate,
                Client client,
                Room room) {
        this.rentId = rentId;
        this.rentCost = rentCost;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
        this.client = client;
        this.room = room;
    }

    public Rent() {
    }

    public UUID getRentId() {
        return rentId;
    }

    public void setRentId(UUID rentId) {
        this.rentId = rentId;
    }

    public double getRentCost() {
        return rentCost;
    }

    public void setRentCost(double rentCost) {
        this.rentCost = rentCost;
    }

    public GregorianCalendar getRentStartDate() {
        return rentStartDate;
    }

    public void setRentStartDate(GregorianCalendar rentStartDate) {
        this.rentStartDate = rentStartDate;
    }

    public GregorianCalendar getRentEndDate() {
        return rentEndDate;
    }

    public void setRentEndDate(GregorianCalendar rentEndDate) {
        this.rentEndDate = rentEndDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void endRent(GregorianCalendar endDate) {
        if (endDate.before(rentStartDate)) {
            rentEndDate = rentStartDate;
        } else {
            rentEndDate = endDate;
        }
        calculateFinalRentCost();
    }


    public int rentDays() {
        return (int) Math.ceil((double) (rentEndDate.getTimeInMillis() - rentStartDate.getTimeInMillis()) / (1000 * 60 * 60 * 24));
    }

    public void calculateFinalRentCost() {
        rentCost = client.applyDiscount(rentDays() * room.getBasePrice());
    }

    @Override
    public String toString() {
        return "Rent{" +
                "rentId=" + rentId +
                ", rentCost=" + rentCost +
//                ", rentStartDate=" + rentStartDate.toString() +
                ", client=" + client +
                ", room=" + room +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rent) {
            Rent rent = (Rent) obj;
            return rent.getRentId().equals(this.rentId);
        }
        return false;
    }
}

