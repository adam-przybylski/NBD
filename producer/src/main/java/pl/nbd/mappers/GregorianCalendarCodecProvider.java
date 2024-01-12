package pl.nbd.mappers;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import java.lang.reflect.Type;
import java.util.GregorianCalendar;
import java.util.List;

public class GregorianCalendarCodecProvider implements CodecProvider {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry codecRegistry) {
        if (clazz == GregorianCalendar.class) {
            return (Codec<T>) new GregorianCalendarCodec();
        }
        return null;
    }
}
