package pl.nbd.entities;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import pl.nbd.mappers.MongoUUID;


@BsonDiscriminator(key = "_clazz", value = "Default")
public class Default extends Client {

    public Default(String firstName, String lastName, String personalId) {
        super(firstName, lastName, personalId);
    }


    @BsonCreator
    public Default(@BsonProperty("id") MongoUUID id,
                   @BsonProperty("firstName") String firstName,
                   @BsonProperty("lastName") String lastName,
                   @BsonProperty("personalId") String personalId) {
        super(id, firstName, lastName, personalId);
    }

    public Default() {

    }

    @Override
    public double applyDiscount(double price) {
        return 0;
    }

    @Override
    public String toString() {
        return "Default{" +
                "id=" + getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", personalId='" + getPersonalId() + '\'' +
                '}';
    }

    @Override
    public void setDiscount(double discount) {

    }

    @Override
    public void setMembershipLevel(int membershipLevel) {

    }

    @Override
    public int getMembershipLevel() {
        return 0;
    }

    @Override
    public double getDiscount() {
        return 0;
    }

}
