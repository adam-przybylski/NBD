package pl.nbd.repositories;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.session.Session;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.bson.conversions.Bson;
import pl.nbd.entities.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomRepository extends AbstractDatabaseRepository {

    @Override
    public void close() throws Exception {

    }


    public RoomRepository() {
        initSession();
        SimpleStatement createRooms =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rooms"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("room_capacity"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("room_number"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("base_price"), DataTypes.DOUBLE)
                        .withClusteringOrder(CqlIdentifier.fromCql("room_capacity"), ClusteringOrder.ASC)
                        .build();
        getSession().execute(createRooms);
    }

    public void deleteData() {
        SimpleStatement deleteRooms = SchemaBuilder.dropTable(CqlIdentifier.fromCql("rooms")).ifExists().build();
        getSession().execute(deleteRooms);
    }



}
