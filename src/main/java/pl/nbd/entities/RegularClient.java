package pl.nbd.entities;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.nbd.mappers.MongoUUID;

@BsonDiscriminator(key = "_clazz", value = "RegularClient")
public class RegularClient extends Client {

    @BsonProperty("discount")
    private double discount;

    public RegularClient(String firstName, String lastName, String personalId, double discount) {
        super(firstName, lastName, personalId);
        this.discount = discount;
    }

    @BsonCreator
    public RegularClient(@BsonProperty("id") MongoUUID id,
                         @BsonProperty("firstName") String firstName,
                         @BsonProperty("lastName") String lastName,
                         @BsonProperty("personalId") String personalId,
                         @BsonProperty("discount") double discount) {
        super(id, firstName, lastName, personalId);
        this.discount = discount;
    }

    public RegularClient() {

    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public double applyDiscount(double price) {
        return price * this.discount;
    }

    @Override
    public String toString() {
        return "RegularClient{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", personalId='" + getPersonalId() + '\'' +
                ", discount=" + discount +
                '}';
    }

}
