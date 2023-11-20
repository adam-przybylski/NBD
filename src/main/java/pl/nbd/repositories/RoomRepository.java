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
    private final Jsonb jsonb = JsonbBuilder.create();
    private final MongoCollection<Room> roomMongoCollection;

    public RoomRepository() {
        this.roomMongoCollection = initDbConnection().getCollection("rooms", Room.class);
        initRedisConnection();
        //restoreCashedRooms();
    }

    public void insertRoom(Room room) {
        roomMongoCollection.insertOne(room);
        insertRoomToRedis(room);
    }

    public void insertRoomToRedis(Room room) {
        String json = jsonb.toJson(room);
        getPool().jsonSet(hashPrefix + room.getRoomNumber(),  json);
    }

    public List<Room> readAllRooms() {
        FindIterable<Room> rooms = roomMongoCollection.find();
        List<Room> roomList = new ArrayList<>();
        rooms.into(roomList);
        return roomList;
    }

    public Room readRoomByRoomNumber(int roomNumber) {
        Room jsonRoom = readRoomByRoomNumberFromRedis(roomNumber);
        if (jsonRoom == null) {
            Room room = readRoomByRoomNumberFromMongo(roomNumber);
            if(room != null) {
                insertRoomToRedis(room);
                return room;
            }
            return null;
        }
        return jsonRoom;
    }

    public Room readRoomByRoomNumberFromRedis(int roomNumber) {
        String key = hashPrefix + roomNumber;
        if (!getPool().exists(key)) {
            return null;
        }
        Object json = getPool().jsonGet(key);
        String jsonRoom = jsonb.toJson(json);
        return jsonb.fromJson(jsonRoom, Room.class);
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
        removeRoomFromCache(roomNumber);
    }

    public void dropRoomCollection() {
        roomMongoCollection.drop();
    }

    public boolean isRoomAvailable(long roomID) {
        return false;
    }

    private void removeRoomFromCache(int roomNumber) {
        getPool().del(hashPrefix + roomNumber);
    }

    public void clearCache() {
        getPool().flushAll();
    }

    private void restoreCashedRooms() {
        List<Room> rooms = readAllRooms();
        for (Room room : rooms) {
            insertRoomToRedis(room);
        }
    }

    public Room readRoomByRoomNumberFromMongo(int roomNumber) {
        return roomMongoCollection.find(Filters.eq("roomNumber", roomNumber)).first();
    }
}
