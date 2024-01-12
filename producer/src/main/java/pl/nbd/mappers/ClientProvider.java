package pl.nbd.mappers;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import pl.nbd.entities.Client;
import pl.nbd.entities.Default;
import pl.nbd.entities.PremiumClient;
import pl.nbd.entities.RegularClient;

public class ClientProvider implements CodecProvider {
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry codecRegistry) {
        if (clazz == Client.class || clazz == Default.class || clazz == RegularClient.class || clazz == PremiumClient.class) {
            return (Codec<T>) new ClientCodec(codecRegistry);
        }
        return null;
    }
}
