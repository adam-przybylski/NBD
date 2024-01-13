package pl.nbd.repositories;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.json.JSONObject;
import pl.nbd.entities.*;
import pl.nbd.mappers.MongoUUID;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer {

    static String consumerGroupId = "rentConsumers2";

    static List<KafkaConsumer<UUID, String>> consumerGroup;

    static String topicName;

    static RentRepository rentRepository = new RentRepository();

    private static void initConsumerGroup() {
        consumerGroup = new ArrayList<>();
        Properties consumerConfig = new Properties();
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
//        consumerConfig.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192, kafka2:9292, kafka3:9392");

        for (int i = 0; i < 2; i++) {
            KafkaConsumer<UUID, String> consumer = new KafkaConsumer<>(consumerConfig);
            consumer.subscribe(List.of(topicName));
            consumerGroup.add(consumer);
        }
    }

    private static void consume(KafkaConsumer<UUID, String> consumer) {
        initConsumerGroup();
        try {
            consumer.poll(0);
            Set<TopicPartition> consumerAssigment = consumer.assignment();
            System.out.println(consumer.groupMetadata().memberId() + " " + consumerAssigment);
//            consumer.seekToBeginning(consumerAssigment);

            Duration timeout = Duration.of(100, ChronoUnit.MILLIS);
            MessageFormat formattter = new MessageFormat("Konsument {5}, Temat {0}, partycja {1}, offset {2, number, integer}, klucz {3}, wartość " +
                    "{4}");
            while (true) {
                ConsumerRecords<UUID, String> records = consumer.poll(timeout);
                for (ConsumerRecord<UUID, String> record : records) {
                    Rent rent = mapStringToRent(record.value());
                    rentRepository.insertRent(rent);
                    String result = formattter.format(new Object[]{
                            record.topic(),
                            record.partition(),
                            record.offset(),
                            record.key(),
                            record.value(),
                            consumer.groupMetadata().memberId()
                    });
                    System.out.println(result);
                    consumer.commitSync();
                }
            }

        } catch (WakeupException e) {
            System.out.println("Job finished");
        }
    }


    public static void consumeTopicsByGroup(String name) throws InterruptedException {
        topicName = name;
        initConsumerGroup();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (KafkaConsumer<UUID, String> consumer : consumerGroup) {
            executorService.execute(() -> consume(consumer));
        }
        Thread.sleep(60000);
        for (KafkaConsumer<UUID, String> consumer : consumerGroup) {
            consumer.wakeup();
        }
        executorService.shutdown();
    }

    private static Rent mapStringToRent(String value) {
        JSONObject jsonObject = new JSONObject(value);

        MongoUUID rentId = new MongoUUID(UUID.fromString(jsonObject.getString("rentId")));

        GregorianCalendar rentStartDate = new GregorianCalendar();
        rentStartDate.setTimeInMillis(jsonObject.getLong("rentStartDate"));

        GregorianCalendar rentEndDate = new GregorianCalendar();
        double rentCost = 0;

        try {
            rentEndDate.setTimeInMillis(jsonObject.getLong("rentEndDate"));
            rentCost = jsonObject.getDouble("rentCost");
        } catch (Exception e) {
            rentEndDate = null;
        }

        Client client = mapStringToClient(jsonObject.getJSONObject("client"));
        Room room = mapStringToRoom(jsonObject.getJSONObject("room"));

        return new Rent(rentId, rentCost, rentStartDate, rentEndDate, client, room);
    }

    private static Room mapStringToRoom(JSONObject room) {
        MongoUUID roomId = new MongoUUID(UUID.fromString(room.getJSONObject("id").getString("uuid")));
        int roomNumber = room.getInt("roomNumber");
        int roomCapacity = room.getInt("roomCapacity");
        double basePrice = room.getDouble("basePrice");
        return new Room(roomId, roomNumber, roomCapacity, basePrice);
    }

    private static Client mapStringToClient(JSONObject client) {
        MongoUUID clientId = new MongoUUID(UUID.fromString(client.getJSONObject("id").getString("uuid")));
        String firstName = client.getString("firstName");
        String lastName = client.getString("lastName");
        String personalId = client.getString("personalId");

        if (client.getString("type").equals("Default"))
            return new Default(clientId, firstName, lastName, personalId);
        else if (client.getString("type").equals("PremiumClient"))
            return new PremiumClient(clientId, firstName, lastName, personalId, client.getInt("membershipLevel"), client.getDouble("discount"));
        else
            return new RegularClient(clientId, firstName, lastName, personalId, client.getDouble("discount"));
    }
}
