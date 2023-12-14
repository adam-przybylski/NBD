package pl.nbd.repositories;


import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.Insert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import com.datastax.oss.driver.api.querybuilder.select.Select;
import com.datastax.oss.driver.api.querybuilder.update.Update;
import pl.nbd.entities.Rent;

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
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_start_date"), DataTypes.BIGINT)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("rent_cost"), DataTypes.DOUBLE)
                        .withColumn(CqlIdentifier.fromCql("rent_end_date"), DataTypes.BIGINT)
                        .withColumn(CqlIdentifier.fromCql("room_id"), DataTypes.UUID)
                        .withClusteringOrder(CqlIdentifier.fromCql("rent_start_date"), ClusteringOrder.ASC)
                        .build();
        getSession().execute(createRentsByClient);

        SimpleStatement createRentsByRoom =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("rents_by_room"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("room_id"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_start_date"), DataTypes.BIGINT)
                        .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("rent_cost"), DataTypes.DOUBLE)
                        .withColumn(CqlIdentifier.fromCql("rent_end_date"), DataTypes.BIGINT)
                        .withColumn(CqlIdentifier.fromCql("client_id"), DataTypes.UUID)
                        .withClusteringOrder(CqlIdentifier.fromCql("rent_start_date"), ClusteringOrder.ASC)
                        .build();
        getSession().execute(createRentsByRoom);


    }

    public void insertData(Rent rent) {
        Insert insertRentsByClient = QueryBuilder.insertInto(CqlIdentifier.fromCql("rents_by_client"))
                .value(CqlIdentifier.fromCql("client_id"), literal(rent.getClient().getId()))
                .value(CqlIdentifier.fromCql("rent_start_date"), literal(rent.getRentStartDate().getTimeInMillis()))
                .value(CqlIdentifier.fromCql("rent_id"), literal(rent.getRentId()))
                .value(CqlIdentifier.fromCql("rent_end_date"), literal(0))
                .value(CqlIdentifier.fromCql("room_id"), literal(rent.getRoom().getId()))
                .value(CqlIdentifier.fromCql("rent_cost"), literal(rent.getRentCost()));



        Insert insertRentsByRoom = QueryBuilder.insertInto(CqlIdentifier.fromCql("rents_by_room"))
                .value(CqlIdentifier.fromCql("client_id"), literal(rent.getClient().getId()))
                .value(CqlIdentifier.fromCql("rent_start_date"), literal(rent.getRentStartDate().getTimeInMillis()))
                .value(CqlIdentifier.fromCql("rent_id"), literal(rent.getRentId()))
                .value(CqlIdentifier.fromCql("rent_end_date"), literal(0))
                .value(CqlIdentifier.fromCql("room_id"), literal(rent.getRoom().getId()))
                .value(CqlIdentifier.fromCql("rent_cost"), literal(rent.getRentCost()));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(insertRentsByClient.build())
                .addStatement(insertRentsByRoom.build())
                .build();

        getSession().execute(batchStatement);
    }

    public void endRent(Rent rent) {

        Update updateRentsByClient = QueryBuilder.update(CqlIdentifier.fromCql("rents_by_client"))
                .setColumn(CqlIdentifier.fromCql("rent_end_date"), literal(rent.getRentEndDate().getTimeInMillis()))
                .setColumn(CqlIdentifier.fromCql("rent_cost"), literal(rent.getRentCost()))
                .where(Relation.column(CqlIdentifier.fromCql("client_id")).isEqualTo(literal(rent.getClient().getId())))
                .where(Relation.column(CqlIdentifier.fromCql("rent_id")).isEqualTo(literal(rent.getRentId())))
                .where(Relation.column(CqlIdentifier.fromCql("rent_start_date")).isEqualTo(literal(rent.getRentStartDate().getTimeInMillis())));

        Update updateRentsByRoom = QueryBuilder.update(CqlIdentifier.fromCql("rents_by_room"))
                .setColumn(CqlIdentifier.fromCql("rent_end_date"), literal(rent.getRentEndDate().getTimeInMillis()))
                .setColumn(CqlIdentifier.fromCql("rent_cost"), literal(rent.getRentCost()))
                .where(Relation.column(CqlIdentifier.fromCql("room_id")).isEqualTo(literal(rent.getRoom().getId())))
                .where(Relation.column(CqlIdentifier.fromCql("rent_id")).isEqualTo(literal(rent.getRentId())))
                .where(Relation.column(CqlIdentifier.fromCql("rent_start_date")).isEqualTo(literal(rent.getRentStartDate().getTimeInMillis())));


        BatchStatement batchStatement2 = BatchStatement.builder(BatchType.LOGGED)
                .addStatement(updateRentsByClient.build())
                .addStatement(updateRentsByRoom.build())
                .build();

        getSession().execute(batchStatement2);
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
