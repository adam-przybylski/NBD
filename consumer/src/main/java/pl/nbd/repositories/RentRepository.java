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
        } catch (Exception e) {
            System.out.println("Topic already exists");
        }

    }

    public void insertRent(Rent rent) throws IllegalStateException {
        try (ClientSession clientSession = mongoClient.startSession()) {
            try {
                clientSession.startTransaction();

                MongoCollection<Rent> rentCollection = db
                        .getCollection("rents", Rent.class)
                        .withWriteConcern(WriteConcern.MAJORITY);

                rentCollection.insertOne(rent);
                clientSession.commitTransaction();
            } catch (Exception e) {
                clientSession.abortTransaction();
                throw e;
            }
        }
    }


    public void dropRentCollection() {
        rentCollection.drop();
    }



}
