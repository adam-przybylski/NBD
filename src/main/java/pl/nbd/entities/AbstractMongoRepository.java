package pl.nbd.entities;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Convention;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;
import java.util.UUID;

public abstract class AbstractMongoRepository implements AutoCloseable {
    private static final ConnectionString connectionString = new ConnectionString("mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single");

    private final MongoCredential credential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

    private CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
            CodecRegistries.fromProviders(new MongoUUIDCodecProvider()),
            MongoClientSettings.getDefaultCodecRegistry());
    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
            .automatic(true)
            .conventions(List.of(Conventions.ANNOTATION_CONVENTION, Conventions.CLASS_AND_PROPERTY_CONVENTION))
            .build());



    private MongoClient mongoClient;
    private MongoDatabase db;

    public MongoDatabase initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(new MongoUUIDCodecProvider()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry))
                .build();

        mongoClient = MongoClients.create(settings);
        return db = mongoClient.getDatabase("rentaroom");
    }

    public void close() {
        mongoClient.close();
    }
}
