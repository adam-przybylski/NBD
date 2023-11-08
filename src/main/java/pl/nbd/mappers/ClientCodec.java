package pl.nbd.mappers;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;
import pl.nbd.entities.Client;
import pl.nbd.entities.Default;
import pl.nbd.entities.PremiumClient;
import pl.nbd.entities.RegularClient;

public class ClientCodec implements Codec<Client> {
    private final CodecRegistry registry;
    private final Codec<MongoUUID> uuidCodec;

    public ClientCodec(CodecRegistry registry) {
        this.registry = registry;
        this.uuidCodec = registry.get(MongoUUID.class);
    }

    @Override
    public Client decode(BsonReader reader, DecoderContext decoderContext) {
        Client client = null;
        Document document = new Document();

        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String fieldName = reader.readName();
            if (fieldName.equals("_id")) {
                MongoUUID mongoUUID = uuidCodec.decode(reader, decoderContext);
                document.put("_id", mongoUUID);
            } else {
                document.put(fieldName, readValue(reader, decoderContext, fieldName));
            }
        }
        reader.readEndDocument();

        String clazz = document.getString("_clazz");
        if (clazz != null) {
            if (clazz.equals("PremiumClient")) {
                client = new PremiumClient();
                client.setDiscount(document.getDouble("discount"));
                client.setMembershipLevel(document.getInteger("membershipLevel"));
            } else if (clazz.equals("RegularClient")) {
                client = new RegularClient();
                client.setDiscount(document.getDouble("discount"));
            } else if (clazz.equals("Default")) {
                client = new Default();
            } else {
                throw new UnsupportedOperationException("Unsupported client type: " + clazz);
            }
        }

        client.setFirstName(document.getString("firstName"));
        client.setLastName(document.getString("lastName"));
        client.setPersonalId(document.getString("personalId"));
        client.setId(document.get("_id", MongoUUID.class));

        return client;
    }

    @Override
    public void encode(BsonWriter writer, Client value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        writer.writeString("_clazz", value.getClass().getSimpleName()); // Add the _clazz field
        writer.writeString("firstName", value.getFirstName());
        writer.writeString("lastName", value.getLastName());
        writer.writeString("personalId", value.getPersonalId());

        if (value instanceof PremiumClient) {
            writer.writeDouble("discount", (value).getDiscount());
            writer.writeInt32("membershipLevel", (value).getMembershipLevel());
        } else if (value instanceof RegularClient) {
            writer.writeDouble("discount", (value).getDiscount());
        }


        writer.writeName("_id");
        uuidCodec.encode(writer, value.getId(), encoderContext);

        writer.writeEndDocument();
    }

    @Override
    public Class<Client> getEncoderClass() {
        return Client.class;
    }

    private void writeValue(BsonWriter writer, EncoderContext encoderContext, Object value) {
        if (value instanceof String) {
            writer.writeString((String) value);
        } else if (value instanceof Integer) {
            writer.writeInt32((int) value);
        } else if (value instanceof Double) {
            writer.writeDouble((double) value);
        }
    }

    private Object readValue(BsonReader reader, DecoderContext decoderContext, String fieldName) {
        BsonType type = reader.getCurrentBsonType();
        switch (type) {
            case STRING:
                return reader.readString();
            case INT32:
                return reader.readInt32();
            case DOUBLE:
                return reader.readDouble();
            default:
                throw new UnsupportedOperationException("Unsupported BSON type for field: " + fieldName);
        }
    }
}
