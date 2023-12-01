//package pl.nbd.repositories;
//
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.model.Filters;
//import com.mongodb.client.model.Updates;
//import org.bson.conversions.Bson;
//import pl.nbd.entities.Client;
//import pl.nbd.entities.Default;
//import pl.nbd.entities.PremiumClient;
//import pl.nbd.entities.RegularClient;
//
//import java.util.ArrayList;
//
//public class ClientRepository extends AbstractDatabaseRepository {
//
//
//    public void insertClient(Client client) {
//        clientCollection.insertOne(client);
//    }
//
//    public ArrayList<Client> readAllClients() {
//        ArrayList<Client> premiumClients = readPremiumClients();
//        ArrayList<Client> regularClients = readRegularClients();
//        ArrayList<Client> defaultClients = readDefaultClients();
//        ArrayList<Client> allClients = new ArrayList<>();
//        allClients.addAll(premiumClients);
//        allClients.addAll(regularClients);
//        allClients.addAll(defaultClients);
//        return allClients;
//    }
//
//    public void dropClientCollection() {
//        clientCollection.drop();
//    }
//
//    public ArrayList<Client> readPremiumClients() {
//        MongoCollection<PremiumClient> premiumClientCollection = initDbConnection().getCollection("clients", PremiumClient.class);
//        Bson filter = Filters.eq("_clazz", "PremiumClient");
//        return premiumClientCollection.find(filter).into(new ArrayList<>());
//    }
//
//    public ArrayList<Client> readRegularClients() {
//        MongoCollection<RegularClient> regularClientCollection = initDbConnection().getCollection("clients", RegularClient.class);
//        Bson filter = Filters.eq("_clazz", "RegularClient");
//        return regularClientCollection.find(filter).into(new ArrayList<>());
//    }
//
//    public ArrayList<Client> readDefaultClients() {
//        MongoCollection<Default> defaultClientCollection = initDbConnection().getCollection("clients", Default.class);
//        Bson filter = Filters.eq("_clazz", "Default");
//        return defaultClientCollection.find(filter).into(new ArrayList<>());
//    }
//
//    public void updateClient(String personalId, String lastName) {
//        Bson filter = Filters.eq("personalId", personalId);
//        Bson setUpdate = Updates.set("lastName", lastName);
//        clientCollection.updateOne(filter, setUpdate);
//    }
//}
