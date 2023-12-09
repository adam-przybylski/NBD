package pl.nbd.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import pl.nbd.entities.Room;

import java.util.UUID;

@Dao
public interface RoomDao {

    @Select
    Room findById(UUID id);

    @Insert
    boolean create(Room room);

    @Update(customWhereClause = "id = :id AND room_capacity = :roomCapacity")
    boolean update(Room room, UUID id, int roomCapacity);

    @Delete(ifExists = true, entityClass = Room.class)
    boolean delete(Room room);

}
