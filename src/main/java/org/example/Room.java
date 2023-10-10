package org.example;

public class Room {

    private final int roomNumber;
    private final int roomCapacity;
    private double basePrice;
    private boolean archived;

    public Room(int roomNumber, int roomCapacity, double basePrice) {
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
