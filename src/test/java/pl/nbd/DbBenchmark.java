package pl.nbd;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import pl.nbd.entities.Room;
import pl.nbd.mappers.MongoUUID;
import pl.nbd.repositories.RoomRepository;

import java.util.UUID;

@State(Scope.Benchmark)
public class DbBenchmark {

    private RoomRepository roomRepository;

    @Setup
    public void setup() {
        // Initialize the repository only once

        roomRepository = new RoomRepository();
        for(int i = 800; i <= 1200; i++){
            roomRepository.insertRoom(new Room(new MongoUUID(UUID.randomUUID()), i, 31, 12));
        }
    }

    @Benchmark
    public void mongoReadBenchmark() {
//        roomRepository.readRoomByRoomNumberFromMongo(667);
        for (int i = 800; i <= 1200; i++) {
            roomRepository.readRoomByRoomNumberFromMongo(i);
        }
    }

    @Benchmark
    public void redisReadBenchmark() {
//        roomRepository.readRoomByRoomNumberFromRedis(667);
        for (int i = 800; i <= 1200; i++) {
            roomRepository.readRoomByRoomNumberFromRedis(i);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(DbBenchmark.class.getSimpleName())
                .forks(1)  // Set the number of forks to 1
                .build();

        new org.openjdk.jmh.runner.Runner(opt).run();
    }
}
