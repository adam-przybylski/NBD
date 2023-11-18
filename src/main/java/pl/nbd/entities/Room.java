package pl.nbd.entities;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTypeAdapter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.nbd.mappers.MongoUUID;
import pl.nbd.mappers.MongoUUIDAdapter;

import java.io.Serializable;

public class Room implements Serializable {
    @JsonbTypeAdapter(MongoUUIDAdapter.class)
    @BsonProperty("_id")
    private MongoUUID id;
    @BsonProperty("roomNumber")
    private int roomNumber;
    @BsonProperty("roomCapacity")
    private int roomCapacity;
    @BsonProperty("basePrice")
    private double basePrice;

    public Room() {
    }

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

    public void setId(MongoUUID id) {
        this.id = id;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }


    public MongoUUID getId() {
        return id;
    }

    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber=" + roomNumber +
                ", roomCapacity=" + roomCapacity +
                ", basePrice=" + basePrice +
                '}';
    }
}
