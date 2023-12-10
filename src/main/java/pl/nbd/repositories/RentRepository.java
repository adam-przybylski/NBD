package pl.nbd.repositories;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.codec.registry.CodecRegistry;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import pl.nbd.entities.Rent;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;

public class RentRepository extends AbstractDatabaseRepository {

    @Override
    public void close() throws Exception {

    }


    public RentRepository() {
        initSession();
        SimpleStatement createRentsByClient =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rents_by_client"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("client_id"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_start_date"), DataTypes.TIMESTAMP)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("rent_cost"), DataTypes.DOUBLE)
                        .withColumn(CqlIdentifier.fromCql("rent_end_date"), DataTypes.TIMESTAMP)
                        .withColumn(CqlIdentifier.fromCql("room_id"), DataTypes.UUID)
                        .withClusteringOrder(CqlIdentifier.fromCql("rent_start_date"), ClusteringOrder.ASC)
                        .build();
        getSession().execute(createRentsByClient);

        SimpleStatement createRentsByRoom =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rents_by_room"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("room_id"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_start_date"), DataTypes.TIMESTAMP)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("rent_cost"), DataTypes.DOUBLE)
                        .withColumn(CqlIdentifier.fromCql("rent_end_date"), DataTypes.TIMESTAMP)
                        .withColumn(CqlIdentifier.fromCql("client_id"), DataTypes.UUID)
                        .withClusteringOrder(CqlIdentifier.fromCql("rent_start_date"), ClusteringOrder.ASC)
                        .build();
        getSession().execute(createRentsByRoom);


    }

    public void insertData(Rent rent) {
        Insert insertRentsByClient = QueryBuilder.insertInto(CqlIdentifier.fromCql("rents_by_client"))
                .value(CqlIdentifier.fromCql("client_id"), literal(rent.getClient().getId()))
                .value(CqlIdentifier.fromCql("rent_start_date"), currentTimestamp())
                .value(CqlIdentifier.fromCql("rent_id"), literal(rent.getRentId()))
                .value(CqlIdentifier.fromCql("rent_end_date"), literal(0))
                .value(CqlIdentifier.fromCql("room_id"), literal(rent.getRoom().getId()))
                .value(CqlIdentifier.fromCql("rent_cost"), bindMarker(CqlIdentifier.fromCql("rent_cost")))
                .ifNotExists();
        SimpleStatement simpleStatement = insertRentsByClient.build()
                .setNamedValuesWithIds(
                        Map.of(CqlIdentifier.fromCql("rent_cost"), rent.getRentCost())
                );
        getSession().execute(simpleStatement);


        Insert insertRentsByRoom = QueryBuilder.insertInto(CqlIdentifier.fromCql("rents_by_room"))
                .value(CqlIdentifier.fromCql("client_id"), literal(rent.getClient().getId()))
                .value(CqlIdentifier.fromCql("rent_start_date"), currentTimestamp())
                .value(CqlIdentifier.fromCql("rent_id"), literal(rent.getRentId()))
                .value(CqlIdentifier.fromCql("rent_end_date"), literal(0))
                .value(CqlIdentifier.fromCql("room_id"), literal(rent.getRoom().getId()))
                .value(CqlIdentifier.fromCql("rent_cost"), bindMarker(CqlIdentifier.fromCql("rent_cost")))
                .ifNotExists();
        SimpleStatement simpleStatement2 = insertRentsByRoom.build()
                .setNamedValuesWithIds(
                        Map.of(CqlIdentifier.fromCql("rent_cost"), rent.getRentCost())
                );
        getSession().execute(simpleStatement2);
    }

    public void endRent(Rent rent) {
        GregorianCalendar rentStartDate = rent.getRentStartDate();

        Date date = rentStartDate.getTime();
        String formattedDate = null;
        if (date != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            formattedDate = dateFormat.format(date);
        } else {
            System.err.println("Invalid rent start date: " + rentStartDate);
        }

//TODO NAPRAWIC TO TRZEBA BO NIE USUWA
        SimpleStatement deleteRentByClient = QueryBuilder.deleteFrom(CqlIdentifier.fromCql("rents_by_client"))
                .whereColumn(CqlIdentifier.fromCql("client_id")).isEqualTo(literal(rent.getClient().getId()))
                .whereColumn(CqlIdentifier.fromCql("rent_start_date")).isEqualTo(literal(formattedDate))
                .whereColumn(CqlIdentifier.fromCql("rent_id")).isEqualTo(literal(rent.getRentId()))
                .build();
        getSession().execute(deleteRentByClient);


        SimpleStatement deleteRentByRoom = QueryBuilder.deleteFrom(CqlIdentifier.fromCql("rents_by_room"))
                .whereColumn(CqlIdentifier.fromCql("rent_start_date")).isEqualTo(literal(formattedDate))
                .whereColumn(CqlIdentifier.fromCql("rent_id")).isEqualTo(literal(rent.getRentId()))
                .whereColumn(CqlIdentifier.fromCql("room_id")).isEqualTo(literal(rent.getRoom().getId()))
                .build();
        getSession().execute(deleteRentByRoom);


        Insert insertRentsByRoom = QueryBuilder.insertInto(CqlIdentifier.fromCql("rents_by_room"))
                .value(CqlIdentifier.fromCql("client_id"), literal(rent.getClient().getId()))
                .value(CqlIdentifier.fromCql("rent_start_date"), literal(formattedDate))
                .value(CqlIdentifier.fromCql("rent_id"), literal(rent.getRentId()))
                .value(CqlIdentifier.fromCql("rent_end_date"), currentTimestamp())
                .value(CqlIdentifier.fromCql("room_id"), literal(rent.getRoom().getId()))
                .value(CqlIdentifier.fromCql("rent_cost"), literal(rent.getRentCost()))
                .ifNotExists();
        SimpleStatement simpleStatement2 = insertRentsByRoom.build();
        getSession().execute(simpleStatement2);


        Insert insertRentsByClient = QueryBuilder.insertInto(CqlIdentifier.fromCql("rents_by_client"))
                .value(CqlIdentifier.fromCql("client_id"), literal(rent.getClient().getId()))
                .value(CqlIdentifier.fromCql("rent_start_date"), literal(formattedDate))
                .value(CqlIdentifier.fromCql("rent_id"), literal(rent.getRentId()))
                .value(CqlIdentifier.fromCql("rent_end_date"), currentTimestamp())
                .value(CqlIdentifier.fromCql("room_id"), literal(rent.getRoom().getId()))
                .value(CqlIdentifier.fromCql("rent_cost"), literal(rent.getRentCost()))
                .ifNotExists();
        SimpleStatement simpleStatement = insertRentsByClient.build();
        getSession().execute(simpleStatement);


    }

    public List<Row> selectAllDataByClient(UUID clientId) {
        Select selectAllDataByClient = QueryBuilder.selectFrom(CqlIdentifier.fromCql("rents_by_client"))
                .all()
                .where(Relation.column(CqlIdentifier.fromCql("client_id")).isEqualTo(literal(clientId)));
        ResultSet resultSet = getSession().execute(selectAllDataByClient.build());
        return resultSet.all();
    }

    public List<Row> selectAllDataByRoom(UUID roomId) {
        Select selectAllDataByRoom = QueryBuilder.selectFrom(CqlIdentifier.fromCql("rents_by_room"))
                .all()
                .where(Relation.column(CqlIdentifier.fromCql("room_id")).isEqualTo(literal(roomId)));
        ResultSet resultSet = getSession().execute(selectAllDataByRoom.build());
        return resultSet.all();
    }

    public void deleteDataByRooms() {
        SimpleStatement deleteRooms = SchemaBuilder.dropTable(CqlIdentifier.fromCql("rents_by_room")).ifExists().build();
        getSession().execute(deleteRooms);
    }

    public void deleteDataByClients() {
        SimpleStatement deleteRooms = SchemaBuilder.dropTable(CqlIdentifier.fromCql("rents_by_client")).ifExists().build();
        getSession().execute(deleteRooms);
    }


}
