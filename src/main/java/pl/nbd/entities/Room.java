package pl.nbd.entities;

public class Room {

    private long id;

    public Room() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private int roomNumber;
    private int roomCapacity;
    private double basePrice;
    private boolean available;

    public Room(int roomNumber, int roomCapacity, double basePrice) {
        this.roomNumber = roomNumber;
        this.roomCapacity = roomCapacity;
        this.basePrice = basePrice;
        this.available = true;
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

    public boolean isAvailable() {
        return available;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String toString() {
        String archived = isAvailable() ? "archived" : "active";
        return String.format("Room %d - %d person(s) - %.2f EUR - %s", roomNumber, roomCapacity, basePrice, archived);
    }
}
