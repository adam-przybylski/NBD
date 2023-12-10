package pl.nbd.repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;


public class ClientRepository extends AbstractDatabaseRepository {

    @Override
    public void close() throws Exception {

    }

    public ClientRepository() {
        initSession();
        SimpleStatement createClients =
                SchemaBuilder.createTable(CqlIdentifier.fromCql("clients"))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                        .withColumn(CqlIdentifier.fromCql("first_name"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("last_name"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("personal_id"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("discriminator"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("membership_level"), DataTypes.INT)
                        .withColumn(CqlIdentifier.fromCql("discount"), DataTypes.DOUBLE)
                        .build();
        getSession().execute(createClients);
    }

    public void deleteData() {
        SimpleStatement deleteClients = SchemaBuilder.dropTable(CqlIdentifier.fromCql("clients")).ifExists().build();
        getSession().execute(deleteClients);
    }

}
