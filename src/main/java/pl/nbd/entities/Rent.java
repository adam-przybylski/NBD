package pl.nbd.entities;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.nbd.mappers.MongoUUID;

import java.util.GregorianCalendar;

public class Rent {

    @BsonProperty("_id")
    private MongoUUID rentId;

    @BsonProperty("rentCost")
    private double rentCost;

    @BsonProperty("rentStartDate")
    private GregorianCalendar rentStartDate;

    @BsonProperty("rentEndDate")
    private GregorianCalendar rentEndDate;

    @BsonProperty("client")
    private Client client;

    @BsonProperty("room")
    private Room room;

    public Rent(MongoUUID uuid, GregorianCalendar rentStartDate, Client client, Room room) {
        this.rentId = uuid;
        this.rentStartDate = rentStartDate;
        this.client = client;
        this.room = room;
    }

    @BsonCreator
    public Rent(@BsonProperty("_id") MongoUUID rentId,
                @BsonProperty("rentCost") double rentCost,
                @BsonProperty("rentStartDate") GregorianCalendar rentStartDate,
                @BsonProperty("rentEndDate") GregorianCalendar rentEndDate,
                @BsonProperty("client") Client client,
                @BsonProperty("room") Room room) {
        this.rentId = rentId;
        this.rentCost = rentCost;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
        this.client = client;
        this.room = room;
    }

    public Rent() {
    }

    public MongoUUID getRentId() {
        return rentId;
    }

    public void setRentId(MongoUUID rentId) {
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

}
