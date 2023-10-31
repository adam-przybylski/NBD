package pl.nbd.entities;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.UUID;

public class Room {

    @BsonProperty("_id")
    private UUID id;
    @BsonProperty("roomNumber")
    private int roomNumber;
    @BsonProperty("roomCapacity")
    private int roomCapacity;
    @BsonProperty("basePrice")
    private double basePrice;

    @BsonCreator
    public Room(
                @BsonProperty("roomNumber") int roomNumber,
                @BsonProperty("roomCapacity") int roomCapacity,
                @BsonProperty("basePrice") double basePrice) {
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.basePrice = basePrice;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public double getBasePrice() {
        return basePrice;
    }


    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }


    public String toString() {
        return "Room{" +
                "id=" + id.toString() +
                "roomNumber=" + roomNumber +
                ", roomCapacity=" + roomCapacity +
                ", basePrice=" + basePrice +
                '}';
    }
}
