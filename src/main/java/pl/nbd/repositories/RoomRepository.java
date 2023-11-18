package pl.nbd.repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.bson.conversions.Bson;
import pl.nbd.entities.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomRepository extends AbstractDatabaseRepository {
    private final String hashPrefix = "room:";
    private Jsonb jsonb = JsonbBuilder.create();
    private final MongoCollection<Room> roomMongoCollection;

    public RoomRepository() {
        this.roomMongoCollection = initDbConnection().getCollection("rooms", Room.class);
        initRedisConnection();
    }

    public void insertRoom(Room room) {
        roomMongoCollection.insertOne(room);
        insertRoomToRedis(room);
    }

    public void insertRoomToRedis(Room room) {
        int roomNumber = room.getRoomNumber();
        String json = jsonb.toJson(room);
        getPool().jsonSet(hashPrefix + roomNumber,  json);
    }

    public List<Room> readAllRooms() {
        FindIterable<Room> rooms = roomMongoCollection.find();
        List<Room> roomList = new ArrayList<>();
        rooms.into(roomList);
        return roomList;
    }

    public Room readRoomByRoomNumber(int roomNumber) {
        String jsonRoom = readRoomByRoomNumberFromRedis(roomNumber);
        if (jsonRoom == null) {
            Room room = roomMongoCollection.find(Filters.eq("roomNumber", roomNumber)).first();
            if(room != null) {
                insertRoomToRedis(room);
                return room;
            }
            return null;
        }
        return jsonb.fromJson(jsonRoom, Room.class);
    }

    public String readRoomByRoomNumberFromRedis(int roomNumber) {
        return getPool().get(hashPrefix + roomNumber);
    }



    public void updateRoomPrice(int roomNumber, double newPrice) {
       Bson filter = Filters.eq("roomNumber", roomNumber);
       Bson setUpdate = Updates.set("basePrice", newPrice);
       roomMongoCollection.updateOne(filter, setUpdate);
       getPool().jsonSet(hashPrefix + roomNumber, jsonb.toJson(readRoomByRoomNumber(roomNumber)));
    }

    public void deleteRoom(int roomNumber) {
        Bson filter = Filters.eq("roomNumber", roomNumber);
        roomMongoCollection.deleteOne(filter);
        getPool().del(hashPrefix + roomNumber);
    }

    public void dropRoomCollection() {
        roomMongoCollection.drop();
    }

    public boolean isRoomAvailable(long roomID) {
        return false;
    }
}
