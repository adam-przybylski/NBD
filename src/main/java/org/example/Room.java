package org.example;

import jakarta.persistence.*;

@Entity
@Access(AccessType.FIELD)
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(unique = true)
    private final int roomNumber;
    private final int roomCapacity;
    private double basePrice;
    private boolean archived;

    @Version
    private int version;

    public Room(int roomNumber, int roomCapacity, double basePrice) {
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.basePrice = basePrice;
    }

    public int getVersion() {
        return version;
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

    public boolean isArchived() {
        return archived;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String toString() {
        String archived = isArchived() ? "archived" : "active";
        return String.format("Room %d - %d person(s) - %.2f EUR - %s", roomNumber, roomCapacity, basePrice, archived);
    }
}
