package pl.nbd.repositories;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import pl.nbd.App;
import pl.nbd.mappers.ClientProvider;
import pl.nbd.mappers.GregorianCalendarCodecProvider;
import pl.nbd.mappers.MongoUUIDCodecProvider;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

public abstract class AbstractDatabaseRepository implements AutoCloseable {
    private static final ConnectionString connectionString = new ConnectionString("mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single");

    private final MongoCredential credential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

    private static JedisPooled pool;


    private CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
            CodecRegistries.fromProviders(new MongoUUIDCodecProvider()),
            MongoClientSettings.getDefaultCodecRegistry());
    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder()
            .automatic(true)
            .conventions(List.of(Conventions.ANNOTATION_CONVENTION, Conventions.CLASS_AND_PROPERTY_CONVENTION))
            .build());


    protected MongoClient mongoClient;
    protected MongoDatabase db;

    public MongoDatabase initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(new MongoUUIDCodecProvider()),
                        CodecRegistries.fromProviders(new ClientProvider()),
                        CodecRegistries.fromProviders(new GregorianCalendarCodecProvider()),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry))
                .build();

        mongoClient = MongoClients.create(settings);
        return db = mongoClient.getDatabase("rentaroom");
    }

    public static JedisPooled getPool() {
        return pool;
    }

    public void initRedisConnection() {
        try {
            Properties properties = new Properties();
            properties.load(App.class.getClassLoader().getResourceAsStream("redisConfig.properties"));

            String host = properties.getProperty("redis.host");
            int port = Integer.parseInt(properties.getProperty("redis.port"));

            JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();

            pool = new JedisPooled(new HostAndPort(host, port), clientConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        mongoClient.close();
    }
}
