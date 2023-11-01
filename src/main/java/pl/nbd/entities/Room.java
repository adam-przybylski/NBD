package pl.nbd.entities;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;

public class Room implements Serializable {

    @BsonProperty("_id")
    private MongoUUID id;
    @BsonProperty("roomNumber")
    private int roomNumber;
    @BsonProperty("roomCapacity")
    private int roomCapacity;
    @BsonProperty("basePrice")
    private double basePrice;


    public Room(int roomNumber,
                int roomCapacity,
                double basePrice) {
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.basePrice = basePrice;
    }

    @BsonCreator
    public Room(@BsonProperty("_id") MongoUUID id,
                @BsonProperty("roomNumber") int roomNumber,
                @BsonProperty("roomCapacity") int roomCapacity,
                @BsonProperty("basePrice") double basePrice) {
        this.id = id;
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
                ", roomNumber=" + roomNumber +
                ", roomCapacity=" + roomCapacity +
                ", basePrice=" + basePrice +
                '}';
    }
}
