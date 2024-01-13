package pl.nbd.repositories;

import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
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

    public RentRepository()  {
        this.rentCollection = initDbConnection().getCollection("rents", Rent.class);
        try {
            Producer.createTopic("rents2");
        } catch (Exception e) {
            System.out.println("Topic already exists");
        }

    }

    public void insertRent(Rent rent) throws IllegalStateException {
        try (ClientSession clientSession = mongoClient.startSession()) {
            try {
                clientSession.startTransaction();

                MongoCollection<Client> clientCollection = db
                        .getCollection("clients", Client.class)
                        .withWriteConcern(WriteConcern.MAJORITY);
                if (clientCollection.find(Filters.eq("personalId", rent.getClient().getPersonalId())).first() == null) {
                    clientCollection.insertOne(rent.getClient());
                }

                MongoCollection<Room> roomCollection = db
                        .getCollection("rooms", Room.class)
                        .withWriteConcern(WriteConcern.MAJORITY);
                if(roomCollection.find(Filters.eq("roomNumber", rent.getRoom().getRoomNumber())).first() == null) {
                    roomCollection.insertOne(rent.getRoom());
                }

                MongoCollection<Rent> rentCollection = db
                        .getCollection("rents", Rent.class)
                        .withWriteConcern(WriteConcern.MAJORITY);

                List<Rent> rents = rentCollection.find().into(new ArrayList<>());

                boolean roomAlreadyRented = rents.stream()
                        .anyMatch(rent1 -> (rent1.getRoom().getRoomNumber() == rent.getRoom().getRoomNumber()) && (rent1.getRentEndDate() == null));

                if (roomAlreadyRented) {
                    throw new IllegalStateException("Room is already rented");
                }
                rentCollection.insertOne(rent);
                clientSession.commitTransaction();
            } catch (Exception e) {
                clientSession.abortTransaction();
                throw e;
            }
            Producer.sendRent(rent);
        }
    }

    public void updateEndRentDate(Rent rent) {
        Bson filter = Filters.eq("_id", rent.getRentId());
        Bson setUpdate = Updates.set("rentEndDate", rent.getRentEndDate());
        Bson setUpdate2 = Updates.set("rentCost", rent.getRentCost());
        List<Bson> updates = new ArrayList<>();
        updates.add(setUpdate);
        updates.add(setUpdate2);
        rentCollection.updateOne(filter, updates);
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
