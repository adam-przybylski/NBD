package pl.nbd.repositories;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class AbstractRedisRepository {
    private static JedisPooled pool;

    public void initConnection() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("config.properties"));

            String host = properties.getProperty("redis.host");
            int port = Integer.parseInt(properties.getProperty("redis.port"));

            JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();

            pool = new JedisPooled(new HostAndPort(host, port), clientConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
