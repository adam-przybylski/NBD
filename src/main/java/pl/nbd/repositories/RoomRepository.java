package pl.nbd.repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import pl.nbd.entities.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomRepository extends AbstractMongoRepository {

    private final MongoCollection<Room> roomCollection;

    public RoomRepository() {
        this.roomCollection = initDbConnection().getCollection("rooms", Room.class);
    }

    public void insertRoom(Room room) {
        roomCollection.insertOne(room);
    }

    public List<Room> readAllRooms() {
        FindIterable<Room> rooms = roomCollection.find();
        List<Room> roomList = new ArrayList<>();
        rooms.into(roomList);
        return roomList;
    }

    public Room readRoomByRoomNumber(int roomNumber) {
        return null;
    }

    public void updateRoomPrice(int roomNumber, double newPrice) {

    }

    public void deleteRoom(int roomNumber) {

    }

    public boolean isRoomAvailable(long roomID) {
        return false;
    }
}
