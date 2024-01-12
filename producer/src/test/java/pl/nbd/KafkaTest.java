package pl.nbd;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.MessageFormatter;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import pl.nbd.entities.Client;
import pl.nbd.entities.Default;
import pl.nbd.mappers.MongoUUID;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KafkaTest {

    KafkaProducer<UUID, String> producer;
    List<KafkaConsumer<UUID, String>> consumerGroup;

    String topicName = "rents";

    String consumerGroupId = "rentConsumers";

    @Test
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


    @Test
    public void initProducer() throws ExecutionException, InterruptedException {
        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.CLIENT_ID_CONFIG, "local");
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192, kafka2:9292, kafka3:9392");
        producer = new KafkaProducer<>(producerConfig);

        Client client = new Default(new MongoUUID(UUID.randomUUID()), "Adam", "Przybylski", "12345678901");
        JSONObject jsonObject = new JSONObject(client);
        String jsonClient = jsonObject.toString();
        ProducerRecord<UUID, String> record = new ProducerRecord<>(topicName, client.getId().getUuid(), jsonClient);
        Future<RecordMetadata> sent = producer.send(record);
        RecordMetadata recordMetadata = sent.get();
        System.out.println(recordMetadata);
    }

    public void initConsumerGroup() {
        consumerGroup = new ArrayList<>();

        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        //consumerConfig.put(ConsumerConfig.GROUP_INSTANCE_ID_CONFIG, consumerGroupId);
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192, kafka2:9292, kafka3:9392");

        for (int i = 0; i < 2; i++) {
            KafkaConsumer<UUID, String> consumer = new KafkaConsumer<>(consumerConfig);
            consumer.subscribe(List.of(topicName));
            consumerGroup.add(consumer);
        }
    }

    private void consume(KafkaConsumer<UUID, String> consumer) {
        try {
            consumer.poll(0);
            Set<TopicPartition> consumerAssigment = consumer.assignment();
            System.out.println(consumer.groupMetadata().memberId() + " " + consumerAssigment);
            consumer.seekToBeginning(consumerAssigment);

            Duration timeout = Duration.of(100, ChronoUnit.MILLIS);
            MessageFormat formattter = new MessageFormat("Konsument {5}, Temat {0}, partycja {1}, offset {2, number, integer}, klucz {3}, wartość {4}");
            while (true) {
                ConsumerRecords<UUID, String> records = consumer.poll(timeout);
                for (ConsumerRecord<UUID, String> record : records) {
                    String result = formattter.format(new Object[]{
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value(),
                            consumer.groupMetadata().memberId()
                    });
                    System.out.println(result);
                }
            }


        } catch (WakeupException e) {
            System.out.println("Job finished");
        }
    }


    @Test
    public void consumeTopicsByGroup() throws InterruptedException {
        initConsumerGroup();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (KafkaConsumer<UUID, String> consumer : consumerGroup) {
            executorService.execute(() -> consume(consumer));
        }
        Thread.sleep(10000);
        for (KafkaConsumer<UUID, String> consumer : consumerGroup) {
            consumer.wakeup();
        }
        executorService.shutdown();
    }

}
