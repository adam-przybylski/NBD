package pl.nbd.repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import pl.nbd.entities.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Filter;

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

    public void updateRoomPrice(int roomNumber, double newPrice) {
       Bson filter = Filters.eq("roomNumber", roomNumber);
       Bson setUpdate = Updates.set("basePrice", newPrice);
       roomCollection.updateOne(filter, setUpdate);
    }

    public void deleteRoom(int roomNumber) {
        Bson filter = Filters.eq("roomNumber", roomNumber);
        roomCollection.deleteOne(filter);
    }

    public void dropRoomCollection() {
        roomCollection.drop();
    }

    public boolean isRoomAvailable(long roomID) {
        return false;
    }
}
