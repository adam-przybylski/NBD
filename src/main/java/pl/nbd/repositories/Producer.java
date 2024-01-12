package pl.nbd.repositories;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.bson.json.JsonObject;
import org.json.JSONObject;
import pl.nbd.entities.Client;
import pl.nbd.entities.Default;
import pl.nbd.entities.Rent;
import pl.nbd.mappers.MongoUUID;


import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer {
    KafkaProducer<UUID, String> producer;

    String topicName = "rents";

    public void createTopic() throws InterruptedException {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192, kafka2:9292, kafka3:9392");
        int partitionsNumber = 3;
        short replicationFactor = 3;

        try (Admin admin = Admin.create(properties)) {
            NewTopic newTopic = new NewTopic(topicName, partitionsNumber, replicationFactor);
            CreateTopicsOptions options = new CreateTopicsOptions()
                    .timeoutMs(1000)
                    .validateOnly(false)
                    .retryOnQuotaViolation(true);
            CreateTopicsResult result = admin.createTopics(List.of(newTopic), options);
            KafkaFuture<Void> futureResult = result.values().get(topicName);
            futureResult.get();
        } catch (ExecutionException e) {
            System.out.println(e.getCause());
        }
    }

    public void initProducer() throws ExecutionException, InterruptedException {
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "local");
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192, kafka2:9292, kafka3:9392");
        producer = new KafkaProducer<>(producerConfig);
    }

    public void sendRent(Rent rent) throws ExecutionException, InterruptedException {
        initProducer();
        JSONObject jsonRent = new JSONObject(rent);
        ProducerRecord<UUID, String> record = new ProducerRecord<>(topicName, rent.getRentId().getUuid(), jsonRent.toString());
        Future<RecordMetadata> sent = producer.send(record);
        RecordMetadata recordMetadata = sent.get();
        System.out.println(recordMetadata);
    }

}
