package pl.nbd;

import pl.nbd.repositories.Consumer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InterruptedException {

        Consumer.consumeTopicsByGroup("rents");
    }
}
