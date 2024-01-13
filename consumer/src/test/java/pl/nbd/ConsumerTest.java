package pl.nbd;


import org.junit.jupiter.api.Test;
import pl.nbd.repositories.Consumer;


public class ConsumerTest {

    @Test
    public void consumeTest() throws InterruptedException {
        Consumer.consumeTopicsByGroup("rents2");
    }

}
