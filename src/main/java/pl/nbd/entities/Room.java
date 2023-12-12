package pl.nbd.entities;

import com.datastax.oss.driver.api.mapper.annotations.*;

import java.io.Serializable;
import java.util.UUID;

import static com.datastax.oss.driver.api.mapper.entity.naming.NamingConvention.UPPER_SNAKE_CASE;

@Entity(defaultKeyspace = "rent_a_room")
@NamingStrategy(convention = UPPER_SNAKE_CASE)
@CqlName("rooms")
public class Room implements Serializable {

    @PartitionKey
    @CqlName("id")
    private UUID id;

    @CqlName("room_number")
    private int roomNumber;

    @ClusteringColumn
    @CqlName("room_capacity")
    private int roomCapacity;

    @CqlName("base_price")
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


    public Room(UUID id,
                int roomNumber,
                 int roomCapacity,
                double basePrice) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.basePrice = basePrice;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomNumber=" + roomNumber +
                ", roomCapacity=" + roomCapacity +
                ", basePrice=" + basePrice +
                '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room that = (Room) o;
        return roomNumber == that.roomNumber &&
                roomCapacity == that.roomCapacity &&
                Double.compare(that.basePrice, basePrice) == 0 &&
                id.equals(that.id);
    }
}
