package pl.nbd.repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import pl.nbd.entities.Client;
import pl.nbd.entities.Rent;
import pl.nbd.entities.Room;
import pl.nbd.mappers.MongoUUID;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class RentRepository extends AbstractMongoRepository {

    private final MongoCollection<Rent> rentCollection;
    private final MongoCollection<Rent> archivedRentCollection;

    public RentRepository() {
        this.rentCollection = initDbConnection().getCollection("rents", Rent.class);
        this.archivedRentCollection = initDbConnection().getCollection("archivedRents", Rent.class);
    }

    public void insertRent(Rent rent) {
        rentCollection.insertOne(rent);
    }

    public void updateEndRentDate(Rent rent) {
        Bson filter = Filters.eq("_id", rent.getRentId());
        Bson setUpdate = Updates.set("rentEndDate", rent.getRentEndDate());
        rentCollection.updateOne(filter, setUpdate);
    }

    public List<Rent> readRentsbyClient(Client client) {
        Bson filter = Filters.eq("client", client);
        List<Rent> rentList = new ArrayList<>();
        return rentCollection.find(filter).into(rentList);
    }

    public List<Rent> readRentsbyRoom(Room room) {
        Bson filter = Filters.eq("room", room);
        List<Rent> rentList = new ArrayList<>();
        return rentCollection.find(filter).into(rentList);
    }

    public List<Rent> readAllRents() {
        List<Rent> rentList = new ArrayList<>();
        return rentCollection.find().into(rentList);
    }

    public void dropRentCollection() {
        rentCollection.drop();
    }



}
